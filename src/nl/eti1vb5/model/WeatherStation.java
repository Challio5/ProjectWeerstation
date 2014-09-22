package nl.eti1vb5.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Data;

/**
 * Model volgens het MVC-ontwerppatroon
 * Bevat de laatste data verkregen uit de database
 * @author Rob Bonhof
 * @date 18 jun. 2014
 */

public class WeatherStation {
	// Lijst met de temperatuur metingen van de BMP sensor
	private ObservableList<Data<Number, Number>> temperatuurBMP;
	
	// Lijst met de temperatuur metingen van de DHT sensor
	private ObservableList<Data<Number, Number>> temperatuurDHT;
	
	// Lijst met de luchtdruk metingen van de BMP sensor
	private ObservableList<Data<Number, Number>> luchtdruk;
	
	// Lijst met de luchtvochtigheid metingen van de DHT sensor
	private ObservableList<Data<Number, Number>> luchtvochtigheid;
	
	// Lijst met de dauwpunt metingen van de DHT sensor
	private ObservableList<Data<Number, Number>> dauwpunt;
	
	// Lijst met de licht metingen van de LDR
	private ObservableList<Data<Number, Number>> licht;
	
	/**
	 * Constructor voor het initialiseren van de datalijsten
	 */
	public WeatherStation() {
		temperatuurBMP = FXCollections.observableArrayList();
		temperatuurDHT = FXCollections.observableArrayList();
		luchtdruk = FXCollections.observableArrayList();
		luchtvochtigheid = FXCollections.observableArrayList();
		dauwpunt = FXCollections.observableArrayList();
		licht = FXCollections.observableArrayList();
	}
	
	/**
	 * Getter voor het opvragen van de lijst met de temperatuurmetingen van de BMP180 sensor
	 * @return De ArrayList met de laatste temperatuurmetingen
	 */
	public ObservableList<Data<Number, Number>> getTemperatuurBMP() {
		return temperatuurBMP;
	}

	/**
	 * Getter voor het opvragen van de lijst met de temperatuurmetingen van de DHT11 sensor
	 * @return De ArrayList met de laatste temperatuurmetingen
	 */
	public ObservableList<Data<Number, Number>> getTemperatuurDHT() {
		return temperatuurDHT;
	}

	/**
	 * Getter voor het opvragen van de lijst met de luchtdrukmetingen van de BMP180 sensor
	 * @return De ArrayList met de laatste luchtdrukmetingen
	 */
	public ObservableList<Data<Number, Number>> getLuchtdruk() {
		return luchtdruk;
	}

	/**
	 * Getter voor het opvragen van de lijst met de luchtvochtigheidsmetingen van de DHT11 sensor
	 * @return De ArrayList met de laatste luchtvochtigheidsmetingen
	 */
	public ObservableList<Data<Number, Number>> getLuchtvochtigheid() {
		return luchtvochtigheid;
	}

	/**
	 * Getter voor het opvragen van de lijst met de dauwpuntmetingen van de DHT11 sensor
	 * @return De ArrayList met de laatste dauwpuntmetingen
	 */
	public ObservableList<Data<Number, Number>> getDauwpunt() {
		return dauwpunt;
	}

	/**
	 * Getter voor het opvragen van de lijst met de lichtmetingen van de LDR sensor
	 * @return De ArrayList met de laatste lichtmetingen
	 */
	public ObservableList<Data<Number, Number>> getLicht() {
		return licht;
	}
	
	/**
	 * Setter voor het vervangen van de temperatuurlijst van de BMP180 sensor met een nieuwe lijst
	 * @param temperatuurBMP De nieuwe arraylist met data
	 */
	public void setTemperatuurBMP(ArrayList<Data<Number, Number>> temperatuurBMP) {
		this.temperatuurBMP.clear();
		this.temperatuurBMP.addAll(temperatuurBMP);
		//this.temperatuurBMP = FXCollections.observableArrayList(temperatuurBMP);
	}

	/**
	 * Setter voor het vervangen van de temperatuurlijst van de DHT11 sensor met een nieuwe lijst
	 * @param temperatuurDHT De nieuwe arraylist met data
	 */
	public void setTemperatuurDHT(ArrayList<Data<Number, Number>> temperatuurDHT) {
		this.temperatuurDHT.clear();
		this.temperatuurDHT.addAll(temperatuurDHT);
		
		//this.temperatuurDHT = FXCollections.observableArrayList(temperatuurDHT);
	}

	/**
	 * Setter voor het vervangen van de luchtdruklijst van de BMP180 sensor met een nieuwe lijst
	 * @param luchtdruk De nieuwe arraylist met data
	 */
	public void setLuchtdruk(ArrayList<Data<Number, Number>> luchtdruk) {
		this.luchtdruk.clear();
		this.luchtdruk.addAll(luchtdruk);
		
		//this.luchtdruk = FXCollections.observableArrayList(luchtdruk);
	}

	/**
	 * Setter voor het vervangen van de luchtvochtigheidslijst van de DHT11 sensor met een nieuwe lijst
	 * @param luchtvochtigheid De nieuwe arraylist met data
	 */
	public void setLuchtvochtigheid(ArrayList<Data<Number, Number>> luchtvochtigheid) {
		this.luchtvochtigheid.clear();
		this.luchtvochtigheid.addAll(luchtvochtigheid);
		
		//this.luchtvochtigheid = FXCollections.observableArrayList(luchtvochtigheid);
	}

	/**
	 * Setter voor het vervangen van de dauwpuntlijst van de DHT11 sensor met een nieuwe lijst
	 * @param dauwpunt De nieuwe arraylist met data
	 */
	public void setDauwpunt(ArrayList<Data<Number, Number>> dauwpunt) {
		this.dauwpunt.clear();
		this.dauwpunt.addAll(dauwpunt);
		
		//this.dauwpunt = FXCollections.observableArrayList(dauwpunt);
	}

	/**
	 * Setter voor het vervangen van de lichtlijst van de LDR sensor met een nieuwe lijst
	 * @param licht De nieuwe arraylist met data
	 */
	public void setLicht(ArrayList<Data<Number, Number>> licht) {
		this.licht.clear();
		this.licht.addAll(licht);
		
		//this.licht = FXCollections.observableArrayList(licht);
	}
	
	/**
	 * Methode om alle lijsten met data mee te legen
	 */
	public void clear() {
		temperatuurBMP.clear();
		temperatuurDHT.clear();
		luchtdruk.clear();
		luchtvochtigheid.clear();
		dauwpunt.clear();
		licht.clear();
	}
}