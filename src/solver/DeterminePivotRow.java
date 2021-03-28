package solver;

import java.util.ArrayList;

import process.*;
import process.ProcessEnvironment;
import simplex_problem.SimplexProblem;

/**
 * Pivot-Zeile ermitteln.
 * 
 * @author Wolfgang Bongartz
 */
public class DeterminePivotRow implements ProcessStep {

	ArrayList<String> _result = new ArrayList<String>();
	private ProcessEnvironment _environment = null;
	private String _pivotRow = null;

	/* (non-Javadoc)
	 * @see process.statements.Condition#evaluate(process.ProcessEnvironment)
	 */
	@Override
	public void perform() throws ExitProcess {
		SimplexProblem sp = (SimplexProblem) _environment.getValue("simplexProblem");
		if(sp==null) throw new IllegalStateException();
		_pivotRow = sp.getPivotRow();
		if(_pivotRow!=null) {
			_environment.setValue("PivotRow", _pivotRow);
			_result.add("Pivot-Zeile: " + _pivotRow + " (strengste Wachstumsbegrenzung):");
			_result.add(sp.getHtmlString(false));
		} else {
			_result.add("Die Pivot-Zeile konnte nicht ermittelt werden. Das Modell ist unbeschränkt!");						
			throw new ExitProcess(_result);
		}
	}

	/* (non-Javadoc)
	 * @see process.statements.Condition#getResult()
	 */
	@Override
	public Object getResult() {
		return _result;
	}

	@Override
	public void setEnvironment(ProcessEnvironment environment) {
		_environment = environment;
	}

	@Override
	public boolean hasNextStep() {
		return false;
	}

	@Override
	public void init() {
		_result = new ArrayList<String>();
		_pivotRow = null;
	}

}
