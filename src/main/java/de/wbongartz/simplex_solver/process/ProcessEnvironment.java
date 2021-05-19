package process;

import java.util.HashMap;

/**
 * Kapselt die Umgebung eines Prozesses. In ihr können Objekte abgelegt und über ihren Namen wieder abgerufen werden.
 * Auf diese Weise können Daten zwischen Prozessschritten ausgetauscht werden. 
 *
 * @author Wolfgang Bongartz
 */
public class ProcessEnvironment {
	
	private HashMap<String, Object> _data = new HashMap<String, Object>();
	
	/**
	 * Liefert ein in der Umgebung abgelegtes Objekt (oder NULL, falls der übergebene Name nicht bekannt ist).
	 * @param name Name, unter dem das Objekt bekannt ist.
	 * @return
	 */
	public Object getValue(String name) {
		if(_data.containsKey(name)) {
			return _data.get(name);
		} else {
			return null;
		}
	}
	
	/**
	 * Legt ein Objekt in der Umgebung ab.
	 * @param name Name, unter dem das Objekt bekannt sein soll.
	 * @param value Referenz auf das abzulegende Objekt.
	 */
	public void setValue(String name, Object value) {
		if(_data.containsKey(name)) {
			_data.replace(name, value);
		} else {
			_data.put(name, value);
		}
	}
	
	/**
	 * Entfernt ein Objekt aus der Umgebung.
	 * @param name Name, unter dem das Objekt bekannt ist.
	 */
	public void removeValue(String name) {
		if(_data.containsKey(name)) {
			_data.remove(name);
		}
	}

	/**
	 * Wird vom ProcessManager aufgerufen, bevor mit der Abarbeitung des Prozesses begonnen wird.
	 * Ermöglicht es, das Environment wieder in den Initialzustand zu versetzen und damit die Daten
	 * eines evtl. vorhergegangenen Prozessdurchlaufs zu löschen.
	 */
	public void init() {
		_data = new HashMap<String, Object>();
	}

}
