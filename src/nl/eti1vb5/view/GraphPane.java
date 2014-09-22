package nl.eti1vb5.view;

import java.util.Arrays;
import java.util.ListIterator;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

/**
 * View volgens het MVC-ontwerppatroon 
 * Klasse om de geselecteerd grafiek weer te geven in de userinterface 
 * Maakt gebruik van een gridpane
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.0
 */

public class GraphPane extends GridPane {

	// Grafiek- en metingtitels
	private String[] graphNames;
	private String[] typeNames;

	// Grafieken
	private AreaChart<Number, Number> vlakDiagram;
	private BubbleChart<Number, Number> bellenDiagram;
	private ScatterChart<Number, Number> spreidingsDiagram;
	private LineChart<Number, Number> lijnDiagram;

	// Assen
	private NumberAxis xAxisVlakDiagram;
	private NumberAxis yAxisVlakDiagram;
	private NumberAxis xAxisBellenDiagram;
	private NumberAxis yAxisBellenDiagram;
	private NumberAxis xAxisSpreidingsDiagram;
	private NumberAxis yAxisSpreidingsDiagram;
	private NumberAxis xAxisLijnDiagram;
	private NumberAxis yAxisLijnDiagram;
	
	// Metingen
	private Series<Number, Number> seriesTemperatuurDHT;
	private Series<Number, Number> seriesTemperatuurBMP;
	private Series<Number, Number> seriesLuchtdruk;
	private Series<Number, Number> seriesLuchtvochtigheid;
	private Series<Number, Number> seriesDauwpunt;
	private Series<Number, Number> seriesLicht;

	// Grafiek- en metingkeuze
	private ChoiceBox<String> graphChoice;
	private ChoiceBox<String> typeChoice;

	/**
	 * Constructor voor het initiliaseren van de verschillende nodes Voegt de
	 * nodes toe aan de view in de juiste volgorde
	 */
	public GraphPane() {
		this.setPadding(new Insets(10));

		// Grafiek- en metingtitels
		graphNames = new String[] { "Vlakdiagram", "Bellendiagram",
				"Spreidingsdiagram", "Lijndiagram" };
		typeNames = new String[] { "Temperatuur", "Luchtdruk", "Luchvochtigheid", 
				"Dauwpunt", "Licht"};

		// Assen
		xAxisVlakDiagram = new NumberAxis(0, 24, 1);
		yAxisVlakDiagram = new NumberAxis(0, 40, 5);
		xAxisBellenDiagram = new NumberAxis(0, 24, 1);
		yAxisBellenDiagram = new NumberAxis(0, 40, 5);
		xAxisSpreidingsDiagram = new NumberAxis(0, 24, 1);
		yAxisSpreidingsDiagram = new NumberAxis(0, 40, 5);
		xAxisLijnDiagram = new NumberAxis(0, 24, 1);
		yAxisLijnDiagram = new NumberAxis(0, 40, 5);
		
		xAxisVlakDiagram.autoRangingProperty().set(true);
		yAxisVlakDiagram.autoRangingProperty().set(true);
		xAxisBellenDiagram.autoRangingProperty().set(true);
		yAxisBellenDiagram.autoRangingProperty().set(true);
		xAxisSpreidingsDiagram.autoRangingProperty().set(true);
		yAxisSpreidingsDiagram.autoRangingProperty().set(true);
		xAxisLijnDiagram.autoRangingProperty().set(true);
		yAxisLijnDiagram.autoRangingProperty().set(true);

		// Metingen
		// Temperatuur
		seriesTemperatuurBMP = new Series<>();
		seriesTemperatuurBMP.setName("TemperatuurBMP");
		seriesTemperatuurDHT = new Series<>();
		seriesTemperatuurDHT.setName("TemperatuurDHT");
		
		// Lucht
		seriesLuchtdruk = new Series<>();
		seriesLuchtdruk.setName("Luchtdruk");
		seriesLuchtvochtigheid = new Series<>();
		seriesLuchtvochtigheid.setName("Vochtigheid");
		
		// Dauw en licht
		seriesDauwpunt = new Series<>();
		seriesDauwpunt.setName("Licht");
		seriesLicht = new Series<>();
		seriesLicht.setName("Licht");
		
		// Grafieken
		vlakDiagram = new AreaChart<>(xAxisVlakDiagram, yAxisVlakDiagram);
		vlakDiagram.prefHeightProperty().bind(this.prefHeightProperty());
		vlakDiagram.prefWidthProperty().bind(this.widthProperty());

		bellenDiagram = new BubbleChart<>(xAxisBellenDiagram, yAxisBellenDiagram);
		bellenDiagram.prefHeightProperty().bind(this.prefHeightProperty());
		bellenDiagram.prefWidthProperty().bind(this.widthProperty());

		spreidingsDiagram = new ScatterChart<>(xAxisSpreidingsDiagram, yAxisSpreidingsDiagram);
		spreidingsDiagram.prefHeightProperty().bind(this.prefHeightProperty());
		spreidingsDiagram.prefWidthProperty().bind(this.widthProperty());

		lijnDiagram = new LineChart<>(xAxisLijnDiagram, yAxisLijnDiagram);
		lijnDiagram.prefHeightProperty().bind(this.prefHeightProperty());
		lijnDiagram.prefWidthProperty().bind(this.widthProperty());

		vlakDiagram.setTitle(graphNames[0]);
		bellenDiagram.setTitle(graphNames[1]);
		spreidingsDiagram.setTitle(graphNames[2]);
		lijnDiagram.setTitle(graphNames[3]);
		
		vlakDiagram.setAnimated(false);
		bellenDiagram.setAnimated(false);
		spreidingsDiagram.setAnimated(false);
		lijnDiagram.setAnimated(false);

		// Grafiekkeuze
		graphChoice = new ChoiceBox<>(FXCollections.observableList(Arrays
				.asList(graphNames)));
		this.add(graphChoice, 0, 0);
		
		// Metingkeuze
		typeChoice = new ChoiceBox<>(FXCollections.observableList(Arrays
				.asList(typeNames)));
		this.add(typeChoice, 1, 0);
		
		// Grafiek
		this.add(lijnDiagram, 0, 1, 2, 1);
	}
	
