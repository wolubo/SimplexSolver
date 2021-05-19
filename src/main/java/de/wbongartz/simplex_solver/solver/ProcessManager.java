package de.wbongartz.simplex_solver.solver;

import de.wbongartz.simplex_solver.process.*;
import de.wbongartz.simplex_solver.process.statements.*;


/**
 * Definiert und verwalt den Prozessablauf.
 * 
 * @author Wolfgang Bongartz
 */
public class ProcessManager extends ProcessMaster {

	private ActionParse parse;
	
	public ProcessManager() {
		super();
		parse = new ActionParse();
	}

	@Override
	public void setup() {
		// Sub-Prozess für die Iteration des Simplex-Verfahrens
		ProcessBlock simplexIteration = new ProcessBlock();
		simplexIteration.addStep(new Print("<h1>Simplex-Iteration durchführen</h2>"));
		simplexIteration.addStep(new Print("<h2>Pivot-Spalte suchen</h2>"));
		simplexIteration.addStep(new DeterminePivotCol());
		simplexIteration.addStep(new Print("<h2>Pivot-Zeile suchen</h2>"));
		simplexIteration.addStep(new DeterminePivotRow());
		simplexIteration.addStep(new Print("<h2>Basistausch durchführen</h2>"));
		simplexIteration.addStep(new ExchangeBase());

		// Sub-Prozess für die Phase I des Simplex-Verfahrens
		ProcessBlock simplexPhase1 = new ProcessBlock();
		simplexPhase1.addStep(new Print("<h1>Phase I durchführen</h1>"));
		simplexPhase1.addStep(new Print("<h2>Überführen der Lösung in ein Hilfsproblem</h2>"));
		simplexPhase1.addStep(new ActionCreateSimplexPhase1());
		simplexPhase1.addStep(new While(new ConditionIsNotOptimal(), simplexIteration));
		simplexPhase1.addStep(new Print("<h2>Simplex Phase II vorbereiten: Überführen des Hilfsproblems in das neue Ausgangssystem</h2>"));
		simplexPhase1.addStep(new ActionCreateSimplexPhase2());
		
		
		// LP-Problem parsen
		addStep(parse);
		
		// LP-Problem in die Standardgleichungsform bringen	
		addStep(new Print("<h1>Umwandeln in die Standardgleichungsform</h1>"));
		addStep(new Print("<h2>Minimierungs- in Maximierungsfunktion umwandeln</h2>"));
		addStep(new ActionConvertTargetFunction());
		addStep(new Print("<h2>Gleichungen umwandeln</h2>"));
		addStep(new ActionConvertEqualRestrictions());
		addStep(new Print("<h2>Grösser-als-Restriktionen umwandeln</h2>"));
		addStep(new ActionConvertMoreOrEqualRestrictions());
		addStep(new Print("<h2>Schlupfvariablen einführen</h2>"));
		addStep(new ActionAddSlackVariables());

		// Initialisierung des Simplex-Verfahrens durchführen
		addStep(new Print("<h1>Initialisierung des Simplex-Verfahrens (all logical base)</h1>"));
		addStep(new ActionInitSimplex());
		
		// Prüfen, ob Phase I notwendig ist
		addStep(new Print("<h2>Zulässigkeit der Startbasis prüfen</h2>"));
		addStep(new IfThenElse(new ConditionIsSolvable(false), null, simplexPhase1));
		
		// Phase II durchführen
		addStep(new Print("<h1>Phase II durchführen</h1>"));
		addStep(new While(new ConditionIsNotOptimal(), simplexIteration));
		
	}

	/**
	 * Initialisiert den Prozess.
	 * @param definition Text-Darstellung des Optimierungsproblems
	 */
	public void initialize(String definition) {
		init();
		parse.setDefinition(definition);
	}

}
