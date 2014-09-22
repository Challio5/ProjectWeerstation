package nl.eti1vb5.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * View volgens het MVC-ontwerppatroon 
 * Klasse om het menu weer te geven in de userinterface
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.0
 */

public class MenuPane extends MenuBar {

	// File menu met bijbehorende items
	private Menu file;
	private MenuItem save;
	private MenuItem connect;
	private MenuItem exit;

	// Database menu met bijbehorende items
	private Menu database;
	private MenuItem test;
	private MenuItem print;
	private MenuItem delete;

	// Terminal menu met bijbehorende items
	private Menu terminal;
	private MenuItem clear;

	/**
	 * Constructor voor het initiliaseren van de verschillende menu's 
	 * Voegt deze toe aan de menubar in de juiste volgorde
	 */
	public MenuPane() {
		// Filemenu
		file = new Menu("File");

		save = new MenuItem("Save");
		file.getItems().add(save);

		connect = new MenuItem("Connect");
		file.getItems().add(connect);

		exit = new MenuItem("Exit");
		file.getItems().add(exit);

		// Databasemenu
		database = new Menu("Database");

		test = new MenuItem("Test");
		database.getItems().add(test);

		print = new MenuItem("Print");
		database.getItems().add(print);

		delete = new MenuItem("Delete");
		database.getItems().add(delete);

		// Terminalmmenu
		terminal = new Menu("Terminal");

		clear = new MenuItem("Clear");
		terminal.getItems().add(clear);

		// Voegt de menu's toe
		this.getMenus().add(file);
		this.getMenus().add(database);
		this.getMenus().add(terminal);
	}

	/**
	 * Methode om aan het menuitem connect een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addSaveSetOnAction(EventHandler<ActionEvent> event) {
		save.setOnAction(event);
	}
	
	/**
	 * Methode om aan het menuitem connect een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addConnectSetOnAction(EventHandler<ActionEvent> event) {
		connect.setOnAction(event);
	}

	/**
	 * Methode om aan het menuitem exit een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addExitSetOnAction(EventHandler<ActionEvent> event) {
		exit.setOnAction(event);
	}

	/**
	 * Methode om aan het menuitem test een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addTestSetOnAction(EventHandler<ActionEvent> event) {
		test.setOnAction(event);
	}

	/**
	 * Methode om aan het menuitem delete een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addDeleteSetOnAction(EventHandler<ActionEvent> event) {
		delete.setOnAction(event);
	}

	/**
	 * Methode om aan het menuitem print een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addPrintSetOnAction(EventHandler<ActionEvent> event) {
		print.setOnAction(event);
	}

	/**
	 * Methode om aan het menuitem clear een eventhandler te koppelen
	 * @param event De eventhandler met daarin de actie die uitgevoert moet worden
	 */
	public final void addClearSetOnAction(EventHandler<ActionEvent> value) {
		clear.setOnAction(value);
	}
}
