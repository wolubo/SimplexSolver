package de.wbongartz.simplex_solver.process;

/**
 * Definiert die möglichen Zustände eines Prozesses.
 * 
 * @author Wolfgang Bongartz
 */
public enum ProcessState {
	/**
	 * Der Prozess wurde noch nicht initialisiert und kann daher noch nicht gestartet werden.
	 */
	NOT_INITIALIZED,
	
	/**
	 * Der Prozess ist initialisiert und kann starten.
	 */
	INITIALIZED,
	
	/**
	 * Der Prozess wird gerade abgearbeitet.
	 */
	RUNNING,
	
	/**
	 * Der Prozess wurde vollständig abgearbeitet.
	 */
	ENDED,
	
	/**
	 * Der Prozess wurde aufgrund eines Fehlers abgebrochen.
	 */
	ERROR
}
