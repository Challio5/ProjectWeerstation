package nl.eti1vb5.control.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.scene.chart.XYChart.Data;
import nl.eti1vb5.control.database.DatabaseController;
import nl.eti1vb5.model.WeatherStation;
import nl.eti1vb5.view.TerminalPane;

/**
 * Klasse voor het beheren van de verbinding met het weerstation
 * Handelt de binnenkomende seriele data af
 * @author Projectgroep ETI1vb5 2014
 * @date 24 mei 2014
 */

public class SerialController {

	// Model met de data
	private WeatherStation model;
	
	// Database controller voor het wegschrijven naar en van de database
	private DatabaseController database;

	// Usb poorten op de verschillende platformen
	private static final String PORT_NAMES[] = { "/dev/tty.usbmodem411", // Mac
			"/dev/ttyUSB0", // Linux
			"COM6", // Windows
			"COM5" };

	// Beschikbare usb poort
	private CommPortIdentifier usbPort;

	// Usb poort als seriele poort
	private SerialPort serialPort;

	// Gegevens voor het opzetten van de verbinding
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	// Gebufferde input stream voor de te onvangen seriele data
	private BufferedReader input;

	/**
	 * Constructor 
	 * Zet de usb- en seriele verbinding op zodat er gecommunicieerd
	 * kan worden tussen de platformen Initialiseert de in- en outputsstreams
	 * voor het ontvangen en versturen van de seriele data
	 */
	public SerialController(WeatherStation model, DatabaseController database) {
		this.model = model;
		this.database = database;
	}

	/**
	 * Checkt of een van de gedefinieerde poorten beschikbaar is voor verbinding
	 * Vergelijkt deze poorten met de aanwezige poorten op het systeem
	 * 
	 * @return commPort de aangesloten usb poort op het systeem
	 */
	public CommPortIdentifier setupCommPort() {
		CommPortIdentifier commPort = null;
		Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();

		while (ports.hasMoreElements()) {
			CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
			for (String portName : PORT_NAMES) {
				if (port.getName().equals(portName)) {
					commPort = port;
					TerminalPane.println("Beschikbare usbpoort gevonden");
					break;
				}
			}
		}

		return commPort;
	}

	/**
	 * Methode om seriele data over de usb verbinding te kunnen sturen en
	 * ontvangen
	 * 
	 * @param commport
	 *            de usb verbinding voor de seriele data
	 * @return de serialport waar de data over gestuurd en ontvangen kan worden
	 */
	public SerialPort setupSerialPort(CommPortIdentifier commport) {
		SerialPort serialPort = null;

		if (commport == null) {
			TerminalPane.println("Geen beschikbare usbpoort gevonden");
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) commport.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// add event listeners
			serialPort.addEventListener(new SerialHandler());
			serialPort.notifyOnDataAvailable(true);

			TerminalPane.println("Seriele communicatie opgezet");
		} catch (Exception e) {
			TerminalPane.println("Geen seriele communicatie mogelijk");
		}
		return serialPort;
	}

	/**
	 * Methode om verbinding te maken met het weerstation
	 * Initialiseert de in- en output stream voor het ophalen en versturen van data
	 */
	public synchronized void connect() {
		usbPort = setupCommPort();
		serialPort = setupSerialPort(usbPort);

		try {
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			TerminalPane
					.println("Verbinding voltooid, klaar voor seriele communicatie");
		} catch (IOException e) {
			TerminalPane.println("Verbinding mislukt");
		} catch (NullPointerException e) {
			TerminalPane.println("Geen Arduino aangesloten");
		}
	}

	/**
	 * Methode om de seriele verbinding te verbreken Verwijdert deze klasse als
	 * listener van binnenkomende data en sluit de verbinding
	 */
	public synchronized void close() {
		if (serialPort != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Klasse handler om de binnenkomende seriele data af te handelen
	 * Verwerkt de binnenkomende commando's en schrijft de data weg in de database
	 */
	class SerialHandler implements SerialPortEventListener {
		@Override
		public synchronized void serialEvent(SerialPortEvent event) {
			Scanner scanner = null;
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					scanner = new Scanner(input);
					while (scanner.hasNext()) {
						String input = scanner.next();
						System.out.println(input);
						switch (input) {
						case "TemperatuurBMP":
							float temperatureBMP = Float.parseFloat(scanner.next());
							database.insertTemperatuurBMPIntoDatabase(temperatureBMP);
							ArrayList<Data<Number, Number>> results1 = database.selectTemperatureBMPData(50);
							
							final CountDownLatch latch = new CountDownLatch(1);
							Platform.runLater(new Runnable() {                          
		                        @Override
		                        public void run() {
		                            try{
		                            	model.setTemperatuurBMP(results1);
		                            }finally{
		                                latch.countDown();
		                            }
		                        }
		                    });
							
							break;

						case "TemperatuurDHT":
							float temperatureDHT = Float.parseFloat(scanner.next());
							database.insertTemperatuurDHTIntoDatabase(temperatureDHT);
							ArrayList<Data<Number, Number>> results2 = database.selectTemperatureDHTData(50);
							
							final CountDownLatch latch1 = new CountDownLatch(1);
							Platform.runLater(new Runnable() {                          
		                        @Override
		                        public void run() {
		                            try{
		                            	model.setTemperatuurDHT(results2);
		                            }finally{
		                                latch1.countDown();
		                            }
		                        }
		                    });
							break;

						case "Luchtdruk":
							float luchtdruk = Float.parseFloat(scanner.next());
							database.insertLuchtdrukIntoDatabase(luchtdruk);
							
							ArrayList<Data<Number, Number>> results3 = database.selectLuchtdrukData(50);
							
							final CountDownLatch latch2 = new CountDownLatch(1);
							Platform.runLater(new Runnable() {                          
		                        @Override
		                        public void run() {
		                            try{
		                            	model.setLuchtdruk(results3);
		                            }finally{
		                                latch2.countDown();
		                            }
		                        }
		                    });
							
							
							break;

						case "Luchtvochtigheid":
							float luchtvochtigheid = Float.parseFloat(scanner.next());
							database.insertLuchtvochtigheidIntoDatabase(luchtvochtigheid);
							ArrayList<Data<Number, Number>> results4 = database.selectLuchtvochtigheidData(50);
							model.setLuchtvochtigheid(results4);
							break;

						case "Dauwpunt":
							float dauwpunt = Float.parseFloat(scanner.next());
							database.insertDauwIntoDatabase(dauwpunt);
							ArrayList<Data<Number, Number>> results5 = database.selectDauwpuntData(50);
							model.setDauwpunt(results5);
							break;

						case "Licht":
							float licht = Float.parseFloat(scanner.next());
							database.insertLichtIntoDatabase(licht);
							ArrayList<Data<Number, Number>> results6 = database.selectLichtData(50);
							model.setLicht(results6);
							break;
						}
					}
				} catch (Exception e) {
					System.err.println(e.toString());
				} finally {
					scanner.close();
				}
			}
		}
	}
}
