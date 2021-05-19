package de.wbongartz.simplex_solver.solver;

import java.util.HashSet;

import de.wbongartz.simplex_solver.lp_problem.LPProblem;
import de.wbongartz.simplex_solver.lp_problem.Operator;
import de.wbongartz.simplex_solver.lp_problem.Restriction;
import de.wbongartz.simplex_solver.process.*;

/**
 * Erzeugen der Standardgleichungsform: Restriktionen mit Größer-als-Operator umwandeln.
 * 
 * @author Wolfgang Bongartz
 */
public class ActionConvertMoreOrEqualRestrictions  extends SolverAction {

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform(process.ProcessData)
	 */
	@Override
	public void perform() throws ProcessError {
		LPProblem lpProblem = (LPProblem) _environment.getValue("lpProblem");
		boolean changed = false;
		
		HashSet<Restriction> all = lpProblem.getAllRestrictions();
		lpProblem.removeAllRestriction();

		for(Restriction e: all) {
			if(e.getOperator()==Operator.MORE || e.getOperator()==Operator.MORE_OR_EQUAL) {
				lpProblem.addRestriction(e.negate());
				changed = true;
			} else {
				lpProblem.addRestriction(e);				
			}
		}
		if(changed) {
			_result.add(lpProblem.getHtmlString());
		} else {
			_result.add("Keine Grösser-als-Restriktionen vorhanden.");
		}
	}

}
