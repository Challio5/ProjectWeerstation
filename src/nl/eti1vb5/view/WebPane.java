package nl.eti1vb5.view;

import java.util.Arrays;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import nl.eti1vb5.model.WeatherParse;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * View volgens het MVC-ontwerppatroon 
 * Klasse om de externe weer websites weer te geven in de userinterface
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.0
 */

public class WebPane extends VBox {

	// Lijst met url's van externe weersites
	private String[] websiteList;
	private String[] urlList;

	// Websitekeuze
	private ComboBox<String> websites;

	// HTML script
	private String htmlScript;

	// Webengine en view
	private WebView webView;
	private WebEngine webEngine;

	//
	private String citycode;

	/**
	 * Constructor voor initialiseren van de verschillende nodes Voegt de nodes
	 * toe aan de view in de juiste volgorde
	 */
	public WebPane() {
		citycode = "729139";

		// Randen van 10cm om de pane
		this.setPadding(new Insets(10));

		// Lijst met url's van externe weersites
		websiteList = new String[] { "yahoo" };
		urlList = new String[] {
				"http://weather.yahooapis.com/forecastrss?w=" + citycode
						+ "&u=c",
				"api.openweathermap.org/data/2.5/weather?lat=52&lon=6&mode=xml" };

		// Websitekeuze
		websites = new ComboBox<String>(FXCollections.observableList(Arrays
				.asList(websiteList)));

		// Webengine en view
		webView = new WebView();
		webView.setPrefWidth(300);
		webEngine = new WebEngine();

		// HTML Script
		StringBuilder template = new StringBuilder();
		template.append("<head>\n");
		template.append("<style type=\"text/css\">body {background-color:#f4f4f4;}</style>\n");
		template.append("</head>\n");
		template.append("<body id='weather_background'>");

		htmlScript = template.toString();

		// URL lader
		websites.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(
							ObservableValue<? extends Number> observable,
							Number oldValue, Number newValue) {
						webEngine.load(urlList[newValue.intValue()]);
					}
				});

		// Rss converter
		webEngine.getLoadWorker().stateProperty()
				.addListener(new ChangeListener<State>() {
					@Override
					public void changed(
							ObservableValue<? extends State> observable,
							State oldValue, State newValue) {
						if (newValue != State.SUCCEEDED) {
							return;
						}
						WeatherParse weather = parse(webEngine.getDocument());
						StringBuilder locationText = new StringBuilder();
						locationText.append("<b>").append(weather.getCity())
								.append(", ").append(weather.getRegion())
								.append(" ").append(weather.getCountry())
								.append("</b><br />\n");
						String timeOfWeatherTextDiv = "<b id=\"timeOfWeatherText\">"
								+ weather.getDateTimeStr() + "</b><br />\n";
						String countdownText = "<b id=\"countdown\"></b><br />\n";
						webView.getEngine().loadContent(
								htmlScript + locationText.toString()
										+ timeOfWeatherTextDiv + countdownText
										+ weather.getHtmlDescription());
					}
				});

		// Selecteert de eerste optie van de combox
		websites.getSelectionModel().selectFirst();

		// Voegt de combobox en webView toe
		this.getChildren().add(websites);
		this.getChildren().add(webView);
	}

	/**
	 * Parsed de rssdata in een weatherobject
	 * Scant deze op de juiste tags en schrijft de data weg in een weatherparse object
	 * 
	 * @param doc Document wat geanalyseerd wordt door de methode voor de juiste gegevens
	 * @return weather Object met de verschillende weergegevens
	 */
	private static WeatherParse parse(Document doc) {
		NodeList currWeatherLocation = doc.getElementsByTagNameNS(
				"http://xml.weather.yahoo.com/ns/rss/1.0", "location");
		WeatherParse weather = new WeatherParse();
		weather.setCity(obtainAttribute(currWeatherLocation, "city"));
		weather.setRegion(obtainAttribute(currWeatherLocation, "region"));
		weather.setCountry(obtainAttribute(currWeatherLocation, "country"));
		NodeList currWeatherCondition = doc.getElementsByTagNameNS(
				"http://xml.weather.yahoo.com/ns/rss/1.0", "condition");
		weather.setDateTimeStr(obtainAttribute(currWeatherCondition, "date"));
		weather.setCurrentWeatherText(obtainAttribute(currWeatherCondition,
				"text"));
		weather.setTemperature(obtainAttribute(currWeatherCondition, "temp"));
		String forcast = doc.getElementsByTagName("description").item(1)
				.getTextContent();
		weather.setHtmlDescription(forcast);
		return weather;
	}

	/**
	 * Haalt de data op bij de juiste tag gevonden door de parse methode
	 * 
	 * @param nodeList De lijst met alle xml data
	 * @param attribute Naam van het onderdeel
	 * @return Waarde van de gefilterde data
	 */
	private static String obtainAttribute(NodeList nodeList, String attribute) {
		String attr = nodeList.item(0).getAttributes().getNamedItem(attribute)
				.getNodeValue().toString();
		return attr;
	}

}
