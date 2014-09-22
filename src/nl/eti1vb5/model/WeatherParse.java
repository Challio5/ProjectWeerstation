package nl.eti1vb5.model;

/**
 * Model volgens het MVC-ontwerppatroon 
 * Klasse om de weerdata verkregen via externe websites op te slaan
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.0
 */

public class WeatherParse {
	// Weerdata
	private String dateTimeStr;
	private String city;
	private String region;
	private String country;
	private String currentWeatherText;
	private String temperature;
	private String htmlDescription;

	// Getters en setters
	public String getDateTimeStr() {
		return dateTimeStr;
	}

	public void setDateTimeStr(String dateTimeStr) {
		this.dateTimeStr = dateTimeStr;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrentWeatherText() {
		return currentWeatherText;
	}

	public void setCurrentWeatherText(String currentWeatherText) {
		this.currentWeatherText = currentWeatherText;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHtmlDescription() {
		return htmlDescription;
	}

	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}

}
