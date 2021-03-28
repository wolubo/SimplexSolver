package solver;

import java.util.ArrayList;

import process.ProcessEnvironment;
import process.ProcessError;
import process.ProcessStep;
import simplex_problem.SimplexProblem;

/**
 * Basistausch durchführen.
 * 
 * @author Wolfgang Bongartz
 */
public class ExchangeBase implements ProcessStep {

	private ProcessEnvironment _environment = null;
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
		SimplexProblem sp          = (SimplexProblem) _environment.getValue("simplexProblem");
		String         pivotColumn = (String) _environment.getValue("PivotColumn");
		String         pivotRow    = (String) _environment.getValue("PivotRow");
		
		sp.exchangeBasevar();
		
		_environment.removeValue("PivotColumn");
		_environment.removeValue("PivotRow");

		_result.add("Basistausch (" + pivotColumn + " hinein, " + pivotRow + " hinaus)");
		_result.add(sp.getHtmlString(true));
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
