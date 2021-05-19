package lp_problem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * Kapselt eine Linearkombination der Form 'x1 + 2*x2 + 5'. 
 * Eine Linearkombination besteht hier immer aus einer Menge von Variablen mit ihren Koeffizienten (VarComponent) und einer 
 * konstanten Komponente. Im Beispiel 2x1 + 3x2 - 3 sind 2x1 und 3x2 variable Komponenten und 3 ist der konstante Wert.
 * 
 * @author Wolfgang Bongartz
 */
public class LinearCombination {

	private HashMap<String,VarComponent> components = new HashMap<String,VarComponent>();
	private BigFraction constComponent = BigFraction.ZERO;

	/**
	 * Fügt eine weitere Komponente der Form '3*x4' hinzu.
	 * @param c
	 */
	public void addVarComponent(VarComponent c) {
		String id = c.getIdentifier();
		VarComponent newComponent=c;
		if(components.containsKey(id)) {
			VarComponent oldComponent = components.get(id);
			components.remove(id);
			newComponent = oldComponent.add(c);
		}
		components.put(id, newComponent);
	}

	/** 
	 * Fügt eine neue konstante Komponente hinzu bzw. addiert einen konstanten Wert zur schon vorhandenen konstanten Komponente.
	 * @param c
	 */
	public void addConstComponent(BigFraction c) {
		constComponent = constComponent.add(c);
	}

	/** 
	 * Fügt eine neue konstante Komponente hinzu bzw. addiert einen konstanten Wert zur schon vorhandenen konstanten Komponente.
	 * @param d konstante Komponente
	 */
	public void addConstComponent(double d) {
		this.addConstComponent(new BigFraction(d));
	}

	@Override
	public String toString() {
		String retVal = "";
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		boolean isFirst=true;

		for(VarComponent c: components.values()) {
			if(isFirst) {
				isFirst=false;
			} else {
				if( ! c.isNegative()) {
					retVal+=" + ";
				}
			}
			retVal+=c.toString();
		}
		if( ! constComponent.equals(BigFraction.ZERO) ) {
			if( constComponent.compareTo(BigFraction.ZERO) >= 0) {
				if(isFirst) {
					retVal+=nf.format(constComponent);
				} else {
					retVal+=" + " + nf.format(constComponent);					
				}
			} else {
				retVal+=" - " + nf.format(constComponent.negate());									
			}
		}
		return retVal;
	}

	@Override
	public int hashCode() {
		int hash = components.hashCode() + constComponent.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if(! (obj instanceof LinearCombination)) return false;
		LinearCombination other = (LinearCombination) obj;
		if(components.equals(other.components)) {
				return constComponent.equals(other.constComponent);
		}
		return false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Liefert die Anzahl der Variablen.
	 * @return
	 */
	public int getNumberOfVarComponents() {
		return components.size();
	}
	
	/**
	 * Liefert TRUE, falls die konstante Komponente ungleich Null ist.
	 * @return
	 */
	public boolean hasConstComponent() {
		return ! constComponent.equals(BigFraction.ZERO); 
	}

	/**
	 * Liefert die konstante Komponente.
	 * @return
	 */
	public BigFraction getConstComponent() {
		return constComponent;
	}

	/**
	 * Ändert die konstante Komponente.
	 * @param d
	 */
	public void setConstComponent(BigFraction d) {
		constComponent=d;
	}

	/**
	 * Füllt die übergebene Liste mit allen in der Linearkombination vorkommenden Variablennamen.
	 * @param varNames
	 */
	public void collectVariables(TreeSet<String> varNames) {
		for(String name: components.keySet()) {
			varNames.add(name);
		}
	}

	/**
	 * Negiert alle Komponenten der Linearkombination.
	 * @return Neue Instanz mit negierten Komponenten.
	 */
	public LinearCombination negate() {
		LinearCombination retVal = new LinearCombination();
		retVal.addConstComponent(constComponent.negate());
		for(VarComponent vc: components.values()) {
			retVal.addVarComponent(vc.negate());
		}
		return retVal;
	}

	/**
	 * Liefert eine Liste aller variablen Komponenten.
	 * @return
	 */
	public ArrayList<VarComponent> getVarComponents() {
		return new ArrayList<VarComponent>(components.values());
	}

	/**
	 * Liefert eine HTML-Darstellung des Objekts.
	 * @return
	 */
	public String getHtmlString() {
		String retVal = "";
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		boolean isFirst=true;

		for(VarComponent c: components.values()) {
			if(isFirst) {
				isFirst=false;
			} else {
				if( ! c.isNegative()) {
					retVal+=" + ";
				}
			}
			retVal+=c.getHtmlString();
		}
		if( ! constComponent.equals(BigFraction.ZERO) ) {
			if( constComponent.compareTo(BigFraction.ZERO) >= 0) {
				if(isFirst) {
					retVal+=nf.format(constComponent);
				} else {
					retVal+=" + " + nf.format(constComponent);					
				}
			} else {
				retVal+=" - " + nf.format(constComponent.negate());									
			}
		}
		return retVal;
	}

	/**
	 * Liefert den der übergebenen Variablen zugeordneten Koeffizienten zurück.
	 * Falls die Variable nicht vorkommt wird BigFraction.ZERO zurückgeliefert.
	 * @param var Bezeichner der Variablen, für die der Koffizient zurückgeliefert werden soll.
	 * @return
	 */
	public BigFraction getCoefficient(String var) {
		if(components.containsKey(var)) {
			return components.get(var).getValue();
		}
		return BigFraction.ZERO;
	}

}
