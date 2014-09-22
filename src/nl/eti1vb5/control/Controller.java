package nl.eti1vb5.control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.eti1vb5.control.database.DatabaseController;
import nl.eti1vb5.control.serial.SerialController;
import nl.eti1vb5.model.MeasuredValue;
import nl.eti1vb5.model.WeatherStation;
import nl.eti1vb5.view.ControlPane;
import nl.eti1vb5.view.ForecastPane;
import nl.eti1vb5.view.GraphPane;
import nl.eti1vb5.view.MenuPane;
import nl.eti1vb5.view.TerminalPane;
import nl.eti1vb5.view.WeatherPane;
import nl.eti1vb5.view.WebPane;

/**
 * Controller volgens het MVC-ontwerppatroon 
 * Kent het model en de views en handelt alle events af
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.3
 */

public class Controller extends Application {
	// Model
	private WeatherStation model;
	
	// Control
	// Serial controller
	private SerialController serial;

	// Database controller
	private DatabaseController database;

	// View
	// Userinterface
	private ControlPane controlPane;

	// ControlPane
	// Menu
	private MenuPane menu;
	
	// Pane
	private TerminalPane terminal;
	private WebPane extWeather;
	private WeatherPane center;
	
	// WeatherPane
	private ForecastPane voorspelling;
	private GraphPane grafiek;
	
	
	/**
	 * Constructor voor het initialiseren van alle attributen
	 * Initialiseer het model en de view en koppelt deze aan elkaar
	 */
	public Controller() {
		// Model
		model = new WeatherStation();
		
		// Control
		// Database Controller
		database = new DatabaseController(model);
		
		// Vul model met laatste waarden uit database
		database.initialize();

		// Serial controller
		serial = new SerialController(model, database);
		
		// View
		// WeatherPane
		voorspelling = new ForecastPane();
		grafiek = new GraphPane();
		
		grafiek.setTemperatuurDHTData(model.getTemperatuurDHT());
		grafiek.setTemperatuurBMPData(model.getTemperatuurBMP());
		grafiek.setLuchtdrukData(model.getLuchtdruk());
		grafiek.setLuchtvochtigheidsData(model.getLuchtvochtigheid());
		grafiek.setDauwpuntData(model.getDauwpunt());
		grafiek.setLichtData(model.getLicht());
		
		grafiek.addGraphChoiceHandler(new GraphHandler());
		grafiek.addDataTypeHandler(new DataHandler());
		
		grafiek.selectFirst();
		
		// ControlPane
		// Menu
		menu = new MenuPane();
		
		// Pane
		terminal = new TerminalPane();
		extWeather = new WebPane();
		center = new WeatherPane(voorspelling, grafiek);
		
		// Userinterface
		controlPane = new ControlPane(menu, terminal, extWeather, center);
		
		
		// Handlers
		// File menu handler
		controlPane.addSaveSetOnAction(new SaveHandler());
		controlPane.addConnectSetOnAction(new ConnectHandler());
		controlPane.addExitSetOnAction(new ExitHandler());

		// Database menu handler
		controlPane.addTestSetOnAction(new TestHandler());
		controlPane.addPrintSetOnAction(new PrintHandler());
		controlPane.addDeleteSetOnAction(new DeleteHandler());

		// Terminal menu handler
		controlPane.addClearSetOnAction(new ClearHandler());
	}

	/**
	 * Methode om de applicatie mee te starten
	 * Maakt een scene aan van de controlpane en showt deze op het scherm
	 * 
	 * @param primaryStage stage waar de userinterface in weergegeven wordt
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(controlPane);

		primaryStage.setOnCloseRequest(new CloseHandler());

		primaryStage.setScene(scene);
		primaryStage.setTitle("Weerstation");
		primaryStage.show();
	}
	
	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Verandert het soort grafiek aan de hand van wat de gebruiker geselecteeerd heeft
	 */
	class GraphHandler implements ChangeListener<String> {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			switch(newValue) {
			case "Vlakdiagram":
				grafiek.addVlakdiagram();
				break;
			case "Bellendiagram":
				grafiek.addBellendiagram();
				break;
			case "Spreidingsdiagram":
				grafiek.addSpreidingsdiagram();
				break;
			case "Lijndiagram":
				grafiek.addLijndiagram();
				break;
			}
		}
	}
	
	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Verandert het soort data dat de grafiek laat zien aan de hand van wat de gebruiker geselecteeerd heeft
	 */
	class DataHandler implements ChangeListener<String> {
		@Override
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			switch(newValue) {
			case "Temperatuur":
				grafiek.addTemperatuurData();			
				break;
			case "Luchtdruk":
				grafiek.addLuchtdrukData();
				break;
			case "Luchvochtigheid":
				grafiek.addLuchtvochtigheidsData();
				break;
			case "Dauwpunt":
				grafiek.addDauwpuntData();
				break;
			case "Licht":
				grafiek.addLichtData();
				break;
			}
		}
	}
	
	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Sluit de seriele verbinding af op het moment de gebruiker het programma sluit
	 */
	class CloseHandler implements EventHandler<WindowEvent> {
		@Override
		public void handle(WindowEvent event) {
			serial.close();
		}
	}

	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Opent een savedialog en slaat de data uit de database op als csv file
	 */
	class SaveHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser filechooser = new FileChooser();
			filechooser.setTitle("Sla weergegevens op");
			filechooser.getExtensionFilters().add(new ExtensionFilter("csv-file", "*.csv"));
			File file = filechooser.showSaveDialog(new Stage());
			
			try {
				FileWriter filewriter = new FileWriter(file);
				
				ArrayList<MeasuredValue> output = new ArrayList<>();
				
				output.addAll(database.selectTemperatureBMP());
				output.addAll(database.selectTemperatureDHT());
				output.addAll(database.selectLuchtdruk());
				output.addAll(database.selectLuchtvochtigheid());
				output.addAll(database.selectDauwpunt());
				output.addAll(database.selectLicht());
				
				for(MeasuredValue value : output) {
					filewriter.append(value.getType() + "; ");
					filewriter.append(value.getId() + "; ");
					filewriter.append(value.getMeting() + "; ");
					filewriter.append(value.getTijd() + "\n");
				}
				
				filewriter.flush();
				filewriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Probeert verbinding met het weerstation te maken op het moment de gebruiker dit aangeeft in het menu
	 */
	class ConnectHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			serial.connect();
		}
	}

	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Stopt het programma op het moment de gebruiker dit aangeeft in het menu
	 */
	class ExitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Platform.exit();
		}
	}

	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Simuleert het weerstation met testwaarden op het moment de gebruiker dit aangeeft via het menu
	 */
	class TestHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			database.test();
		}
	}

	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Print alle waarden uit van de database op het terminal scherm wanneer de gebruiker dit aangeeft via het menu
	 */
	class PrintHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			database.print();
		}
	}

	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Verwijdert alle waarden uit de database op het moment de gebruiker dit aangeeft via het menu
	 */
	class DeleteHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			database.delete();
		}
	}

	/**
	 * Geneste klasse van de controller voor het afhandelen van events
	 * Leegt het terminalscherm op het moment de gebruiker dit aangeeft via het menu
	 */
	class ClearHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			TerminalPane.clear();
		}
	}
}
