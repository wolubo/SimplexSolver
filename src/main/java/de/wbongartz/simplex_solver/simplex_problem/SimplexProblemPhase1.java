package de.wbongartz.simplex_solver.simplex_problem;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * Bildet die Phase I des Simplex-Algorithmus ab. Stellt das Hilfsproblem auf und speichert die ursprüngliche Zielfunktion in der Matrix ab. 
 * Die ursprüngliche Zielfunktion wird in Zeile 1 der Matrix abgelegt und bei allen Operationen wie eine Restriktion behandelt.
 * 
 * @author Wolfgang Bongartz
 */
public class SimplexProblemPhase1 extends SimplexProblem {

	private String _originalTargetFunctionIdentifier;
	private Integer _originalTargetFunctionIndex;

	/**
	 * Erzeugt aus einem Simplex-Problem mit ungültiger Basislösung ein Hilfs-Problem für die 
	 * Durchführung der Phase I des Simplex-Algorithmus.
	 * @param simplexProblem
	 */
	public SimplexProblemPhase1(SimplexProblem simplexProblem) {
		super(simplexProblem._maxDimCol, simplexProblem._maxDimRow + 1);

		_varnameIndex = simplexProblem._varnameIndex;
		_baseVariables = simplexProblem._baseVariables;
		_nonBaseVariables = simplexProblem._nonBaseVariables;
		_originalTargetFunctionIndex = _maxDimRow - 1; // Alte Zielfunktion ans Ende anhängen.

		// Bezeichner für die neue Zielfunktion suchen
		_originalTargetFunctionIdentifier = simplexProblem._targetFunctionIdentifier;
		Integer i = 1;
		do {
			_targetFunctionIdentifier = _originalTargetFunctionIdentifier + i.toString();
			i++;
		} while (_varnameIndex.containsKey(_targetFunctionIdentifier));

		// Matrix übernehmen (ohne Zielfunktion)
		for(Integer row=1; row<simplexProblem._maxDimRow; row++) {
			for(Integer col=0; col<this._maxDimCol; col++) {
				this.setValue(col, row, simplexProblem.getValue(col, row));
			}
		}

		// Alte Zielfunktion ans Ende der Matrix anhängen
		for(Integer col=0; col<this._maxDimCol; col++) {
			this.setValue(col, _originalTargetFunctionIndex, simplexProblem.getValue(col, 0));
		}
		
		// Neue Zielfunktion bilden (durch Aufsummieren aller Zeilen mit negativer "rechter Seite")
		for(Integer row=1; row<this._maxDimRow-1; row++) {
			BigFraction rhs = getValue(0, row);
			if(rhs.compareTo(BigFraction.ZERO)<0) {
				for(Integer col=0; col<this._maxDimCol; col++) {
					if(row!=col) {
						BigFraction currentValue = this.getValue(col, 0);
						BigFraction newValue = currentValue.add(this.getValue(col, row));
						this.setValue(col, 0, newValue);
					}
				}
			}
		}
	}

	@Override
	public boolean isOptimal() {
		return getValue(0, 0).compareTo(BigFraction.ZERO) >= 0;
	}

	@Override
	protected String addFormatedHeader() {
		String retVal = "";
		retVal += addFormatedTargetFunction(_targetFunctionIdentifier, 0);
		retVal += addFormatedTargetFunction(_originalTargetFunctionIdentifier, _originalTargetFunctionIndex);
		return retVal;
	}

	@Override
	public String toString() {
		boolean first = true;
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);

		// Zielfunktion
		String retVal = "max " + _targetFunctionIdentifier + "\n";
		retVal += _targetFunctionIdentifier + " = ";
		retVal += nf.format(getValue(0, 0));
		for(String colName: _nonBaseVariables) {
			int c = _varnameIndex.get(colName);
			BigFraction v = getValue(c, 0);
			retVal += formatDouble(v.negate(), colName);
		}
		retVal += "\n";
		retVal += "----------------------------\n";

		// Ursprüngliche Zielfunktion
		retVal += _originalTargetFunctionIdentifier + " = ";
		retVal += nf.format(getValue(0, _originalTargetFunctionIndex));
		for(HashMap.Entry<String,Integer> e: _varnameIndex.entrySet()) {
			BigFraction v = getValue(e.getValue(), _originalTargetFunctionIndex);
			if( ! v.equals(BigFraction.ZERO)) {
				retVal += formatDouble(v.negate(), e.getKey());				
			}
		}
		retVal += "\n";

		// Restriktionen
		for(String rowName: _baseVariables) {
			int rowIndex = _varnameIndex.get(rowName);
			retVal += rowName + " = ";
			retVal += nf.format(getValue(0, rowIndex));
			for(String colName: _varnameIndex.keySet()) {
				int colIndex = _varnameIndex.get(colName);
				if(colIndex!=rowIndex) {
					BigFraction v = getValue(colIndex, rowIndex);
					retVal += formatDouble(v.negate(), colName);
				}
			}
			retVal += "\n";
		}

		retVal += "Basisvariablen: ";
		first = true;
		for(String s: _baseVariables) {
			if(first) 
				first = false;
			else
				retVal += ", ";
			retVal += s + "=" + nf.format(getValue(0, _varnameIndex.get(s)));
		}
		retVal += "\n";

		retVal += "Nichtbasisvariablen: ";
		first = true;
		for(String s: _nonBaseVariables) {
			if(first) 
				first = false;
			else
				retVal += ", ";
			retVal += s + "=" + nf.format(getValue(0, _varnameIndex.get(s)));
		}
		retVal += "\n";

		retVal += "Zielfunktionswert: " + _targetFunctionIdentifier + "=" + nf.format(getValue(0, 0));

		retVal += "\n";

		return retVal;
	}

	public String getOriginalTargetFunctionIdentifier() {
		return _originalTargetFunctionIdentifier;
	}

}
