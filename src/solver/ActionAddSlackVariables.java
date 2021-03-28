package solver;

import java.util.HashSet;
import java.util.TreeSet;

import lp_problem.LPProblem;
import lp_problem.Restriction;
import process.*;

/**
 * Erzeugen der Standardgleichungsform: Hinzufügen der Schlupfvariablen.
 * 
 * @author Wolfgang Bongartz
 */
public class ActionAddSlackVariables extends SolverAction {

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform(process.ProcessData)
	 */
	@Override
	public void perform() throws ProcessError {
		
		String varNameBase = "x";
		String varName;
		int varIndex=1;

		LPProblem lpProblem = (LPProblem) _environment.getValue("lpProblem");

		HashSet<Restriction> restrictions = lpProblem.getAllRestrictions();
		
		// Variablennamen sammeln.
		TreeSet<String> varNames = lpProblem.collectVariables();

		// Schlupfvariablen hinzufügen
		for(Restriction r: restrictions) {
			do {
				varName = varNameBase + varIndex++;
			} while (varNames.contains(varName) && varIndex<100);
			
			if(varIndex>=100) throw new IllegalStateException("Namen für Schlupfvariablen können nicht ermittelt werden!");
			
			varNames.add(varName);
			r.setSlackVariable(varName);
		}
		
		_result.add(lpProblem.getHtmlString());
	}


}
