package nl.eti1vb5.view;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

/**
 * View volgens het MVC-ontwerppatroon 
 * Klasse om alle debugdata weer te geven in de userinterface 
 * Maakt gebruik van een titledpane
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.0
 */

public class TerminalPane extends TitledPane {

	private static TextArea terminalText;

	/**
	 * Constructor voor het initiliaseren van het tekstvak 
	 * Voegt het tekstvak toe als content van de view
	 */
	public TerminalPane() {
		this.setText("Terminal");
		this.setPadding(new Insets(10));
		this.setExpanded(false);

		terminalText = new TextArea();
		terminalText.setPromptText("Output");
		terminalText.setEditable(false);

		this.setContent(terminalText);
	}

	/**
	 * Methode om string mee in het terminalvenster te printen
	 * @param value De string wat op het terminalvenster geprint moet worden
	 */
	public static void print(String value) {
		terminalText.appendText(value);
	}

	/**
	 * Methode om string mee in het terminalvenster te printen gevolgd door een enter
	 * @param value De string wat op het terminalvenster geprint moet worden
	 */
	public static void println(String value) {
		terminalText.appendText("\n" + value);
	}

	/**
	 * Methode om geprinte data in het terminalvenster mee te wissen
	 */
	public static void clear() {
		terminalText.clear();
	}

}
