package nl.eti1vb5.view;

import java.util.Calendar;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * View volgens het MVC-ontwerppatroon 
 * Klasse om de weersvoorspellingen weer te geven in de userinterface 
 * Maakt gebruik van een gridpane
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.0
 */

public class ForecastPane extends GridPane {
	// Datum
	private int uur;
	
	// Labels
	private Label uur1Label;
	private Label uur2Label;
	private Label uur3Label;
	private Label uur4Label;
	private Label uur5Label;

	// Weersymbolen
	private ImageView dag1;
	private ImageView dag2;
	private ImageView dag3;
	private ImageView dag4;
	private ImageView dag5;

	/**
	 * Constructor voor het initiliaseren van de verschillende nodes 
	 * Voegt de nodes toe aan de view in de juiste volgorde
	 */
	public ForecastPane() {
		// Breedte tussen de cellen
		this.setVgap(0);
		this.setHgap(20);

		// Datum
		uur = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		
		// Labels
		uur1Label = new Label((uur + 1) + ":00");
		uur2Label = new Label((uur + 2) + ":00");
		uur3Label = new Label((uur + 3) + ":00");
		uur4Label = new Label((uur + 4) + ":00");
		uur5Label = new Label((uur + 5) + ":00");

		// Weersymbolen
		dag1 = new ImageView(new Image(this.getClass().getResourceAsStream(
				"/cloudrain.png")));
		dag2 = new ImageView(new Image(this.getClass().getResourceAsStream(
				"/heavycloudrain.png")));
		dag3 = new ImageView(new Image(this.getClass().getResourceAsStream(
				"/heavycloudrain.png")));
		dag4 = new ImageView(new Image(this.getClass().getResourceAsStream(
				"/cloudrain.png")));
		dag5 = new ImageView(new Image(this.getClass().getResourceAsStream(
				"/sun.png")));

		// Voegt de labels toe aan de eerste rij
		this.add(uur1Label, 0, 0);
		this.add(uur2Label, 1, 0);
		this.add(uur3Label, 2, 0);
		this.add(uur4Label, 3, 0);
		this.add(uur5Label, 4, 0);

		// Voegt de weersymbolen toe aan de tweede rij
		this.add(dag1, 0, 1);
		this.add(dag2, 1, 1);
		this.add(dag3, 2, 1);
		this.add(dag4, 3, 1);
		this.add(dag5, 4, 1);
	}

}
