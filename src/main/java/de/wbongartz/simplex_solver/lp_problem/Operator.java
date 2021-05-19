package de.wbongartz.simplex_solver.lp_problem;

/**
 * Definiert die in Zielfunktionen und Restriktionen verwendbaren Vergleichsoperatoren.
 * 
 * @author Wolfgang Bongartz
 */
public enum Operator {
  	EQUAL, LESS_OR_EQUAL, MORE_OR_EQUAL, LESS, MORE;
  	
	@Override
	public String toString() {
		switch(this) {
		case EQUAL: 		return " = ";
		case LESS_OR_EQUAL: return " <= ";
		case MORE_OR_EQUAL: return " >= ";
		case LESS: 			return " < ";
		case MORE: 			return " > ";
		}
		return "<UNKNOWN OPERATOR>";
	}

	/**
	 * Liefert eine HTML-Darstellung des Objekts.
	 * @return
	 */
	public String getHtmlString() {
		switch(this) {
		case EQUAL: 		return "=";
		case LESS_OR_EQUAL: return "&lt;=";
		case MORE_OR_EQUAL: return "&gt;=";
		case LESS: 			return "&lt;";
		case MORE: 			return "&gt;";
		}
		return "<UNKNOWN OPERATOR>";
	}
}
