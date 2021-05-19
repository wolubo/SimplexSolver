package de.wbongartz.simplex_solver.solver;

import java.util.ArrayList;

import de.wbongartz.simplex_solver.process.*;
import de.wbongartz.simplex_solver.simplex_problem.SimplexProblem;

/**
 * Prüfen, ob das Problem tatsächlich lösbar ist (ob also eine zulässige Startbasis existiert oder eine solche ermittelt werden kann).
 * 
 * @author Wolfgang Bongartz
 */
public class ConditionIsSolvable implements Condition {
	
	ArrayList<String> _result = new ArrayList<String>();
	private boolean _exitProcess = false;

	public ConditionIsSolvable(boolean exitProcess) {
		_exitProcess = exitProcess;
	}

	/* (non-Javadoc)
	 * @see process.statements.Condition#evaluate(process.ProcessEnvironment)
	 */
	@Override
	public boolean evaluate(ProcessEnvironment environment) throws ExitProcess {
		SimplexProblem sp = (SimplexProblem) environment.getValue("simplexProblem");
		if( ! sp.isValid() ) {
			if(_exitProcess) {
				_result.add("Die Lösung ist nicht zulässig! Das Problem ist nicht lösbar!");
				throw new ExitProcess(_result);
			}
			_result.add("Die Startbasis ist nicht zulässig! Phase I muss durchgeführt werden, um nach einer zulässigen Startbasis zu suchen.");
			return false;
		}
		
		_result.add("Die Startbasis ist zulässig!");
		
		return true;
	}

	/* (non-Javadoc)
	 * @see process.statements.Condition#getResult()
	 */
	@Override
	public Object getResult() {
		return _result;
	}

	@Override
	public void init() {
		_result = new ArrayList<String>();
	}

}
