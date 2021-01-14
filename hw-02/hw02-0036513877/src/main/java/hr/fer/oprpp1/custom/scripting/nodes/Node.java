package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.List;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;

/**
 * Razred predstavlja općeniti čvor sintaksnog stabla kojeg stvara i gradi sintaksni analizator {@link SmartScriptParser}.
 * 
 * @author Tomislav Bjelčić
 *
 */
public class Node {
	
	/**
	 * Djeca ovog čvora.
	 */
	private List children;
	
	/**
	 * Dodaje čvor {@code child} kao direktno dijete ovog čvora.
	 * 
	 * @param child čvor dijete ovog čvora.
	 * @throws NullPointerException ako je predani čvor {@code null}.
	 */
	public void addChildNode(Node child) {
		Objects.requireNonNull(child, "Predani čvor je null.");
		
		if (children == null)
			children = new ArrayIndexedCollection();
		
		children.add(child);
	}
	
	/**
	 * Vraća broj djece ovog čvora.
	 * 
	 * @return broj djece ovog čvora.
	 */
	public int numberOfChildren() {
		return children == null ? 0 : children.size();
	}
	
	/**
	 * Dohvaća čvor dijete na poziciji {@code index}.
	 * 
	 * @param index pozicija djeteta.
	 * @return dijete na poziciji {@code index}.
	 */
	public Node getChild(int index) {
		Objects.checkIndex(index, numberOfChildren());
		return (Node) children.get(index);
	}
	
	/**
	 * Vraća String reprezentaciju ovog čvora u obliku kojem je zapisan u izvornom programu.<br>
	 * U to su uključeni svi parametri ovog čvora i sva njegova djeca.
	 * 
	 * @return String reprezentacija ovog čvora u zapisu kao u izvornom programu.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		children.forEach(sb::append);
		return sb.toString();
	}
	
	/**
	 * Provjerava jednakost dvaju čvorova. Prilikom određivanja jednakosti provjeravaju se vrste čvorova, 
	 * brojevi djece te njihova podudarnost. Uz to se u obzir uzimaju i parametri čvorova, odnosno izrazi koje 
	 * čvorovi sadrže.
	 * 
	 * @return {@code true} ako se čvorovi u potpunosti podudaraju, inače {@code false}.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		Node nodeObj = (Node) obj;
		int childrenThis = this.numberOfChildren();
		int childrenObj = nodeObj.numberOfChildren();
		
		if (childrenThis != childrenObj)
			return false;
		
		for (int i=0; i<childrenThis; i++) {
			if (!this.getChild(i).equals(nodeObj.getChild(i)))
				return false;
		}
		return true;
	}
}
