package lp_problem;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * Kapselt eine variable Komponente, wie sie in Linearkombinationen verwendet wird.
 * Eine variable Komponente besteht immer aus einem Koeffizienten und einem Variablenbezeichner (bspw. '3*x1').
 * 
 * @author Wolfgang Bongartz
 */
public class VarComponent implements Comparable<VarComponent> {

	private String identifier;
	private BigFraction value;
	
	/**
	 * Erzeugt eine neue Instanz.
	 * @param v Koeffizient
	 * @param id Variablenbezeichner
	 */
	public VarComponent(BigFraction v, String id) {
		if(v==null) throw new IllegalArgumentException();
		if(id==null) throw new IllegalArgumentException();
		identifier=id;
		value=v;
	}

	/**
	 * Erzeugt eine neue Instanz.
	 * @param v Koeffizient
	 * @param id Variablenbezeichner
	 */
	public VarComponent(Double v, String id) {
		this(new BigFraction(v), id);
	}

	/**
	 * Prüft, ob der Koeffizient negativ ist.
	 * @return
	 */
	public boolean isNegative() {
		return value.compareTo(BigFraction.ZERO)<0;
	}

	/**
	 * Liefert den Variablenbezeichner.
	 * @return
	 */
	public String getIdentifier() {
		return identifier;
	}

	/** 
	 * Addiert zwei variable Komponenten, indem die Koeefizienten addiert werden.
	 * Voraussetzung: Identische Variablenbezeichner.
	 * @param c Zu addierende Komponente.
	 * @return Ergebnis der Addition (neue Instanz).
	 */
	public VarComponent add(VarComponent c) {
		if(c==null) throw new IllegalArgumentException();
		if(this.identifier.compareTo(c.identifier)!=0) throw new IllegalArgumentException();
		return new VarComponent(this.value.add(c.value), this.identifier);

	}

	/**
	 * Addiert zwei variable Komponenten, indem die Koeefizienten addiert werden.
	 * Voraussetzung: Identische Variablenbezeichner.
	 * @param v Multiplikator
	 * @return Ergebnis der Multiplikation (neue Instanz).
	 */
	public VarComponent multiply(double v) {
		return new VarComponent(this.value.multiply(new BigFraction(v)), this.identifier);
	}

	/**
	 * Negiert die variable Komponente.
	 * @return Ergebnis der Negation (neue Instanz).
	 */
	public VarComponent negate() {
		return new VarComponent(this.value.negate(), this.identifier);
	}

	/**
	 * Liefert den Wert des Koeffizienten.
	 * @return
	 */
	public BigFraction getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		int h = identifier.hashCode() + value.hashCode();
		return h;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if( ! (obj instanceof VarComponent) ) return false;
		VarComponent other = (VarComponent) obj;
		if(other.identifier.compareTo(this.identifier)!=0) return false;
		return this.value.compareTo(other.value)==0;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();		
	}

	@Override
	public int compareTo(VarComponent o) {
		int id = identifier.compareTo(o.identifier);
		if(id!=0) return id;
		return value.compareTo(o.value);
	}

	@Override
	public String toString() {
		String retVal;
		if(value.equals(BigFraction.ONE)) {
			retVal = identifier;
		} else if(value.equals(BigFraction.MINUS_ONE)) {
			retVal = "- " + identifier;
		} else {
			NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
			if(value.compareTo(BigFraction.ZERO)>0) {
				retVal = nf.format(value) + "∙" + identifier;				
			} else {
				retVal = " - " + nf.format(value.negate()) + "∙" + identifier;				
			}
		}
		return retVal;
	}

	/**
	 * Liefert eine HTML-Darstellung des Objekts.
	 * @return
	 */
	public String getHtmlString() {
		String retVal;
		if(value.equals(BigFraction.ONE)) {
			retVal = identifier;
		} else if(value.equals(BigFraction.MINUS_ONE)) {
			retVal = "- " + identifier;
		} else {
			NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
			if(value.compareTo(BigFraction.ZERO)>0) {
				retVal = nf.format(value) + "&sdot;" + identifier;				
			} else {
				retVal = " - " + nf.format(value.negate()) + "&sdot;" + identifier;				
			}
		}
		return retVal;
	}
}
