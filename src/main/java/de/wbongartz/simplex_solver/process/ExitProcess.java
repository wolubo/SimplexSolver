package process;

/**
 * Die Exception ExitProcess kann jederzeit während der Prozessabarbeitung geworfen werden, um den Prozess sofort zu beenden.
 * Wird vom ProcessMaster gefangen und führt zur sofortigen Beendigung des Prozesses.
 * 
 * @author Wolfgang Bongartz
 */
public class ExitProcess extends Exception {

	private static final long serialVersionUID = 1L;
	private Object _result = null;

	/**
	 * 
	 * @param result Das in result übergebene Objekt wird vom ProcessMaster im StepExecuted-Event als Resultat des letzten ausgeführen Prozessschrittes verwendet.
	 */
	public ExitProcess(Object result) {
		_result = result;
	}

	/**
	 * 
	 * @return Ergebnis (wie im Constructor übergeben)
	 */
	public Object getResult() {
		return _result;
	}

}
