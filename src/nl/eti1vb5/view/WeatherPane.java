package nl.eti1vb5.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;

/**
 * View volgens het MVC-ontwerppatroon Klasse om de gemaakte weersvoorspellingen
 * weer te geven in de userinterface
 * 
 * @author Projectgroep ETI1vb5 2014
 * @version 1.0
 */

public class WeatherPane extends VBox {

	/**
	 * Constructor voor intialiseren van de nodes Voegt de nodes toe aan de view
	 * in de juiste volgorde
	 */
	public WeatherPane(ForecastPane voorspelling, GraphPane grafiek) {
		this.setPadding(new Insets(10));
		this.setAlignment(Pos.CENTER);

		ColumnConstraints constraint = new ColumnConstraints();
		constraint.setPercentWidth(50);
		voorspelling.getColumnConstraints().addAll(constraint, constraint,
				constraint, constraint, constraint);
		
		this.getChildren().add(voorspelling);
		this.getChildren().add(grafiek);
	}
}