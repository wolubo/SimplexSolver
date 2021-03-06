package de.wbongartz.simplex_solver.solver;

import java.util.ArrayList;

import de.wbongartz.simplex_solver.process.ProcessEnvironment;
import de.wbongartz.simplex_solver.process.ProcessStep;

/**
 * Abstrakte Basisklasse aller Prozessschritte des Packages.
 * 
 * @author Wolfgang Bongartz
 */
public abstract class SolverAction implements ProcessStep {
	protected ProcessEnvironment _environment;
	protected ArrayList<String> _result = new ArrayList<String>();
	
	@Override
	public void setEnvironment(ProcessEnvironment environment)
	{
		_environment = environment;
	}
	
	@Override
	public boolean hasNextStep() {
		return false;
	}

	@Override
	public void init() {
		_result = new ArrayList<String>();
	}

	@Override
	public Object getResult() {
		return _result;
	}
}
