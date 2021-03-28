package lp_problem;

import java.util.TreeSet;

/**
 * Kapselt eine Zielfunktion der Form 'max z = x1 + x2'.
 * Eine Zielfunktion besteht aus einer Linearkombination (Klasse LinearCombination). Sie ist entweder eine 
 * Minimierung- oder eine Maximierungsfunktion (Klasse TargetFunctionType) und sie beinhaltet einen Bezeichner 
 * für den Zielfunktionswert.
 * Im Beispiel max z = 2x1 + 3x2 - 3 ist 2x1 + 3x2 -3 die Linearkombination, max ist der Zielfunktionstyp und z 
 * ist der Bezeichner des Zielfunktionswerts.
 * 
 * @author Wolfgang Bongartz
 */
public class TargetFunction {
	
	private String identifier=null;
	private TargetFunctionType targetFunctionType;
	private LinearCombination linearCombination;
	
	/**
	 * Erzeugt eine neue Instanz.
	 * @param id Bezeichner der Zielfunktion (bspw. 'z')
	 * @param tft Art der Zielfunktion ('max' oder 'min')
	 * @param lc Linearkombination ("rechte Seite" der Zielfunktion)
	 */
	public TargetFunction(String id, TargetFunctionType tft, LinearCombination lc) {
		if(lc==null) throw new IllegalArgumentException();
		if(tft==null) throw new IllegalArgumentException();
		if(id==null) throw new IllegalArgumentException();
		linearCombination = lc;
		identifier=id;
		targetFunctionType=tft;
	}

	/**
	 * Liefert den Bezeichner der Zielfunktion.
	 * @return
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Liefert die Art der Zielfunktion ('max' oder 'min').
	 * @return
	 */
	public TargetFunctionType getType() {
		return targetFunctionType;
	}

	/**
	 * Füllt die übergebene Liste mit allen auf der rechten Seite der Zielfunktion vorkommenden 
	 * Variablennamen (also nicht den Bezeichner der Zielfunktion selbst).
	 * @param retVal
	 */
	public void collectVariables(TreeSet<String> retVal) {
		getLinearCombination().collectVariables(retVal);
	}

	/**
	 * Falls die Zielfunktion eine Minimierungsfunktion ist: In Maximierungsfunktion konvertieren.
	 * Falls die Zielfunktion eine Maximierungsfunktion ist: In Minimierungsfunktion konvertieren.
	 */
	public TargetFunction invert() {
		TargetFunction retVal=null;
		switch(targetFunctionType) {
		case MIN:
			retVal = new TargetFunction(this.identifier, TargetFunctionType.MAX, linearCombination.negate());
			break;
		case MAX:
			retVal = new TargetFunction(this.identifier, TargetFunctionType.MIN, linearCombination.negate());
			break;
		}
		return retVal;
	}

	/**
	 * Liefert die Linearkombination ("rechte Seite").
	 * @return
	 */
	public LinearCombination getLinearCombination() {
		return linearCombination;
	}
	
	@Override
	public String toString() {
		String retVal = targetFunctionType.toString() + " ";
		retVal+=identifier + " = " + getLinearCombination().toString();
		return retVal;
	}
	
	@Override
	public int hashCode() {
		return linearCombination.hashCode() + identifier.hashCode() + targetFunctionType.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if( ! (obj instanceof TargetFunction) ) return false;
		TargetFunction other = (TargetFunction) obj;
		return this.getLinearCombination().equals(other.getLinearCombination()) && this.identifier.equals(other.identifier) && this.targetFunctionType.equals(other.targetFunctionType);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();		
	}

	/**
	 * Liefert eine HTML-Darstellung des Objekts.
	 * @return
	 */
	public String getHtmlString() {
		String retVal = "<div>";
		retVal += targetFunctionType.getHtmlString() + " ";
		retVal += identifier + " = " + getLinearCombination().getHtmlString();
		retVal += "</div>";
		return retVal;
	}

}
