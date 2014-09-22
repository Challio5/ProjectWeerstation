package nl.eti1vb5.control.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.chart.XYChart.Data;
import nl.eti1vb5.model.MeasuredValue;
import nl.eti1vb5.model.WeatherStation;
import nl.eti1vb5.view.TerminalPane;

/**
 * Controller klasse volgens het MVC-ontwerppatroon
 * Schrijft naar en haalt data op uit de database met behulp van de DatabaseManager
 * @author Rob Bonhof
 * @date 18 jun. 2014
 */

public class DatabaseController {
	
	// Model met de data
	private WeatherStation model;
	
	// Manager met de connectie naar de database 
	private DatabaseManager manager;
	
	/**
	 * Constructor voor het initialiseren van de attributen
	 * @param model Model voor het wegschrijven van de database data
	 */
	public DatabaseController(WeatherStation model) {
		this.model = model;
		manager = DatabaseManager.getInstance();
	}
	
	/**
	 * Methode om het model te initialiseren met de laatste data uit de datbase
	 * Haalt de laatste 24 metingen op voor het voorspellen van het weer
	 */
	public void initialize() {
		ArrayList<Data<Number, Number>> results1 = this.selectTemperatureBMPData(24);
		model.setTemperatuurBMP(results1);

		ArrayList<Data<Number, Number>> results2 = this.selectTemperatureDHTData(24);
		model.setTemperatuurDHT(results2);

		ArrayList<Data<Number, Number>> results3 = this.selectLuchtdrukData(24);
		model.setLuchtdruk(results3);

		ArrayList<Data<Number, Number>> results4 = this.selectLuchtvochtigheidData(24);
		model.setLuchtvochtigheid(results4);

		ArrayList<Data<Number, Number>> results5 = this.selectDauwpuntData(24);
		model.setDauwpunt(results5);

		ArrayList<Data<Number, Number>> results6 = this.selectLichtData(24);
		model.setLicht(results6);
	}
	
	/**
	 * Methode die alle data uit de database print op het terminalscherm
	 * Haalt de lijst met MeasuredValues op en print deze een voor een als format string
	 */
	public void print() {
		ArrayList<MeasuredValue> temperatureBMP = this.selectTemperatureBMP();
		for (MeasuredValue value : temperatureBMP) {
			String output = String.format(
					"%s\t\t%d\t\t%2.2f\t\t%s", 
					value.getType(),
					value.getId(),
					value.getMeting(), 
					value.getTijd());
			TerminalPane.println(output);
		}

		ArrayList<MeasuredValue> temperatureDHT = this.selectTemperatureDHT();
		for (MeasuredValue value : temperatureDHT) {
			String output = String.format(
					"%s\t\t%d\t\t%2.2f\t\t%s", 
					value.getType(),
					value.getId(),
					value.getMeting(), 
					value.getTijd());
			TerminalPane.println(output);
		}

		ArrayList<MeasuredValue> luchtvochtigheid = this.selectLuchtvochtigheid();
		for (MeasuredValue value : luchtvochtigheid) {
			String output = String.format(
					"%s\t\t%d\t\t%.2f\t\t%s", 
					value.getType(),
					value.getId(),
					value.getMeting(), 
					value.getTijd());
			TerminalPane.println(output);
		}

		ArrayList<MeasuredValue> luchtdruk = this.selectLuchtdruk();
		for (MeasuredValue value : luchtdruk) {
			String output = String.format("%s\t\t\t%d\t\t%.2f\t\t%s",
					value.getType(),
					value.getId(), 
					value.getMeting(), 
					value.getTijd());
			TerminalPane.println(output);
		}

		ArrayList<MeasuredValue> dauwpunt = this.selectDauwpunt();
		for (MeasuredValue value : dauwpunt) {
			String output = String.format(
					"%s\t\t\t\t%d\t\t%2.2f\t\t%s", 
					value.getType(),
					value.getId(),
					value.getMeting(), 
					value.getTijd());
			TerminalPane.println(output);
		}

		ArrayList<MeasuredValue> licht = this.selectLicht();
		for (MeasuredValue value : licht) {
			String output = String.format("%s\t\t\t\t%d\t\t%.2f\t\t%s",
					value.getType(),
					value.getId(), 
					value.getMeting(), 
					value.getTijd());
			TerminalPane.println(output);
		}
	}

