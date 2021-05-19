package solver;

import lp_problem.LPProblem;
import process.*;
import simplex_problem.SimplexProblem;

/**
 * Phase II initialisieren.
 * 
 * @author Wolfgang Bongartz
 */
public class ActionInitSimplex extends SolverAction {

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform(process.ProcessData)
	 */
	@Override
	public void perform() throws ProcessError {
		LPProblem lpProblem = (LPProblem) _environment.getValue("lpProblem");
		SimplexProblem sp = new SimplexProblem(lpProblem);
		_environment.removeValue("lpProblem");
		_environment.setValue("simplexProblem", sp);
		_result.add(sp.getHtmlString(true));
	}

}
