package simplex_problem;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.math3.fraction.BigFraction;

import lp_problem.*;

/**
 * Die Klasse SimplexProblem beinhaltet alles, was für das durchlaufen der Phase I des Simplex-Algorithmus nötig ist. Dazu wird das 
 * Problem in Form einer Matrix dargestellt. Die Zielfunktion befindet sich in Zeile 0 dieser Matrix. Die Restriktionen folgen in 
 * den Zeilen 1 bis n. Jede Zeile bildet die betreffende Linearkombination ab. In der Spalte 0 befinden sich die jeweiligen 
 * konstanten Werte. In den Spalten 1 bis n folgen die Koeffizienten der Entscheidung- und Schlupfvariablen. Auf dieser Basis kann 
 * das Problem durch einfache Matrizenoperationen gelöst werden.
 * 
 * @author Wolfgang Bongartz
 */
public class SimplexProblem {

	protected int _maxDimCol, _maxDimRow;
	protected BigFraction[][] _matrix = null;
	protected HashMap<String, Integer> _varnameIndex = new HashMap<String, Integer>(); 
	protected TreeSet<String> _baseVariables = new TreeSet<String>(); 
	protected TreeSet<String> _nonBaseVariables = new TreeSet<String>(); 
	protected String _targetFunctionIdentifier = null;
	private String _pivotColumn = "";
	private String _pivotRow = "";

	/**
	 * Initialisiert ein "leeres" Objekt mit einer bestimmten Matrizzen-Größe. 
	 * @param dimCol Anzahl der Spalten
	 * @param dimRow Anzahl der Zeilen
	 */
	public SimplexProblem(int dimCol, int dimRow) {
		_maxDimCol = dimCol;
		_maxDimRow = dimRow;
		_matrix = new BigFraction[_maxDimCol][_maxDimRow];
		_varnameIndex = new HashMap<String, Integer>(); 
		_baseVariables = new TreeSet<String>(); 
		_nonBaseVariables = new TreeSet<String>(); 
		_targetFunctionIdentifier = null;
	}

	/**
	 * Erzeugt aus einem LP-Problem ein Simplex-Problem für die Durchführung der Phase II
	 * des Simplex-Algorithmus.
	 * @param lpProblem
	 */
	public SimplexProblem(LPProblem lpProblem) {
		int colIndex, rowIndex;

		TargetFunction targetFunction = lpProblem.getTargetFunction();
		if(targetFunction.getType()!=TargetFunctionType.MAX) throw new IllegalArgumentException("Zielfunktionstyp muss MAX sein!");

		// Variablennamen indizieren
		TreeSet<String> variables = lpProblem.collectVariables();
		int index=1;
		for(String varName: variables) {
			_varnameIndex.put(varName,  new Integer(index++));
		}

		HashSet<Restriction> restrictions = lpProblem.getAllRestrictions();

		_maxDimCol = variables.size() + 1;
		_maxDimRow = _maxDimCol;

		_matrix = new BigFraction[_maxDimCol][_maxDimRow];

		// Zielfunktion hinzufügen (immer in Zeile 0)
		_targetFunctionIdentifier = targetFunction.getIdentifier();
		LinearCombination lc = targetFunction.getLinearCombination();
		setValue(0, 0, lc.getConstComponent());
		for(VarComponent vc: lc.getVarComponents()) {
			_nonBaseVariables.add(vc.getIdentifier());
			colIndex = _varnameIndex.get(vc.getIdentifier());
			setValue(colIndex, 0, vc.getValue().negate());
		}

		// Nichtbasisvariablen hinzufügen
		for(String nonBaseVar: _nonBaseVariables) {
			rowIndex = _varnameIndex.get(nonBaseVar);
			setValue(0, rowIndex, BigFraction.ZERO);
			setValue(rowIndex, rowIndex, BigFraction.ONE);
		}

		// Basisvariablen hinzufügen (aus Restriktionen)
		for(Restriction r: restrictions) {
			String slackVariable = r.getSlackVariable();
			_baseVariables.add(slackVariable);
			rowIndex = _varnameIndex.get(slackVariable);
			lc = r.getLinearCombination();
			BigFraction cc = r.getRightHandSide();
			setValue(0, rowIndex, cc);					// rechte Seite
			setValue(rowIndex, rowIndex, BigFraction.ONE);	// Schlupfvariable
			for(VarComponent vc: lc.getVarComponents()) {
				String id = vc.getIdentifier();
				BigFraction v  = vc.getValue();
				colIndex = _varnameIndex.get(id);
				setValue(colIndex, rowIndex, v);
			}
		}
	}

