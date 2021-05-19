package de.wbongartz.simplex_solver.process;

import java.util.ArrayList;

/**
 * Kapselt einen Block (also eine Liste) von Prozessschritten, die nacheinander ausgeführt werden sollen.
 * 
 * @author Wolfgang Bongartz
 */
public class ProcessBlock implements ProcessStep {

	private ArrayList<ProcessStep> _subProcess = new ArrayList<ProcessStep>();
	private int _nextStep=0; 
	private ProcessStep _previousStep = null;
	private ProcessEnvironment _environment = null;

	/**
	 * Fügt einen Prozessschritt ans Ende des Blocks an.
	 * @param newStep
	 */
	public void addStep(ProcessStep newStep) {
		_subProcess.add(newStep);
	}

	/**
	 * Liefert den Prozessschritt, der als nächstes zur Ausführung kommt.
	 * @return
	 */
	public final ProcessStep getCurrentStep() {
		if(_nextStep>=0 && _nextStep<_subProcess.size()) {
			return _subProcess.get(_nextStep);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform(java.lang.Object)
	 */
	@Override
	public void perform() throws ProcessError, ExitProcess {
		ProcessStep step = getCurrentStep();
		if(step!=_previousStep && step!=null) {
			step.setEnvironment(_environment);
			step.init();
		}
		_previousStep=step;

		if(step!=null) {
			step.perform();
			if( ! step.hasNextStep() ) {
				_nextStep++;
			}
		}
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#hasNextStep()
	 */
	@Override
	public boolean hasNextStep() {
		return (_nextStep<_subProcess.size());
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#init()
	 */
	@Override
	public void init() {
		_nextStep = 0;
		_previousStep = null;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#getResult()
	 */
	@Override
	public Object getResult() {
//		ProcessStep current = getCurrentStep();
		if(_previousStep!=null) {
			return _previousStep.getResult();
		} 
		return null;
	}

	@Override
	public void setEnvironment(ProcessEnvironment environment) {
		_environment = environment;
	}

}
