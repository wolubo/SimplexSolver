package solver;

import java.util.ArrayList;

import process.Condition;
import process.ExitProcess;
import process.ProcessEnvironment;
import simplex_problem.SimplexProblem;

/**
 * Prüfen, ob die aktuelle Lösung bereits optimal ist.
 * 
 * @author Wolfgang Bongartz
 */
public class ConditionIsNotOptimal implements Condition {

	String _result = "ERROR: ConditionIsNotOptimal not evaluated!";
	
	/* (non-Javadoc)
	 * @see process.Condition#evaluate(process.ProcessEnvironment)
	 */
	@Override
	public boolean evaluate(ProcessEnvironment environment) throws ExitProcess {
		SimplexProblem sp = (SimplexProblem) environment.getValue("simplexProblem");
		if( sp.isOptimal() ) {
			if( sp.isDegenerated() ) {
				_result = "<p>Die aktuelle Lösung ist optimal, aber degeneriert!</p>";				
			} else {
				_result = "<p>Die aktuelle Lösung ist optimal!</p>";
			}
			return false;
		}

		_result = "<p>Die aktuelle Lösung ist nicht optimal! Eine (weitere) Iteration ist notwendig.</p>";
		
		return true;
	}

	/* (non-Javadoc)
	 * @see process.Condition#getResult()
	 */
	@Override
	public Object getResult() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(_result);
		return result;
	}

	@Override
	public void init() {
		_result = "ERROR: ConditionIsNotOptimal not evaluated!";
	}

}
