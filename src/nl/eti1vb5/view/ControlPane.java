package nl.eti1vb5.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;

/**
 * View volgens het MVC-ontwerppatroon 
 * Klasse voor het samenvoegen van de verschillende panes in een enkele pane 
 * Maakt gebruik van een borderpane
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.0
 */

public class ControlPane extends BorderPane {

	// Menu
	private MenuPane menu;

	/**
	 * Constructor voor het initiliaseren van de verschillende panes Voegt de
	 * panes toe aan de view op de juiste locatie
	 */
	public ControlPane(MenuPane menu, TerminalPane terminal, WebPane extWeather, WeatherPane center) {
		// Menu
		this.menu = menu;

		// Layout
		this.setTop(menu);
		this.setRight(extWeather);
		this.setBottom(terminal);
		this.setCenter(center);
	}
	
	/**
	 * Methode om aan het menuitem delete een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addSaveSetOnAction(EventHandler<ActionEvent> event) {
		menu.addSaveSetOnAction(event);
	}

	/**
	 * Methode om aan het menuitem delete een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addDeleteSetOnAction(EventHandler<ActionEvent> event) {
		menu.addDeleteSetOnAction(event);
	}

	/**
	 * Methode om aan het menuitem test een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addTestSetOnAction(EventHandler<ActionEvent> event) {
		menu.addTestSetOnAction(event);
	}

	/**
	 * Methode om aan het menuitem connect een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addConnectSetOnAction(EventHandler<ActionEvent> event) {
		menu.addConnectSetOnAction(event);
	}

	/**
	 * Methode om aan het menuitem clear een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addClearSetOnAction(EventHandler<ActionEvent> value) {
		menu.addClearSetOnAction(value);
	}

	/**
	 * Methode om aan het menuitem exit een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addExitSetOnAction(EventHandler<ActionEvent> event) {
		menu.addExitSetOnAction(event);
	}

	/**
	 * Methode om aan het menuitem print een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addPrintSetOnAction(EventHandler<ActionEvent> event) {
		menu.addPrintSetOnAction(event);
	}
}
