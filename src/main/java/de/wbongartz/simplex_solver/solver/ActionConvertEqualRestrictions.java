package de.wbongartz.simplex_solver.solver;

import java.util.HashSet;

import de.wbongartz.simplex_solver.lp_problem.LPProblem;
import de.wbongartz.simplex_solver.lp_problem.Operator;
import de.wbongartz.simplex_solver.lp_problem.Restriction;
import de.wbongartz.simplex_solver.process.*;

/**
 * Erzeugen der Standardgleichungsform: Restriktionen mit Gleichheits-Operator umwandeln.
 * 
 * @author Wolfgang Bongartz
 */
public class ActionConvertEqualRestrictions extends SolverAction {

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform(process.ProcessData)
	 */
	@Override
	public void perform() throws ProcessError {
		boolean changed = false;
		
		LPProblem lpProblem = (LPProblem) _environment.getValue("lpProblem");

		HashSet<Restriction> all = lpProblem.getAllRestrictions();
		lpProblem.removeAllRestriction();

		for(Restriction e: all) {
			if(e.getOperator()==Operator.EQUAL) {
				lpProblem.addRestriction(new Restriction(e.getLinearCombination(), Operator.LESS_OR_EQUAL, e.getRightHandSide()));
				lpProblem.addRestriction(new Restriction(e.getLinearCombination(), Operator.MORE_OR_EQUAL, e.getRightHandSide()));
				changed = true;
			} else {
				lpProblem.addRestriction(e);				
			}
		}
		if(changed) {
			_result.add(lpProblem.getHtmlString());
		} else {
			_result.add("Keine Gleichungen vorhanden");
		}
	}

}
