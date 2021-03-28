package solver;

import java.util.ArrayList;

import process.ProcessEnvironment;
import process.ProcessError;
import process.ProcessStep;
import simplex_problem.SimplexProblem;

/**
 * Pivot-Spalte ermitteln.
 * 
 * @author Wolfgang Bongartz
 */
public class DeterminePivotCol implements ProcessStep {
	
	private ProcessEnvironment _environment = null;
	private String _pivot = null;
	private ArrayList<String> _result = new ArrayList<String>();

	/* (non-Javadoc)
	 * @see process.ProcessStep#setEnvironment(process.ProcessEnvironment)
	 */
	@Override
	public void setEnvironment(ProcessEnvironment environment) {
		_environment = environment;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform()
	 */
	@Override
	public void perform() throws ProcessError {
		SimplexProblem sp = (SimplexProblem) _environment.getValue("simplexProblem");
		_pivot = sp.getPivotColumn();
		_environment.setValue("PivotColumn", _pivot);

		if(_pivot!=null) {
			_result.add("Pivot-Spalte: " + _pivot + " (größter positiver Koeffizient in der Zielfunktion):");			
			_result.add(sp.getHtmlString(false));
		} else {
			throw new IllegalStateException("FEHLER: Pivot-Spalte konnte nicht ermittelt werden!");
//			_result.add("FEHLER: Pivot-Spalte konnte nicht ermittelt werden!");						
		}
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#hasNextStep()
	 */
	@Override
	public boolean hasNextStep() {
		return false;
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#init()
	 */
	@Override
	public void init() {
		_pivot = null;
		_result = new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see process.ProcessStep#getResult()
	 */
	@Override
	public Object getResult() {
		return _result;
	}

}