	// Methode voor het setten van de eerste items van de comboboxes
	public void selectFirst() {
		graphChoice.getSelectionModel().selectFirst();
		typeChoice.getSelectionModel().selectFirst();
	}
	
	/**
	 * Methode om aan het de grafiek selectie box een changelistener te koppelen
	 * @param listener De changelistener met daarin de actie die uitgevoert moet worden
	 */
	public void addGraphChoiceHandler(ChangeListener<String> listener) {
		graphChoice.getSelectionModel().selectedItemProperty().addListener(listener);
	}
	
	/**
	 * Methode om aan het de type selectie box een changelistener te koppelen
	 * @param listener De changelistener met daarin de actie die uitgevoert moet worden
	 */
	public void addDataTypeHandler(ChangeListener<String> listener) {
		typeChoice.getSelectionModel().selectedItemProperty().addListener(listener);
	}
	
	/**
	 * Methode om de huidige diagram te vervangen voor een bellendiagram
	 * Haalt de huidige diagram uit de lijst met nodes en voegt de nieuwe toe
	 */
	public void addBellendiagram() {
		ListIterator<Node> iterator = this.getChildren().listIterator();
		while(iterator.hasNext()) {
			Node node = iterator.next();
			if(node instanceof XYChart) {
				iterator.remove();
			}
		}
		
		this.add(bellenDiagram, 0, 1, 2, 1);
	}
	
	/**
	 * Methode om de huidige diagram te vervangen voor een vlakdiagram
	 * Haalt de huidige diagram uit de lijst met nodes en voegt de nieuwe toe
	 */
	public void addVlakdiagram() {
		ListIterator<Node> iterator = this.getChildren().listIterator();
		while(iterator.hasNext()) {
			Node node = iterator.next();
			if(node instanceof XYChart) {
				iterator.remove();
			}
		}
		
		this.add(vlakDiagram, 0, 1, 2, 1);
	}
	
	/**
	 * Methode om de huidige diagram te vervangen voor een lijndiagram
	 * Haalt de huidige diagram uit de lijst met nodes en voegt de nieuwe toe
	 */
	public void addLijndiagram() {
		ListIterator<Node> iterator = this.getChildren().listIterator();
		while(iterator.hasNext()) {
			Node node = iterator.next();
			if(node instanceof XYChart) {
				iterator.remove();
			}
		}
		
		this.add(lijnDiagram, 0, 1, 2, 1);
	}
	
	/**
	 * Methode om de huidige diagram te vervangen voor een spreidingsdiagram
	 * Haalt de huidige diagram uit de lijst met nodes en voegt de nieuwe toe
	 */
	public void addSpreidingsdiagram() {
		ListIterator<Node> iterator = this.getChildren().listIterator();
		while(iterator.hasNext()) {
			Node node = iterator.next();
			if(node instanceof XYChart) {
				iterator.remove();
			}
		}
		
		this.add(spreidingsDiagram, 0, 1, 2, 1);
	}
	
