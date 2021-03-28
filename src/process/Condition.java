package process;

/**
 * Schnittstelle einer Prüfung. 
 * Der ProcessMaster ruft zunächst init(), dann evaluate() und zuletzt getResult() auf.
 * 
 * @author Wolfgang Bongartz
 */
public interface Condition {
	/**
	 * Wird vom ProcessMaster aufgerufen, um das Ergebnis der Prüfung zu ermitteln.
	 * @param environment Beinhaltet die Umgebung des Prozesses.
	 * @return TRUE: Bedingung erfüllt.
	 * @throws ExitProcess Wenn die Prüfung zum sofortigen Beenden des Prozesses führen soll, so muss eine ExitProcess-Exception geworfen werden.
	 */
	public boolean evaluate(ProcessEnvironment environment) throws ExitProcess;

	/**
	 * Wird vom ProcessMaster nach evaluate() aufgerufen und ermöglicht es, ein Ergebnis zurückzugeben, dass im StepExecuted-Event übergeben wird.
	 * @return Ergebnis der Prüfung.
	 */
	public Object getResult();

	/**
	 * Wird vom ProcessMaster vor evaluate() aufgerufen und ermöglicht es, den Zustand des Objekts zu initialisieren.
	 */
	public void init();
}
