package de.wbongartz.simplex_solver.process;

/**
 * Signalisiert einen Fehler, der innerhalb eines Prozessschrittes aufgetreten ist.
 * Wird vom ProcessMaster gefangen und beendet den Prozessablauf.
 * 
 * @author Wolfgang Bongartz
 */
public class ProcessError extends Exception {
	
	private Exception _originalException = null;
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message Text, der die Fehlerursache beschreibt.
	 * @param originalException Sollte der Fehler auf einer gefangenen Exception beruhen, so kann hier die originäre Exception referenziert werden.
	 */
	public ProcessError(String message, Exception originalException) {
		super(message);
		_originalException = originalException;
	}
	
	/**
	 * Liefert eine Referenz auf die originäre Exception (falls vorhanden).
	 * @return
	 */
	public Exception getOriginalException() {
		return _originalException;
	}
}
