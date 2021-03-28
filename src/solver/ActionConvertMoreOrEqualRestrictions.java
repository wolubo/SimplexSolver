package solver;

import java.util.HashSet;

import lp_problem.LPProblem;
import lp_problem.Operator;
import lp_problem.Restriction;
import process.*;

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
