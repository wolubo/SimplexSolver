package lp_problem;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * Kapselt ein Problem der linearen Programmierung, das aus einer Zielfunktion und einer Menge von Restriktionen besteht.
 * 
 * @author Wolfgang Bongartz
 */
public class LPProblem {
	
	private TargetFunction targetFunction=null;
	private LinkedHashSet<Restriction> restrictions=new LinkedHashSet<Restriction>();
	
	/**
	 * Erzeuge neue Instanz mit der übergebenen Zielfunktion (ohne Restriktionen).
	 * @param tf
	 */
	public LPProblem(TargetFunction tf) {
		if(tf==null) throw new IllegalArgumentException();
		setTargetFunction(tf);
	}

	/**
	 * Fügt eine Restriktion hinzu. Restriktionen der Form 'x1&gt;=0' werden dabei ignoriert.
	 * @param r
	 */
	public void addRestriction(Restriction r) {
		if( ! r.definesCodomain() ) restrictions.add(r);
	}

	/**
	 * Entfernt eine Restriktion.
	 * @param r Die zu entfernende Restriktion
	 */
	public void removeRestriction(Restriction r) {
		restrictions.remove(r);
	}

	/** 
	 * Entfernt alle Restriktionen.
	 */
	public void removeAllRestriction() {
		restrictions=new LinkedHashSet<Restriction>();
	}
	
	/**
	 * Liefert die Zielfunktion.
	 * @return
	 */
	public TargetFunction getTargetFunction() {
		return targetFunction;
	}
	
	/**
	 * Ändert die Zielfunktion.
	 * @param new_tf
	 */
	public void setTargetFunction(TargetFunction new_tf) {
		targetFunction = new_tf;
	}

	/**
	 * Liefert eine HTML-Darstellung des Objekts.
	 * @return
	 */
	public String getHtmlString() {
		String retVal;
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);

		retVal=targetFunction.getHtmlString() + '\n';
		
		retVal+="<p>subject to</p>\n";

		TreeSet<String> vars = collectVariables();

		// Tabelle aufbauen
		retVal += "<table>\n";
		boolean first, firstAndNegative;
		BigFraction coeff;
		for(Restriction r: restrictions) {
			first = true;
			firstAndNegative = false;
			retVal += "<tr>";
			
			LinearCombination lc = r.getLinearCombination();

			for(String var: vars) {
				
				String slackVar = r.getSlackVariable();
				if(slackVar!=null && var.compareTo(slackVar)==0) {
					coeff = BigFraction.ONE;
					var = slackVar;
				} else {
					coeff = lc.getCoefficient(var);
				}

				if(coeff.equals(BigFraction.ZERO)) {
					retVal += "<td></td>";
					retVal += "<td></td>";
				} else {
					if(first) {
						first = false;
						firstAndNegative = coeff.compareTo(BigFraction.ZERO)<0; 
					} else {
						if(coeff.compareTo(BigFraction.ZERO)>0) {
							retVal += "<td>+</td>";
						} else {
							retVal += "<td>-</td>";						
						}
					}

					retVal += "<td>";
					if(firstAndNegative) {
						retVal += "- ";
						firstAndNegative = false;
					}
					
					if( coeff.equals(BigFraction.ONE) ||coeff.equals(BigFraction.MINUS_ONE) ) {
						retVal += var;
					} else {
						if(coeff.compareTo(BigFraction.ZERO)>0) {
							retVal += nf.format(coeff) + "&sdot;" + var;				
						} else {
							retVal += nf.format(coeff.negate()) + "&sdot;" + var;				
						}
					}
					retVal += "</td>";
				}
			}
			
			if(lc.hasConstComponent()) {
				BigFraction cc = lc.getConstComponent();
				if(cc.compareTo(BigFraction.ZERO)>=0) {
					retVal += "<td>+</td>";					
					retVal += "<td>" + nf.format(cc) + "</td>";					
				} else {
					retVal += "<td>-</td>";										
					retVal += "<td>" + nf.format(cc.negate()) + "</td>";					
				}
			}
			
			retVal += "<td class=\"operator_col\">" + r.getOperator().getHtmlString() + "</td>";										
			
			BigFraction rhs = r.getRightHandSide();
			if(rhs.compareTo(BigFraction.ZERO)>=0) {
				retVal += "<td>" + nf.format(rhs) + "</td>";					
			} else {
				retVal += "<td> - " + nf.format(rhs.negate()) + "</td>";					
			}

			retVal += "</tr>\n";
		}
		
		retVal += "<tr>\n";
		first=true;
		for(String v: vars) {
			if(first) {
				first=false;
			} else {
				retVal += "<td>,</td>";
			}
			retVal += "<td>" + v + "</td>";
		}
		retVal += "<td class=\"operator_col\">&gt;=</td><td>0</td></tr>\n";
		
		retVal += "</table>\n";

		return retVal;
	}

	/**
	 * Liefert alle Restriktionen.
	 * @return
	 */
	public HashSet<Restriction> getAllRestrictions() {
		return new LinkedHashSet<Restriction>(restrictions);
	}

	/**
	 * Liefert alle Restriktionen eines bestimmten Typs.
	 * @param op Typ der zu liefernden Restriktionen.
	 * @return
	 */
	public HashSet<Restriction> getRestrictions(Operator op) {
		HashSet<Restriction> retVal = new HashSet<Restriction>();
		for(Restriction r: restrictions) {
			if(r.getOperator()==op) {
				retVal.add(r);
			}
		}		
		return retVal;
	}
	
	/**
	 * Liefert eine Liste aller verwendeten Optimierungs-Variablen.
	 * @return
	 */
	public TreeSet<String> collectVariables() {
		TreeSet<String> retVal = new TreeSet<String>();
		targetFunction.collectVariables(retVal);
		for(Restriction r: restrictions) {
			r.collectVariables(retVal);
		}
		return retVal;
	}

	@Override
	public String toString() {
		String retVal;
		retVal=targetFunction.toString() + '\n';
		
		retVal+="subject to\n";
		
		for(Restriction r: restrictions) {
			retVal += "    " + r.toString() + '\n';
		}
		
		TreeSet<String> vars = collectVariables();
		boolean first=true;
		for(String v: vars) {
			if(first) {
				first=false;
				retVal += "    " + v;
			} else {
				retVal += ", " + v;
			}
		}
		retVal += " >= 0\n";
		
		return retVal;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();		
	}

}
