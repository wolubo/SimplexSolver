package de.wbongartz.simplex_solver.lp_problem;

/**
 * Definiert alle zulässigen Zielfunktionsarten ('min' und 'max').
 * 
 * @author Wolfgang Bongartz
 */
public enum TargetFunctionType {
	MIN, MAX;

	@Override 
	public String toString() {
		switch(this) {
		case MIN: return "min";
		case MAX: return "max";
		}
		return "";
	}

	/**
	 * Liefert eine HTML-Darstellung des Objekts.
	 * @return
	 */
	public String getHtmlString() {
		return this.toString();
	}
}