	/**
	 * Methode om alle metingen uit de database te verwijderen
	 * Verwijdert alle data uit elke tabel met behulp van mysqlstatements
	 */
	public void delete() {
		try {
			Statement statement = manager.getConnection().createStatement();
			
			// Verwijdert alle dauwmetingen
			statement.executeUpdate("delete from dauwmeting");

			// Verwijdert alle lichtmetingen
			statement.executeUpdate("delete from lichtmeting");

			// Verwijdert alle tempertuutmetingen
			statement.executeUpdate("delete from temperatuurmetingbmp");
			statement.executeUpdate("delete from temperatuurmetingdht");

			// Verwijdert alle luchtvochtigheidsmetingen
			statement.executeUpdate("delete from luchtvochtigheidsmeting");

			// Verwijdert alle luchtdrukmetingen
			statement.executeUpdate("delete from luchtdrukmeting");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		model.clear();
	}
	
	/**
	 * Methode om met behulp van nepwaarden de database te testen
	 * Schrijft enkele waarden weg van elke tabel uit de database en haalt ze weer op
	 */
	public void test() {
		this.insertTemperatuurBMPIntoDatabase(26.33f);
		this.insertTemperatuurBMPIntoDatabase(28.86f);

		this.insertTemperatuurDHTIntoDatabase(25.49f);
		this.insertTemperatuurDHTIntoDatabase(28.21f);

		this.insertLuchtvochtigheidIntoDatabase(36.90f);
		this.insertLuchtvochtigheidIntoDatabase(38.56f);

		this.insertLuchtdrukIntoDatabase(1001.01f);
		this.insertLuchtdrukIntoDatabase(1000.63f);

		this.insertDauwIntoDatabase(10.12f);
		this.insertDauwIntoDatabase(13.29f);

		this.insertLichtIntoDatabase(200.98f);
		this.insertLichtIntoDatabase(220.55f);

		initialize();
	}
	
	/**
	 * Methode om een temperatuurmeting mee op te slaan in de database 
	 * De temperatuurmeting is afkomstig van de BMP180 sensor
	 * 
	 * @param meting de meting die moet worden weggeschreven in de database
	 */
	public void insertTemperatuurBMPIntoDatabase(float meting) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;

		// String met de insert statement
		String insertString = "insert into temperatuurmetingbmp " + "(meting) "
				+ "values " + "(?);";

		try {
			// Maakt het preparedstatement aan met het insert statement
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Meldt het argument van de methode aan
			insertStatement.setFloat(1, meting);

			// Voert het statement uit
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
	}

	// Insert Statements
	
	/**
	 * Methode om een temperatuurmeting mee op te slaan in de database 
	 * De temperatuurmeting is afkomstig van de DHT11 sensor
	 * 
	 * @param meting de meting die moet worden weggeschreven in de database
	 */
	public void insertTemperatuurDHTIntoDatabase(float meting) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;

		// String met de insert statement
		String insertString = "insert into temperatuurmetingdht " + "(meting) "
				+ "values " + "(?);";

		try {
			// Maakt het preparedstatement aan met het insert statement
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Meldt het argument van de methode aan
			insertStatement.setFloat(1, meting);

			// Voert het statement uit
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
	}

	/**
	 * Methode om een luchtdrukmeting mee op te slaan in de database
	 * De luchtdrukmeting is afkomstig van de BMP180 sensor
	 * 
	 * @param metingde meting die moet worden weggeschreven in de database
	 */
	public void insertLuchtdrukIntoDatabase(float meting) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;

		// String met de insert statement
		String insertString = "insert into luchtdrukmeting " + "(meting) "
				+ "values " + "(?);";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Meldt het argument van de methode aan
			insertStatement.setFloat(1, meting);

			// Voert het statement uit
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
	}

