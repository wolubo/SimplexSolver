package process;

/**
 * Schnittstelle für Handler des Events 'StateChanged';
 * 
 * @author Wolfgang Bongartz
 */
public interface StateChangedListener {
	
	/**
	 * Ein Statuswechsel hat stattgefunden.
	 * @param prevState Status des Prozesses vor dem Statuswechsel.
	 * @param newState Status des Prozesses nach dem Statuswechsel.
	 */
    void processStateChanged(ProcessState prevState, ProcessState newState);
}
