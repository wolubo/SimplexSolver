package de.wbongartz.simplex_solver.process;

/**
 * Schnittstelle für Handler des Events 'StepExecuted';
 * 
 * @author Wolfgang Bongartz
 */
public interface StepExecutedListener {
	
	/**
	 * Ein Prozessschritt wurde abgearbeitet.
	 * @param currentState Status des Prozesses nach der Abarbeitung des Prozessschrittes.
	 * @param result Ergebnis des letzten abgearbeiteten Prozessschrittes (wurde mit getResult() ermittelt).
	 */
    void processStepExecuted(ProcessState currentState, Object result);
}
