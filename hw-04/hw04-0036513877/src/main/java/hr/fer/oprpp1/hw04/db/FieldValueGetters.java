package hr.fer.oprpp1.hw04.db;

import java.util.Map;

/**
 * Razred sadrži implementacije objekta koji dohvaćaju atribute iz specificiranog 
 * zapisa studenta. Sadržane su implementacije za dohvaćanje svih atributa osim 
 * za atribut konačne ocjene.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class FieldValueGetters {
	
	/**
	 * Objekt koji dohvaća JMBAG.
	 */
	public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
	/**
	 * Objekt koji dohvaća prezime.
	 */
	public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;
	/**
	 * Objekt koji dohvaća ime.
	 */
	public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;
	
	/**
	 * Preslikavanje (mapa)<br>
	 * ime atributa -> objekt koji ga može dohvatiti.
	 */
	public static final Map<String, IFieldValueGetter> FIELD_GETTER_MAP
				= Map.of("jmbag", JMBAG,
						"lastName", LAST_NAME,
						"firstName", FIRST_NAME);
	
	/**
	 * Onemogući stvaranje instanci ovog razreda jer nema smisla.
	 */
	private FieldValueGetters() {}
	
}
