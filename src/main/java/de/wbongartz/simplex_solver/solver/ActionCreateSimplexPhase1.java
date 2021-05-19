package de.wbongartz.simplex_solver.solver;

import java.util.ArrayList;

import de.wbongartz.simplex_solver.process.ExitProcess;
import de.wbongartz.simplex_solver.process.ProcessEnvironment;
import de.wbongartz.simplex_solver.process.ProcessError;
import de.wbongartz.simplex_solver.process.ProcessStep;
import de.wbongartz.simplex_solver.simplex_problem.SimplexProblem;
import de.wbongartz.simplex_solver.simplex_problem.SimplexProblemPhase1;


/**
 * Hilfsproblem für Phase I erzeugen.
 * 
 * @author Wolfgang Bongartz
 */
public class ActionCreateSimplexPhase1 implements ProcessStep {

	private ProcessEnvironment _environment = null;
	private ArrayList<String> _result = new ArrayList<String>();

	/* (non-Javadoc)
	 * @see process.ProcessStep#setEnvironment(process.ProcessEnvironment)
	 */
	@Override
	public void setEnvironment(ProcessEnvironment environment) {
		_environment = environment;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform()
	 */
	@Override
	public void perform() throws ProcessError, ExitProcess {
		SimplexProblem sp = (SimplexProblem) _environment.getValue("simplexProblem");
		SimplexProblemPhase1 sp_phase1 = new SimplexProblemPhase1(sp);
		_result.add(sp_phase1.getHtmlString(true));
		_environment.setValue("simplexProblem", sp_phase1);
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#hasNextStep()
	 */
	@Override
	public boolean hasNextStep() {
		return false;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#init()
	 */
	@Override
	public void init() {
		_result = new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#getResult()
	 */
	@Override
	public Object getResult() {
		return _result;
	}

}
