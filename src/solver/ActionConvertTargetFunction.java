package solver;

import lp_problem.*;
import process.*;

/**
 * Erzeugen der Standardgleichungsform: Minimierungs- in Maximierungsfunktion umwandeln.
 * 
 * @author Wolfgang Bongartz
 */
public class ActionConvertTargetFunction extends SolverAction {

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform(process.ProcessData)
	 */
	@Override
	public void perform() throws ProcessError {
		LPProblem lpProblem = (LPProblem) _environment.getValue("lpProblem");

		TargetFunction tf = lpProblem.getTargetFunction();
		if(tf.getType()==TargetFunctionType.MIN) {
			lpProblem.setTargetFunction(tf.invert());
			_result.add(lpProblem.getHtmlString());
		} else {
			_result.add("Die Zielfunktion ist bereits eine Maximierungsfunktion.");
		}
	}

}
