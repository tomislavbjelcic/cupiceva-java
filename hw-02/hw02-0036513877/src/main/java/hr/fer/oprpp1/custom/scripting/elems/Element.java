package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Razred predstavlja općeniti izraz jezika SmartScript, koji onda može biti varijabla, konstanta, funkcija, operator ili znakovni niz.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Element {
	
	/**
	 * Vraća tekstualni zapis ovog izraza.
	 * 
	 * @return tekstualni zapis ovog izraza.
	 * @implSpec ova implementacija vraća prazan zapis.
	 */
	public String asText() {
		return "";
	}
	
	/**
	 * Vraća String reprezentaciju ovog izraza. Takva String reprezentacija će biti oblika kako je zapisana u izvornom 
	 * SmartScript programu.
	 * 
	 * @return String reprezentacija ovog izraza zapisana na isti način kao u SmartScript programu.
	 */
	@Override
	public String toString() {
		return asText();
	}
	
	/**
	 * Provjerava je li ovaj izraz jednak predanom izrazu {@code obj}. <br>
	 * Metoda provjerava vrste izraza i njihove tekstualne zapise. Ako se u nečemu ne podudaraju izrazi, metoda vraća 
	 * {@code false}.
	 * 
	 * @param obj izraz s kojim se uspoređuje ovaj izraz za jednakost.
	 * @return {@true} ako se vrste izraza i njihovi tekstualni zapisi podudaraju, inače {@code false}.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		Element elemObj = (Element) obj;
		
		return this.asText().equals(elemObj.asText());
	}
	
}