	/**
	 * Methode om een luchtvochtigheidsmeting mee op te slaan in de database
	 * De luchtvochtigheidmeting is afkomstig van DHT11
	 * 
	 * @param meting de meting die moet worden weggeschreven in de database
	 */
	public void insertLuchtvochtigheidIntoDatabase(float meting) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;

		// String met de insert statement
		String insertString = "insert into luchtvochtigheidsmeting "
				+ "(meting) " + "values " + "(?);";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Meldt het argument van de methode aan
			insertStatement.setFloat(1, meting);

			// Voert het statement uit
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
	}

	/**
	 * Methode om een temperatuurmeting mee op te slaan in de database
	 * De dauwmeting is afkomstig van de DHT11 sensor
	 * 
	 * @param meting de meting die moet worden weggeschreven in de database
	 */
	public void insertDauwIntoDatabase(float meting) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;

		// String met de insert statement
		String insertString = "insert into dauwmeting " + "(meting) "
				+ "values " + "(?);";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Meldt het argument van de methode aan
			insertStatement.setFloat(1, meting);

			// Voert het statement uit
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
	}

	/**
	 * Methode om een lichtmeting weer mee op te slaan in de database
	 * De lichtmeting is afkomstig van de LDR
	 * 
	 * @param meting de meting die moet worden weggeschreven in de database
	 */
	public void insertLichtIntoDatabase(float meting) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;

		// String met de insert statement
		String insertString = "insert into lichtmeting " + "(meting) "
				+ "values " + "(?);";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Meldt het argument van de methode aan
			insertStatement.setFloat(1, meting);

			// Voert het statement uit
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
	}
	
	/**
	 * Methode om temperatuurmetingen mee op te halen uit de database
	 * Temperatuurmetingen zijn afkomstig van de BMP180 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<Data<Number, Number>> selectTemperatureBMPData(int limit) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<Data<Number, Number>> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from temperatuurmetingBMP limit ?";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);
			insertStatement.setInt(1, limit);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			int counter = 0;
			while(set.next()) {
				float meting = set.getFloat("meting");
				results.add(new Data<Number, Number>(counter, meting));
				counter++;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om temperatuurmetingen mee op te halen uit de database
	 * Temperatuurmetingen zijn afkomstig van de DHT11 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<Data<Number, Number>> selectTemperatureDHTData(int limit) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<Data<Number, Number>> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from temperatuurmetingDHT limit ?";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);
			insertStatement.setInt(1, limit);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			int counter = 0;
			while(set.next()) {
				float meting = set.getFloat("meting");
				results.add(new Data<Number, Number>(counter, meting));
				counter++;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om luchtdrukmetingen mee op te halen uit de database
	 * De luchtdrukmetingen zijn afkomstig van de BMP180 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<Data<Number, Number>> selectLuchtdrukData(int limit) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<Data<Number, Number>> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from luchtdrukmeting limit ?";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);
			insertStatement.setInt(1, limit);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			int counter = 0;
			while(set.next()) {
				float meting = set.getFloat("meting");
				results.add(new Data<Number, Number>(counter, meting));
				counter++;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om luchtvochtigheidsmetingen mee op te halen uit de database
	 * De luchtvochtigheidsmetingen zijn afkomstig van de DHT11 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<Data<Number, Number>> selectLuchtvochtigheidData(int limit) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<Data<Number, Number>> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from luchtvochtigheidsmeting limit ?";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);
			insertStatement.setInt(1, limit);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			int counter = 0;
			while(set.next()) {
				float meting = set.getFloat("meting");
				results.add(new Data<Number, Number>(counter, meting));
				counter++;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om dauwpuntmetingen mee op te halen uit de database
	 * De dauwmetingen zijn afkomstig van de DHT11 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<Data<Number, Number>> selectDauwpuntData(int limit) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<Data<Number, Number>> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from dauwmeting limit ?";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);
			insertStatement.setInt(1, limit);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			int counter = 0;
			while(set.next()) {
				float meting = set.getFloat("meting");
				results.add(new Data<Number, Number>(counter, meting));
				counter++;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om lichtmetingen mee op te halen uit de database
	 * De lichtmetingen zijn afkomstig van een LDR
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<Data<Number, Number>> selectLichtData(int limit) {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<Data<Number, Number>> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from lichtmeting limit ?";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);
			insertStatement.setInt(1, limit);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			int counter = 0;
			while(set.next()) {
				float meting = set.getFloat("meting");
				results.add(new Data<Number, Number>(counter, meting));
				counter++;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	// Select statements zonder limit
	/**
	 * Methode om temperatuurmetingen mee op te halen uit de database
	 * De temperatuurmetingen zijn afkomstig van de BMP180 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<MeasuredValue> selectTemperatureBMP() {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<MeasuredValue> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from temperatuurmetingBMP";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			while(set.next()) {
				int id = set.getInt("id");
				float meting = set.getFloat("meting");
				Date tijd = set.getDate("tijd");
				results.add(new MeasuredValue("temperatuurmetingBMP", id, meting, tijd));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om temperatuurmetingen mee op te halen uit de database
	 * De temperatuurmetingen zijn afkomstig van de DHT11 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<MeasuredValue> selectTemperatureDHT() {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<MeasuredValue> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from temperatuurmetingDHT";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			while(set.next()) {
				int id = set.getInt("id");
				float meting = set.getFloat("meting");
				Date tijd = set.getDate("tijd");
				results.add(new MeasuredValue("temperatuurmetingDHT", id, meting, tijd));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om luchtdrukmetingen mee op te halen uit de database
	 * De luchtdrukmetingen zijn afkomstig van de BMP180 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<MeasuredValue> selectLuchtdruk() {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<MeasuredValue> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from luchtdrukmeting";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			while(set.next()) {
				int id = set.getInt("id");
				float meting = set.getFloat("meting");
				Date tijd = set.getDate("tijd");
				results.add(new MeasuredValue("luchtdrukmeting", id, meting, tijd));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om luchtvochtigheidsmetingen mee op te halen uit de database
	 * De luchtvochtigheidsmetingen zijn afkomstig van de DHT11 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<MeasuredValue> selectLuchtvochtigheid() {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<MeasuredValue> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from luchtvochtigheidsmeting";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			while(set.next()) {
				int id = set.getInt("id");
				float meting = set.getFloat("meting");
				Date tijd = set.getDate("tijd");
				results.add(new MeasuredValue("luchtvochtigheidsmeting", id, meting, tijd));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om dauwpuntmetingen mee op te halen uit de database
	 * De dauwpuntmeting is afkomstig van de DHT11 sensor
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<MeasuredValue> selectDauwpunt() {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<MeasuredValue> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from dauwmeting";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			while(set.next()) {
				int id = set.getInt("id");
				float meting = set.getFloat("meting");
				Date tijd = set.getDate("tijd");
				results.add(new MeasuredValue("dauwmeting", id, meting, tijd));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
	
	/**
	 * Methode om lichtmetingen mee op te halen uit de database
	 * De lichtmeting is afkomstig van een LDR
	 * 
	 * @return Arraylist met de verkregen waarden uit de database
	 */
	public ArrayList<MeasuredValue> selectLicht() {
		// Preparedstatement voor het executeren van de sqlstatement
		PreparedStatement insertStatement = null;
		
		// ArrayList voor het opslaan van de verkregen waarden
		ArrayList<MeasuredValue> results = new ArrayList<>();

		// String met de insert statement
		String insertString = "select * from lichtmeting";

		try {
			// Maakt het preparedstatement aan met het insert statement en voert
			// deze uit
			insertStatement = manager.getConnection().prepareStatement(insertString);

			// Voert het statement uit
			ResultSet set = insertStatement.executeQuery();
			
			// Zet de verkregen waardes in de arraylist
			while(set.next()) {
				int id = set.getInt("id");
				float meting = set.getFloat("meting");
				Date tijd = set.getDate("tijd");
				results.add(new MeasuredValue("lichtmeting", id, meting, tijd));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
		
		// Retourneert de arraylist met de verkregen waarden
		return results;
	}
}
