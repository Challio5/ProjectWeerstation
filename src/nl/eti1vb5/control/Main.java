package nl.eti1vb5.control;

import javafx.application.Application;

/**
 * Klasse met de mainmethode voor launchen van de applicatie
 * 
 * @author Rob Bonhof
 * @since 21 mei 2014
 */

public class Main {
	/**
	 * Main methode voor het starten van het programma
	 * Start de JavaFx applicatie
	 * @param args Eventuele argumenten meegegeven via de commandline
	 */
	public static void main(String[] args) {
		Application.launch(Controller.class, args);
	}
}
