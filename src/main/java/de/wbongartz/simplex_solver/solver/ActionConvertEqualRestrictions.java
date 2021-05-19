package solver;

import java.util.HashSet;

import lp_problem.LPProblem;
import lp_problem.Operator;
import lp_problem.Restriction;
import process.*;

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
