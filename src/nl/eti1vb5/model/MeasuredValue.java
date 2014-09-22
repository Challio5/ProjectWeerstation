package nl.eti1vb5.model;

import java.sql.Date;

/**
 * Klasse om de meting uit de database mee op te slaan
 * De attributen komen overeen met de kolommen uit de tabel
 * @author Projectgroep ETI1vb5 2014
 * @date 11 jun. 2014
 */

public class MeasuredValue {
	// Weerresultaten uit de database
	private String metingType;
	private int id;
	private float meting;
	private Date tijd;

	// Constructor
	public MeasuredValue(String metingType, int id, float meting, Date tijd) {
		this.metingType = metingType;
		this.id = id;
		this.meting = meting;
		this.tijd = tijd;
	}

	/**
	 * Getter voor het opvragen van het type van de meting
	 * @return Het tpye van de meting
	 */
	public String getType() {
		return metingType;
	}
	
	/**
	 * Getter voor het opvragen van de id van de meting
	 * @return Het nummer van de meting
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter voor het opvragen van de waarde van de meting
	 * @return De waarde van de meting
	 */
	public float getMeting() {
		return meting;
	}

	/**
	 * Getter voor het opvragen van de tijd van de meting
	 * @return De tijd waarop de meting gedaan is
	 */
	public Date getTijd() {
		return tijd;
	}
}
