package nl.eti1vb5.control.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singletonklasse die verbinding beheert met de database
 * Bevat een eigen instantie van zichzelf die kan worden opgevraagd
 * 
 * @author Projectgroep ETI1vb5 2014
 * @date 10 jun. 2014
 */

public class DatabaseManager {

	// DatabaseManager
	private static DatabaseManager manager;
	private Connection connection;
	
	// Database properties
	private InputStream propertyStream;
	private Properties databaseProperties;

	/**
	 * Private constructor zodat er geen instanties van buitenaf kunnen worden gemaakt
	 * 
	 * @param model
	 */
	private DatabaseManager() {}

	/**
	 * Maakt verbinding met de databaseserver met behulp van de gegevens uit de propertyfile
	 * @throws IOException Fout in het lezen van de propertyfile 
	 * @throws ClassNotFoundException Fout in het registreren van de drivers
	 * @throws SQLException Fout in het maken van de verbinding
	 * @return De connectie met de database
	 */
	public Connection connect() throws IOException, ClassNotFoundException, SQLException {
		// Eigenschappen voor de database
		databaseProperties = new Properties();

		// Laad de juiste eigenschappen in uit de propertyfile
		propertyStream = this.getClass().getResourceAsStream(
				"/database.properties");
		databaseProperties.load(propertyStream);

		// Sluit de inputstream, de eigenschappen zijn ingeladen
		propertyStream.close();

		// Slaat de eigenschappen op in de bijbehorende strings
		String drivers = databaseProperties.getProperty("jdbc.drivers");
		String url = databaseProperties.getProperty("jdbc.url");
		String username = databaseProperties.getProperty("jdbc.username");
		String password = databaseProperties.getProperty("jdbc.password");

		// Registreert de driver
		Class.forName(drivers);

		return DriverManager.getConnection(url, username, password);
	}
	
	/**
	 * Sluit de verbinding met de database server
	 * Zet de instanties die de database verbinding beheren op null
	 * Zorgt ervoor dat ze bij een nieuwe verbinding opnieuw geinitialiseerd worden
	 */
	public void disconnect() {
		try {
			connection.close();
			manager = null;
			connection = null;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			System.out.println("Connectie mislukt");
		}
	}
	
	/**
	 * Getter voor het opvragen van de databasemananger
	 * @return De databasemananager met de connectie naar de database
	 */
	public static DatabaseManager getInstance() {
		if(manager == null) {
			manager = new DatabaseManager();
		}
		return manager;
	}

	/**
	 * Getter voor het opvragen van de connectie van de databasemanager
	 * @return De connectie met de database
	 */
	public Connection getConnection() {
		if(connection == null) {
			try {
				connection = connect();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
}