	/**
	 * Erzeugt aus dem Ergebnis der Phase I ein Simplex-Problem für die Durchführung der Phase II 
	 * des Simplex-Algorithmus.
	 * @param simplexProblem
	 */
	public SimplexProblem(SimplexProblemPhase1 simplexProblem) {
		this(simplexProblem._maxDimCol, simplexProblem._maxDimRow-1);
		_varnameIndex = simplexProblem._varnameIndex;
		_baseVariables = simplexProblem._baseVariables;
		_nonBaseVariables = simplexProblem._nonBaseVariables;
		_targetFunctionIdentifier = simplexProblem.getOriginalTargetFunctionIdentifier();

		// Zielfunktion übertragen
		for(Integer col=0; col<_maxDimCol; col++) {
			setValue(col, 0, simplexProblem.getValue(col, _maxDimRow));
		}
		
		// Matrix übertragen
		for(Integer row=1; row<_maxDimRow; row++) {
			for(Integer col=0; col<_maxDimCol; col++) {
				setValue(col, row, simplexProblem.getValue(col, row));				
			}
		}
	}

	/**
	 * Prüft, ob die aktuelle Lösung gültig ist.
	 * @return
	 */
	public boolean isValid() {
		for(int i=1; i<_maxDimRow; i++) {
			if(getValue(0, i).compareTo(BigFraction.ZERO) < 0 ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Prüft, ob die aktuelle Lösung optimal ist.
	 * Das ist dann der Fall, wenn die Koeffizienten aller Variablen der Zielfunktion negativ oder gleich Null sind.
	 * @return
	 */
	public boolean isOptimal() {
		for(int i=1; i<_maxDimCol; i++) {
			if(getValue(i, 0).compareTo(BigFraction.ZERO) < 0 ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Prüft, ob die aktuelle Lösung degeneriert ist.
	 * Das ist dann der Fall, wenn ein Element der Basis gleich Null ist. 
	 * @return
	 */
	public boolean isDegenerated() {
		for(String b: _baseVariables) {
			int index = _varnameIndex.get(b);
			BigFraction value = getValue(0, index);
			if(value.equals(BigFraction.ZERO)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ermittelt die Pivot-Spalte (Pivot-Variable).
	 * @return Bezeichner, der für die Pivot-Spalte steht.
	 */
	public String getPivotColumn() {
		BigFraction maxval = BigFraction.ZERO;
		String pivot = null;
		for(String x: this._nonBaseVariables) {
			BigFraction current = this.getValue(this._varnameIndex.get(x), 0).negate();
			if(current.compareTo(maxval)>0) {
				maxval = current;
				pivot  = x;
			}
		}
		_pivotColumn = pivot;
		return pivot;
	}

	/**
	 * Liefert das Vorzeichen der übergebenen Zahl.
	 * @param n Die zu prüfende Zahl.
	 * @return TRUE, falls n&gt;=0. Andernfalls FALSE.
	 */
	protected int getSign(BigFraction n) {
		if(n.equals(BigFraction.ZERO))
			return 0;
		if(n.compareTo(BigFraction.ZERO)>0)
			return 1;
		return -1;
	}

	/**
	 * Ermittelt die Pivot-Zeile.
	 * Vorraussetzung: Die Pivot-Zeile ist bereits bestimmt.
	 * @return Bezeichner, der für die Pivot-Zeile steht.
	 */
	public String getPivotRow() {
		if( _pivotColumn==null || _pivotColumn.length()==0 ) throw new IllegalStateException();

		Integer pivotColumnIndex = _varnameIndex.get(_pivotColumn);
		BigFraction minVal = new BigFraction(Double.MAX_VALUE);
		String pivotRow = null;

		for(HashMap.Entry<String, Integer> e: _varnameIndex.entrySet()) {
			if(e.getKey().compareTo(_pivotColumn)!=0) {
				BigFraction divider = getValue(pivotColumnIndex, e.getValue());
				BigFraction value   = getValue(0, e.getValue());
				if( ! ( divider.equals(BigFraction.ZERO) || value.equals(BigFraction.ZERO) ) ) {
					if( getSign(divider) == getSign(value) ) {
						BigFraction quotient = value.divide(divider);
						if(quotient.compareTo(minVal)<0) {
							minVal = quotient;
							pivotRow = e.getKey();
						}
					}
				}
			}
		}
		_pivotRow = pivotRow;
		return pivotRow;
	}
	
	/**
	 * Führe Basistausch durch.
	 * Vorraussetzung: Sowohl die Pivot-Zeile als auch die Pivor-Spalte sind bereits bestimmt.
	 */
	public void exchangeBasevar() {
		if( _pivotColumn==null || _pivotColumn.length()==0 ) throw new IllegalStateException();
		if( _pivotRow==null    || _pivotRow.length()==0 )    throw new IllegalStateException();

		Integer index_in  = _varnameIndex.get(_pivotColumn);
		Integer index_out = _varnameIndex.get(_pivotRow);
		
		// Pivot-Zeile behandeln
		BigFraction divider = getValue(index_in, index_out);
		for(int col=0; col<_maxDimCol; col++) {
			_matrix[col][index_out] = getValue(col, index_out).divide(divider);
		}

		// Alle anderen Zeilen behandeln
		for(int row=0; row<_maxDimRow; row++) {
			if(row!=index_out) {
				BigFraction multiplier = getValue(index_in, row);
				for(int col=0; col<_maxDimCol; col++) {
					this._matrix[col][row] = getValue(col, row).subtract( multiplier.multiply(getValue(col, index_out)) );
				}
			}
		}

		// Zeilen tauschen
		for(int col=0; col<_maxDimCol; col++) {
			this._matrix[col][index_in] = getValue(col, index_out);
			if(col==index_out)
				this._matrix[col][index_out] = BigFraction.ONE;
			else
				this._matrix[col][index_out] = BigFraction.ZERO;
		}

		_baseVariables.remove(_pivotRow);
		_baseVariables.add(_pivotColumn);
		_nonBaseVariables.remove(_pivotColumn);
		_nonBaseVariables.add(_pivotRow);

		_pivotColumn = "";
		_pivotRow = "";
	}

	/**
	 * Ändert den Wert einer bestimmten Zelle der Matrix.
	 * @param col Spaltennummer
	 * @param row Zeilennummer
	 * @param value Neuer Wert
	 */
	protected void setValue(int col, int row, BigFraction value) {
		_matrix[col][row]=value;
	}

	/**
	 * Liefert den Wert einer bestimmten Zelle der Matrix.
	 * @param col Spaltennummer
	 * @param row Zeilennummer
	 * @return Aktueller Wert der Zelle
	 */
	protected BigFraction getValue(int col, int row) {
		BigFraction retVal = _matrix[col][row];
		if( retVal!=null) {
			return retVal;
		}
		return BigFraction.ZERO;
	}
	
	/**
	 * Erzeugt den Kopfbereich der HTML-Darstellung des Objekts.
	 * @return Kopfbereich der HTML-Darstellung des Objekts
	 */
	protected String addFormatedHeader() {
		String retVal = "";
		retVal += addFormatedTargetFunction(_targetFunctionIdentifier, 0);
		return retVal;
	}

	/**
	 * Erzeugt eine HTML-Darstellung der Zielfunktion.
	 * @param id Bezeichner der Optimierungsvariablen.
	 * @param row Index der Matrizzenzeile, in der die Zielfunktion abgelegt ist.
	 * @return HTML-Darstellung der Zielfunktion
	 */
	protected String addFormatedTargetFunction(String id, int row) {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		String retVal = "";

		retVal += "<tr class=\"target\">";
		
		retVal += "<td>" + id + "</td>";
		retVal += "<td class=\"operator_col\">=</td>";
		retVal += "<td>" + nf.format(getValue(0, row)) + "</td>";
		
		for(String colName: _nonBaseVariables) {
			boolean highlight = colName.compareTo(_pivotColumn)==0;
			int colIndex = _varnameIndex.get(colName);
			BigFraction v = getValue(colIndex, row);
			if(highlight) {
				retVal += "<td class=\"highlight\">";
			} else {
				retVal += "<td>";
			}
			retVal += formatDouble(v.negate(), colName); 
			retVal += "</td>";
		}
		
		retVal += "</tr>\n";
		
		return retVal;
	}
	
	/**
	 * Erzeugt eine HTML-Darstellung aller Restriktionen.
	 * @return HTML-Darstellung aller Restriktionen
	 */
	protected String addFormatedRestrictions() {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		String retVal = "";
		
		for(String rowName: _baseVariables) {
			boolean highlight_row = rowName.compareTo(_pivotRow)==0;
			retVal += "<tr>";
			int rowIndex = _varnameIndex.get(rowName);
			
			if(highlight_row) {
				retVal += "<td class=\"highlight\">";
			} else {
				retVal += "<td>";
			}
			retVal += rowName;
			retVal += "</td>";
			
			if(highlight_row) {
				retVal += "<td class=\"operator_col_highligh\">";
			} else {
				retVal += "<td class=\"operator_col\">";
			}
			retVal += "=";
			retVal += "</td>";
			
			if(highlight_row) {
				retVal += "<td class=\"highlight\">";
			} else {
				retVal += "<td>";
			}
			retVal += nf.format(getValue(0, rowIndex));
			retVal += "</td>";

			for(String colName: _nonBaseVariables) {
				boolean highlight_col = colName.compareTo(_pivotColumn)==0;
				int colIndex = _varnameIndex.get(colName);
				if(colIndex!=rowIndex) {
				BigFraction v = getValue(colIndex, rowIndex);
				if(highlight_col || highlight_row) {
					retVal += "<td class=\"highlight\">";
				} else {
					retVal += "<td>";
				}
				retVal += formatDouble(v.negate(), colName);
				retVal += "</td>";
			}
			}
			
			retVal += "</tr>\n";
		}

		return retVal;
	}
	
	/**
	 * Erzeugt den Fussbereich der HTML-Darstellung des Objekts.
	 * @return Fussbereich der HTML-Darstellung des Objekts
	 */
	protected String addFormatedFooter() {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		String retVal = "";
		
		retVal += "<p>";

		retVal += "<div>";
		retVal += "Basisvariablen: ";
		boolean first = true;
		for(String s: _baseVariables) {
			if(first) 
				first = false;
			else
				retVal += ", ";
			retVal += s + "=" + nf.format(getValue(0, _varnameIndex.get(s)));
		}
		retVal += "</div>\n";

		retVal += "<div>";
		retVal += "Nichtbasisvariablen: ";
		first = true;
		for(String s: _nonBaseVariables) {
			if(first) 
				first = false;
			else
				retVal += ", ";
			retVal += s + "=" + nf.format(getValue(0, _varnameIndex.get(s)));
		}
		retVal += "</div>\n";

		retVal += "<div>";
		retVal += "Zielfunktionswert: " + _targetFunctionIdentifier + "=" + nf.format(getValue(0, 0));
		retVal += "</div>\n";

		retVal += "</p>\n";

		return retVal;
	}
	
	/**
	 * Liefert eine HTML-Darstellung des Objekts.
	 * @return
	 */
	public String getHtmlString(boolean addFooter) {
		// Zielfunktion
		String retVal =	"<p>max " + _targetFunctionIdentifier + "</p>\n";
		
		retVal += "<table>\n";
		
		retVal += addFormatedHeader();

		retVal += addFormatedRestrictions();
		
		retVal += "</table>\n";
		
		if(addFooter) {
			retVal += addFormatedFooter();
		}

		retVal += "\n";
		
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
		
//		retVal = showTableau(retVal);

		return retVal;
	}

	/**
	 * Tableau-Darstellung für Fehlersuche
	 * @param retVal
	 */
	protected String showTableau(String retVal) {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		int maxlen = 0;
		String[][] out = new String[_maxDimCol][_maxDimRow];
		for(int row=0; row<_maxDimRow; row++) {
			for(int col=0; col<_maxDimCol; col++) {
				BigFraction v = getValue(col, row);
				String s = nf.format(v);
				out[col][row] = s;
				if(s.length()>maxlen) maxlen = s.length();
			}
		}
		maxlen++;

		for(int row=0; row<_maxDimRow; row++) {
			for(int col=0; col<_maxDimCol; col++) {
				String s = out[col][row];
				for(int i=0; i<maxlen-s.length(); i++) retVal += " ";
				retVal += s;
			}
			retVal += "\n";
		}

		retVal += "\n";
		
		return retVal;
	}

	/**
	 * Bereitet ein Objekt der Klasse BigFraction für die Ausgabe auf.
	 * @param d Aufzubereitender Wert
	 * @param var Bezeichner der dem Wert zugeordneten Optimierungsvariablen.
	 * @return Aufbereitete String-Darstellung des Werts
	 */
	protected String formatDouble(BigFraction d, String var) {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		String retVal = "";
		if(d!=null) {
			if(d.equals(BigFraction.ONE)) {
				retVal = " + " + var;
			} else if(d.equals(BigFraction.MINUS_ONE)) {
				retVal = " - " + var;
			} else if(d.equals(BigFraction.ZERO)) {
			} else {
				if(d.compareTo(BigFraction.ZERO)>0) {
					retVal += " + ";
				} else {
					retVal += " - ";
					d = d.negate();
				}
				retVal += nf.format(d) + "&sdot;" + var;
			}
		}
		return retVal;
	}

	@Override
	public int hashCode() {
		throw new IllegalStateException("Not implemented yet!");
	}

	@Override
	public boolean equals(Object obj) {
		throw new IllegalStateException("Not implemented yet!");
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


}
