package de.wbongartz.simplex_solver.solver;

import de.wbongartz.simplex_solver.lp_problem.LPProblem;
import de.wbongartz.simplex_solver.process.*;
import de.wbongartz.simplex_solver.simplex_problem.SimplexProblem;

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