	/**
	 * Methode om de huidige data te vervangen voor de temperatuurData
	 * Leegt de lijst met data van de grafiek en voegt de nieuwe toe
	 */
	@SuppressWarnings("unchecked")
	public void addTemperatuurData() {
		for(Node node : this.getChildren()) {
			if(node instanceof XYChart<?,?>) {
				XYChart<Number, Number> chart = (XYChart<Number, Number>) node;				
				chart.getData().clear();
				chart.getData().add(seriesTemperatuurDHT);
				chart.getData().add(seriesTemperatuurBMP);
			}
		}
	}
	
	/**
	 * Methode om de huidige data te vervangen voor de luchtdrukData
	 * Leegt de lijst met data van de grafiek en voegt de nieuwe toe
	 */
	@SuppressWarnings("unchecked")
	public void addLuchtdrukData() {
		for(Node node : this.getChildren()) {
			if(node instanceof XYChart<?,?>) {
				XYChart<Number, Number> chart = (XYChart<Number, Number>) node;				
				chart.getData().clear();
				chart.getData().add(seriesLuchtdruk);
			}
		}
	}
	
	/**
	 * Methode om de huidige data te vervangen voor de luchtvochtigheidsData
	 * Leegt de lijst met data van de grafiek en voegt de nieuwe toe
	 */
	@SuppressWarnings("unchecked")
	public void addLuchtvochtigheidsData() {
		for(Node node : this.getChildren()) {
			if(node instanceof XYChart<?,?>) {
				XYChart<Number, Number> chart = (XYChart<Number, Number>) node;				
				chart.getData().clear();
				chart.getData().add(seriesLuchtvochtigheid);
			}
		}
	}
	
	/**
	 * Methode om de huidige data te vervangen voor de dauwpuntData
	 * Leegt de lijst met data van de grafiek en voegt de nieuwe toe
	 */
	@SuppressWarnings("unchecked")
	public void addDauwpuntData() {
		for(Node node : this.getChildren()) {
			if(node instanceof XYChart<?,?>) {
				XYChart<Number, Number> chart = (XYChart<Number, Number>) node;				
				chart.getData().clear();
				chart.getData().add(seriesDauwpunt);
			}
		}
	}
	
	/**
	 * Methode om de huidige data te vervangen voor de lichtData
	 * Leegt de lijst met data van de grafiek en voegt de nieuwe toe
	 */
	@SuppressWarnings("unchecked")
	public void addLichtData() {
		for(Node node : this.getChildren()) {
			if(node instanceof XYChart<?,?>) {
				XYChart<Number, Number> chart = (XYChart<Number, Number>) node;				
				chart.getData().clear();
				chart.getData().add(seriesLicht);
			}
		}
	}
	
	/**
	 * Setter voor het vervangen van de huidige temperatuurdata van de dht sensor met een nieuwe lijst
	 * @param value De nieuwe lijst met de temperatuurdata van de dht sensor
	 */
	public void setTemperatuurDHTData(ObservableList<Data<Number, Number>> value) {
		seriesTemperatuurDHT.setData(value);
	}
	
	/**
	 * Setter voor het vervangen van de huidige temperatuurdata van de bmp sensor met een nieuwe lijst
	 * @param value De nieuwe lijst met de temperatuurdata van de bmp sensor
	 */
	public void setTemperatuurBMPData(ObservableList<Data<Number, Number>> value) {
		seriesTemperatuurBMP.setData(value);
	}
	
	/**
	 * Setter voor het vervangen van de huidige luchtdrukdata met een nieuwe lijst
	 * @param value De nieuwe lijst met de luchtdrukdata
	 */
	public void setLuchtdrukData(ObservableList<Data<Number, Number>> value) {
		seriesLuchtdruk.setData(value);
	}
	
	/**
	 * Setter voor het vervangen van de huidige luchtvochtigheidsdata met een nieuwe lijst
	 * @param value De nieuwe lijst met de luchtvochtigheidsdata
	 */
	public void setLuchtvochtigheidsData(ObservableList<Data<Number, Number>> value) {
		seriesLuchtvochtigheid.setData(value);
	}
	
	/**
	 * Setter voor het vervangen van de huidige dauwpuntdata met een nieuwe lijst
	 * @param value De nieuwe lijst met de dauwpuntdata
	 */
	public void setDauwpuntData(ObservableList<Data<Number, Number>> value) {
		seriesDauwpunt.setData(value);
	}
	
	/**
	 * Setter voor het vervangen van de huidige lichtdata met een nieuwe lijst
	 * @param value De nieuwe lijst met de lichtdata
	 */
	public void setLichtData(ObservableList<Data<Number, Number>> value) {
		seriesLicht.setData(value);
	}
}
