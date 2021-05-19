package solver;

import process.ExitProcess;
import process.ProcessError;

/**
 * Ausgeben eines Textes.
 * 
 * @author Wolfgang Bongartz
 */
public class Print extends SolverAction {
	
	private String _msg;
	
	public Print(String msg) {
		_msg = msg;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform()
	 */
	@Override
	public void perform() throws ProcessError, ExitProcess {
		_result.add(_msg);
	}

}
