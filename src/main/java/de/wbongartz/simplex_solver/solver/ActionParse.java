package solver;

import lp_problem.LPProblem;
import parser.ParseException;
import parser.Parser;
import process.*;

/**
 * Benutzereingabe in interne Darstellung überführen.
 * 
 * @author Wolfgang Bongartz
 */
public class ActionParse extends SolverAction {

	private Parser _parser = null;

	/* (non-Javadoc)
	 * @see process.ProcessStep#perform()
	 */
	@Override
	public void perform() throws ProcessError {
		_result.add("<h1>Ausgangsbasis</h1>");
		try {
			LPProblem lpProblem = _parser.lp_definition();
			_result.add(lpProblem.getHtmlString());
			_environment.setValue("lpProblem", lpProblem);
		} catch (ParseException e) {
			throw new ProcessError(e.getMessage(), e);
		}
	}

	public void setDefinition(String definition) {
		_parser = new Parser(definition);
	}

}
