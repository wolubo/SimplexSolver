import java.awt.EventQueue;

import gui.MainWindow;


/**
 * Hauptprogramm. Enthält lediglich die main-Methode, welche das Hauptfenster öffnet und die Event-Verarbeitung startet.
 * 
 * @author Wolfgang Bongartz
 */
public class SimplexSolver {

	/**
	 * Öffne Hauptfenster und starte die Event-Verarbeitung. 
	 * @param args Kommandozeilen-Parameter
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
