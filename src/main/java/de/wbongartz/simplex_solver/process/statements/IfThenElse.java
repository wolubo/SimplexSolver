package process.statements;

import process.*;
import process.ProcessEnvironment;

/**
 * Kontrollstruktur: If-Then-Else
 * Prozessschritt, der eine Anweisung ausführt, falls eine Bedingung erfüllt ist.
 * 
 * @author Wolfgang Bongartz
 */
public class IfThenElse implements ProcessStep {

	private Condition _condition = null;
	private ProcessStep _ifStep = null;
	private ProcessStep _elseStep = null;
	private ProcessEnvironment _environment = null;
	private State _loopState = State.CHECK;
	private Object _result = null;

	private enum State {
		CHECK,
		PERFORM_IF,
		PERFORM_ELSE,
		PERFORMING_IF,
		PERFORMING_ELSE,
		EXECUTED
	};

	/**
	 * Erzeugt einen Prozessschritt, der eine von zwei Anweisungen ausführt (je nachdem, ob eine Bedingung erfüllt ist oder nicht).
	 * @param condition Die zu prüfende Bedingung.
	 * @param ifStep Anweisung die ausgeführt werden soll, falls die Bedingung erfüllt ist (TRUE).
	 * @param elseStep Anweisung die ausgeführt werden soll, falls die Bedingung nicht erfüllt ist (FALSE).
	 */
	public IfThenElse(Condition condition, ProcessStep ifStep, ProcessStep elseStep) {
		if(condition==null) throw new IllegalArgumentException();
		_condition = condition;
		_ifStep = ifStep;
		_elseStep = elseStep;
	}

	@Override
	public void perform() throws ProcessError, ExitProcess {
		switch(_loopState) {
		case CHECK:
			if(_condition.evaluate(_environment)) {
				_loopState = State.PERFORM_IF;
			} else {
				_loopState = State.PERFORM_ELSE;				
			}
			_result = _condition.getResult();
			break;
		case EXECUTED:
			throw new IllegalStateException();
		case PERFORM_IF:
			_ifStep.init();
			_ifStep.setEnvironment(_environment);
			_ifStep.perform();
			_result = _ifStep.getResult();
			if( ! _ifStep.hasNextStep() ) 
				_loopState = State.EXECUTED;
			else
				_loopState = State.PERFORMING_IF;
			break;
		case PERFORMING_IF:
			_ifStep.perform();
			_result = _ifStep.getResult();
			if( ! _ifStep.hasNextStep() ) _loopState = State.EXECUTED;
			break;
		case PERFORM_ELSE:
			_elseStep.init();
			_elseStep.setEnvironment(_environment);
			_elseStep.perform();
			_result = _elseStep.getResult();
			if( ! _elseStep.hasNextStep() ) 
				_loopState = State.EXECUTED;
			else
				_loopState = State.PERFORMING_ELSE;
			break;
		case PERFORMING_ELSE:
			_elseStep.perform();
			_result = _elseStep.getResult();
			if( ! _elseStep.hasNextStep() ) _loopState = State.EXECUTED;
			break;
		}
	}

	@Override
	public boolean hasNextStep() {
		boolean retVal = false;
		switch(_loopState) {
		case CHECK:			retVal = true;
		break;
		case EXECUTED:		retVal = false;
		break;
		case PERFORM_IF:	retVal = (_ifStep != null);
		break;
		case PERFORM_ELSE:	retVal = (_elseStep != null);
		break;
		case PERFORMING_ELSE: retVal = true;
			break;
		case PERFORMING_IF:	retVal = true;
			break;
		}
		return retVal;
	}

	@Override
	public void init() {
		if(_ifStep!=null) _ifStep.init();
		if(_elseStep!=null) _elseStep.init();
		_condition.init();
		_loopState = State.CHECK;
	}

	@Override
	public Object getResult() {
		return _result;
	}

	@Override
	public void setEnvironment(ProcessEnvironment environment) {
		_environment = environment;
	}

}
