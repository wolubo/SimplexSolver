package de.wbongartz.simplex_solver.process;

/**
 * Schnittstelle für Klassen, die einen Prozessschritt implementieren.
 * 
 * @author Wolfgang Bongartz
 */
public interface ProcessStep {
	
	public void setEnvironment(ProcessEnvironment environment);
	
	/**
	 * Wird vom ProcessManager aufgerufen und beinhaltet den Code für die Abarbeitung eines
	 * Prozeßschrittes. Falls die ProcessStep-Implementierung mehrere Schritte umfasst wird
	 * perform() aufgerufen, solange hasNextStep() TRUE zurückliefert. Auf diese Weise können
	 * Schleifen und Subprozesse (ProcessBlock) auch schrittweise durchlaufen werden.
	 * @throws ProcessError Ein Fehler trat auf. Prozess muss abgebrochen werden.
	 * @throws ExitProcess Prozess soll abgebrochen werden.
	 */
	public void perform() throws ProcessError, ExitProcess;
	
	/**
	 * Wird vom ProcessManager aufgerufen, bevor perform() aufgerufen wird. Sollte TRUE liefern, 
	 * solange der Prozeßschritt noch nicht vollständig abgearbeitet ist. Sobald FALSE geliefert 
	 * wird fährt ProcessManager mit der Abarbeitung des nächsten Prozeßschritts fort. 
	 * @return TRUE, wenn noch mindestens ein weiterer Prozessschritt existiert. Andernfalls: FALSE.
	 */
	public boolean hasNextStep();
	
	/**
	 * Wird vom ProcessManager aufgerufen, nachdem der vorherige Prozeßschritt vollständig 
	 * abgearbeitet wurde und bevor perform() aufgerufen wird. Evtl. nötige Initialisierungen 
	 * sollten in init() vorgenommen werden.  
	 */
	public void init();
	
	/** 
	 * Liefert das (Zwischen-) Ergebnis des Prozeßschrittes. 
	 * @return (Zwischen-) Ergebnis
	 */
	public Object getResult();
}
