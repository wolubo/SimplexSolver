package de.wbongartz.simplex_solver.process;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt den Kern der Prozessverarbeitung dar. Sie muss überschrieben werden mit einer Klasse, welche die Methode setup() implementiert.
 * 
 * @author Wolfgang Bongartz
 */
public abstract class ProcessMaster {

	private ArrayList<ProcessStep> _process = new ArrayList<ProcessStep>();
	private ProcessState _processState = ProcessState.NOT_INITIALIZED;
	private int _nextStep=0; 
	private ProcessEnvironment _processEnvironment = new ProcessEnvironment();
	private ProcessStep _previousStep = null;

	private List<StateChangedListener> 	stateChangedListener = new ArrayList<StateChangedListener>();
	private List<StepExecutedListener> 	stepExecutedListener = new ArrayList<StepExecutedListener>();

	/**
	 * Fügt einen Prozessschritt ans Ende der Liste an.
	 * @param newStep
	 */
	protected void addStep(ProcessStep newStep) {
		_process.add(newStep);
	}

	/**
	 * Registriert einen Handler für das StateChanged-Event, das versendet wird, sobald sich der Status des Prozesses geändert hat.
	 * @param toAdd
	 */
	public final void addStateChangedListener(StateChangedListener toAdd) {
		stateChangedListener.add(toAdd);
	}

	/**
	 * Registriert einen Handler für das StepExecuted-Event, das nach der Abarbeitung eines jeden Prozessschrittes versendet wird.
	 * @param toAdd
	 */
	public final void addStepExecutedListener(StepExecutedListener toAdd) {
		stepExecutedListener.add(toAdd);
	}

	private void changeProcessState(ProcessState newState) {
		ProcessState prevState = _processState;
		_processState = newState;
		for (StateChangedListener l : stateChangedListener) {
			l.processStateChanged(prevState, newState);
		}
	}

	private void sendProcessStepExecuted(Object result) {
		ProcessStep currentStep = getCurrentStep();
		if(result==null) result = currentStep.getResult();
		for (StepExecutedListener l : stepExecutedListener) {
			l.processStepExecuted(_processState, result);
		}
	}

	/**
	 * Liefert den aktuellen Status des Prozesses zurück.
	 * @return
	 */
	public final ProcessState getProcessState() {
		return _processState;
	}

	/**
	 * Liefert eine Referenz auf den aktuellen Prozessschritt zurück.
	 * @return
	 */
	protected final ProcessStep getCurrentStep() {
		if(_nextStep>=0 && _nextStep<_process.size()) {
			return _process.get(_nextStep);
		}
		return null;
	}

	/**
	 * Initialisiert den ProcessMaster, sodass der Prozess erneut gestartet wird.
	 */
	protected final void init() {
		if(_process.size()==0) setup();
		if(_process.size()==0) throw new IllegalStateException("Nothing to execute (no steps defined)!");
		_processEnvironment.init();
		_nextStep = 0;
		this._previousStep=null;
		changeProcessState(ProcessState.INITIALIZED);
	}

	/**
	 * Definiert die Abfolge der Prozessschritte. Muss von einer abgeleiteten Klasse überschrieben werden!
	 */
	public abstract void setup();

	/**
	 * Durchläuft den vollständigen Prozess. Versendet die Events StateChanged und StepExecuted.
	 * @throws ProcessError
	 * @throws ExitProcess
	 */
	public final void run() throws ProcessError, ExitProcess {
		while(hasNextStep()) step();
	}
	
	public final boolean hasNextStep() {
		switch(_processState) {
		case ENDED:				return false;
		case ERROR:				return false;
		case NOT_INITIALIZED:	throw new IllegalStateException("Process has not been initialized!");
		default:				return _nextStep<_process.size();
		}
	}

	/**
	 * Arbeitet den nächsten Prozessschritt ab. Versendet die Events StateChanged und StepExecuted.
	 * @throws ProcessError
	 * @throws ExitProcess
	 */
	public final void step() throws ProcessError, ExitProcess  {
		ProcessStep step = getCurrentStep();
		if(step!=_previousStep && step!=null) { 
			step.setEnvironment(_processEnvironment);
			step.init();
		}
		_previousStep=step;
		
		switch(_processState) {
		case ENDED:				throw new IllegalStateException("Process has already ended!");
		case ERROR:				throw new IllegalStateException("Process ended due to an error!");
		case NOT_INITIALIZED:	throw new IllegalStateException("Process has not been initialized!");
		case INITIALIZED:		changeProcessState(ProcessState.RUNNING); break;
		case RUNNING:			break;
		default:				break;
		}
		
		if(step!=null) {
			try {
				step.perform();
				sendProcessStepExecuted(null);
				if( ! step.hasNextStep() ) {
					_nextStep++;
					if(_nextStep>=_process.size()) {
						changeProcessState(ProcessState.ENDED);
					}
				}
			} catch (ExitProcess e) {
				changeProcessState(ProcessState.ENDED);
				sendProcessStepExecuted(e.getResult());
			} catch (ProcessError e) {
				changeProcessState(ProcessState.ERROR);
				throw e;
			} catch (Exception e) {
				changeProcessState(ProcessState.ERROR);
				throw e;
			}
		} else {
			changeProcessState(ProcessState.ENDED);
		}
	}

}
