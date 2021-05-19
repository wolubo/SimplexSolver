package de.wbongartz.simplex_solver.process.statements;

import de.wbongartz.simplex_solver.process.*;
import de.wbongartz.simplex_solver.process.ProcessEnvironment;

/**
 * Kontrollstruktur: While-Schleife
 * Prozessschritt, der eine Anweisung solange ausführt, wie eine Bedingung erfüllt ist.
 * Falls die Bedingung von vorneherein nicht erfüllt ist wird die Anweisung gar nicht ausgeführt.
 * 
 * @author Wolfgang Bongartz
 */
public class While implements ProcessStep {
	private Condition _condition = null;
	private ProcessStep _step = null;
	private ProcessEnvironment _environment = null;
	private State _loopState = State.CHECK;
	private Object _result = null;
	
	private enum State {
		CHECK,			// Lauf-Bedingung für ersten bzw. nächsten Durchlauf prüfen.
		LOOPING,		// Lauf-Bedingung war erfüllt. Loop-Body ausführen.
		NOT_EXECUTED,	// Ende: Lauf-Bedingung war von vorneherein nicht erfüllt. Loop-Body nie ausgeführt. 
		EXECUTED		// Ende: Loop-Body ausgeführt. Aber nun ist Lauf-Bedingung nicht mehr erfüllt.
	};
	/*
	 * Statusübergänge:
	 * CHECK   --> NOT_EXECUTED: Die Lauf-Bedingung war von Anfang an nicht erfüllt.
	 * CHECK   --> EXECUTED:	 Nach dem letzten Durchlauf ist die Lauf-Bedingung nicht mehr erfüllt.
	 * CHECK   --> LOOPING:		 Die Lauf-Bedingung ist erfüllt: Loop-Body wird ausgeführt.
	 * LOOPING --> CHECK:		 Loop-Body ist komplett ausgeführt. Lauf-Bedingung checken. 
	 */

	/**
	 * Erzeugt einen Prozessschritt, der eine Anweisung solange ausführt, wie eine Bedingung erfüllt ist.
	 * Falls die Bedingung von vorneherein nicht erfüllt ist wird die Anweisung gar nicht ausgeführt.
	 * @param condition Zu prüfende Bedingung.
	 * @param step Anweisung die solange wiederholt ausgeführt wird, wie die Bedingung erfüllt ist. 
	 */
	public While(Condition condition, ProcessStep step) {
		if(condition==null) throw new IllegalArgumentException();
		if(step==null) throw new IllegalArgumentException();
		_condition = condition;
		_step = step;
	}
	
	/* (non-Javadoc)
	 * @see process.ProcessStep#perform(java.lang.Object)
	 */
	@Override
	public void perform() throws ProcessError, ExitProcess {
		switch(_loopState) {
		case CHECK:
			if(_condition.evaluate(_environment)) {
				_step.init();
				_step.setEnvironment(_environment);
				_loopState = State.LOOPING;
			} else {
				if(_result==null) {
					_loopState = State.NOT_EXECUTED;
				} else {
					_loopState = State.EXECUTED;
				}
			}
			_result = _condition.getResult();
			break;
		case LOOPING:
			_step.perform();
			if( ! _step.hasNextStep() ) {
				_loopState = State.CHECK;
			}
			_result = _step.getResult();
			break;
		case EXECUTED:
			throw new IllegalStateException();
		case NOT_EXECUTED:
			throw new IllegalStateException();
		}
	}
	
	/* (non-Javadoc)
	 * @see process.ProcessStep#init(java.lang.Object)
	 */
	@Override
	public void init() {
		_step.init();
		_condition.init();
		_loopState = State.CHECK;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#hasNextStep()
	 */
	@Override
	public boolean hasNextStep() {
		boolean retVal = false;
		switch(_loopState) {
		case CHECK:			retVal = true;  break;
		case EXECUTED:		retVal = false; break;
		case LOOPING: 		retVal = true;  break;
		case NOT_EXECUTED:	retVal = false; break;
		}
		return retVal;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#getResult()
	 */
	@Override
	public Object getResult() {
		return _result;
	}

	@Override
	public void setEnvironment(ProcessEnvironment environment) {
		_environment = environment;
	}
}
