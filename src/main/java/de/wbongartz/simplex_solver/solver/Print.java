package de.wbongartz.simplex_solver.solver;

import de.wbongartz.simplex_solver.process.ExitProcess;
import de.wbongartz.simplex_solver.process.ProcessError;

/**
 * Ausgeben eines Textes.
 * 
 * @author Wolfgang Bongartz
 */
public class Print extends SolverAction {
	
	private String _msg;
	
	public Print(String msg) {
		_msg = msg;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform()
	 */
	@Override
	public void perform() throws ProcessError, ExitProcess {
		_result.add(_msg);
	}

}
