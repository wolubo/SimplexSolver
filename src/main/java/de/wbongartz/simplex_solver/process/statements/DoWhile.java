package process.statements;

import process.*;
import process.ProcessEnvironment;

/**
 * Kontrollstruktur: Do-While-Schleife (repeat-until)
 * Prozessschritt, der eine Anweisung solange ausführt, wie eine Bedingung erfüllt ist.
 * Die Anweisung wird mindestens einmal ausgeführt, auch wenn die Bedingung von vorneherein nicht erfüllt ist.
 *  
 * @author Wolfgang Bongartz
 */
public class DoWhile implements ProcessStep {

	@Override
	public void perform() throws ProcessError {
		throw new IllegalStateException("Not implemented yet!");
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasNextStep() {
		throw new IllegalStateException("Not implemented yet!");
		// TODO Auto-generated method stub
	}

	@Override
	public void init() {
		throw new IllegalStateException("Not implemented yet!");
		// TODO Auto-generated method stub
	}

	@Override
	public Object getResult() {
		throw new IllegalStateException("Not implemented yet!");
		// TODO Auto-generated method stub
	}

	@Override
	public void setEnvironment(ProcessEnvironment environment) {
		throw new IllegalStateException("Not implemented yet!");
		// TODO Auto-generated method stub
	}

}
