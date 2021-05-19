package de.wbongartz.simplex_solver.lp_problem;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * Kapselt eine Restriktion der Form '3*X1 - x2 &lt;= 5'.
 * Eine Restriktion besteht aus einer Linearkombination. Außerdem beinhaltet sie ein Operator (Klasse Operator), 
 * einen konstanten Wert (auf der rechten Seite des Operators) und gegebenenfalls eine Schlupfvariable.
 * Im Beispiel 2x1 + x2 &lt;= 1000 ist 2x1 + x2 die Linearkombination, &lt;= der Operator und 1000 ist der konstante Wert.
 * HINWEIS: Variable Komponenten auf der rechten Seite werden derzeit nicht unterstützt.
 * 
 * @author Wolfgang Bongartz
 */
public class Restriction {

	private Operator _operator;
	private BigFraction _rightHandSide;
	private LinearCombination _linearCombination;
	private String _slackVariable = null;

	/**
	 * Erzeugt eine neue Instanz. Überträgt dabei die konstante Komponente der übergebenen Linearkombination auf 
	 * die rechte Seite und prüft, ob die Restriktion grundsätzlich erfüllbar ist ('4 &gt;= 3' ist nicht erfüllbar).
	 * @param lc Linearkombination (linke Seite der Restriktion)
	 * @param op Operator
	 * @param rhs Rechte Seite der Restriktion (derzeit zwingend eine Konstante). 
	 */
	public Restriction(LinearCombination lc, Operator op, BigFraction rhs) {
		if(lc==null) throw new IllegalArgumentException();
		_linearCombination = lc;
		_operator = op;
		if(lc.hasConstComponent()) {
			_rightHandSide = rhs.subtract(lc.getConstComponent());
			getLinearCombination().setConstComponent(BigFraction.ZERO);
		} else {
			_rightHandSide = rhs;
		}
		if(!isSolvable()) throw new IllegalStateException("Die Restriktion " + this.toString() + " ist unerfüllbar!");
	}

	/**
	 * Erzeugt eine neue Instanz. Überträgt dabei die konstante Komponente der übergebenen Linearkombination auf 
	 * die rechte Seite und prüft, ob die Restriktion grundsätzlich erfüllbar ist ('4 &gt;= 3' ist nicht erfüllbar).
	 * @param lc Linearkombination (linke Seite der Restriktion)
	 * @param op Operator
	 * @param rhs Rechte Seite der Restriktion (derzeit zwingend eine Konstante). 
	 */
	public Restriction(LinearCombination lc, Operator op, Double rhs) {
		this(lc,op, new BigFraction(rhs));
	}

	/** 
	 * Liefert den Bezeichner der Schlupfvariablen.
	 * @return
	 */
	public String getSlackVariable() {
		return _slackVariable;
	}

	/** 
	 * Setzt den Bezeichner der Schlupfvariablen.
	 */
	public void setSlackVariable(String slackVariable) {
		_operator=Operator.EQUAL;
		_slackVariable = slackVariable;
	}

	/**
	 * Liefert die linke Seite (konstante Komponente ist darin immer Null).
	 * @return
	 */
	public LinearCombination getLinearCombination() {
		return _linearCombination;
	}
	
	/**
	 * Liefert den Operator.
	 * @return
	 */
	public Operator getOperator() {
		return _operator;
	}

	/**
	 * Liefert die rechte Seite.
	 * @return
	 */
	public BigFraction getRightHandSide() {
		return _rightHandSide;
	}

	/**
	 * Prüft, ob die Restriktion nur dazu dient, den Wertebereich einer Variablen als 'grösser oder gleich Null' zu definieren (wie bspw. X1&gt;=0).
	 * @return
	 */
	public boolean definesCodomain() {
		if(_slackVariable==null && _operator==Operator.MORE_OR_EQUAL) {
			if(getLinearCombination().getNumberOfVarComponents()==1) {
				return _rightHandSide.equals(BigFraction.ZERO);
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die Restriktion grundsätzlich erfüllbar ist.
	 * '1=2' oder '5<=6' wären bspw. grundsätzlich nicht erfüllbar.
	 * @return
	 */
	private boolean isSolvable() {
		if(getLinearCombination().getNumberOfVarComponents()==0) {
			switch(_operator) {
			case EQUAL:
				return _rightHandSide.equals(BigFraction.ZERO);
			case LESS:
				return _rightHandSide.compareTo(BigFraction.ZERO)>0;
			case LESS_OR_EQUAL:
				return _rightHandSide.compareTo(BigFraction.ZERO)>=0;
			case MORE:
				return _rightHandSide.compareTo(BigFraction.ZERO)<0;
			case MORE_OR_EQUAL:
				return _rightHandSide.compareTo(BigFraction.ZERO)<=0;
			default:
				return true;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
		String lc_str = getLinearCombination().toString();
		if(getLinearCombination().getNumberOfVarComponents()==0) lc_str="0";
		if(_slackVariable!=null) lc_str+= " + " + _slackVariable;			
		String retVal = lc_str + _operator.toString() + nf.format(_rightHandSide);
		return retVal;
	}

	@Override
	public int hashCode() {
		int s = _linearCombination.hashCode();
		if(_slackVariable!=null) s += _slackVariable.hashCode();
		int lc = getLinearCombination().hashCode();
		int rhs = _rightHandSide.hashCode();
		int h = s + lc + rhs;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if(! (obj instanceof Restriction) ) return false;
		Restriction other = (Restriction) obj;
		if(this._linearCombination.equals(other._linearCombination)) {
			if(this._operator.equals(other._operator)) { 
				if(this._rightHandSide.equals(other._rightHandSide)) {
					if(this._slackVariable!=null) {
						return this._slackVariable.equals(other._slackVariable);
					} else {
						return other._slackVariable==null;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();		
	}

	/**
	 * Füllt die übergebene Liste mit allen in der Restriktion vorkommenden Variablennamen.
	 * @param varNames
	 */
	public void collectVariables(TreeSet<String> varNames) {
		getLinearCombination().collectVariables(varNames);
		if(_slackVariable!=null) varNames.add(_slackVariable);
	}

	/**
	 * Negiert die Restriktion. Aus 'X1 + X2 &lt; 5' wird also '-X1 - X2 &gt; -5'.
	 * @return
	 */
	public Restriction negate() {
		if(_operator!=Operator.EQUAL) {
			Operator newOp=null;
			switch(_operator) {
			case LESS: 
				newOp = Operator.MORE;
				break;
			case LESS_OR_EQUAL: 
				newOp = Operator.MORE_OR_EQUAL;
				break;
			case MORE:
				newOp = Operator.LESS;
				break;
			case MORE_OR_EQUAL:
				newOp = Operator.LESS_OR_EQUAL;
				break;
			default:
				break;
			}
			
			LinearCombination newLC = getLinearCombination().negate();
			BigFraction newRHS = _rightHandSide.negate();
			return new Restriction(newLC, newOp, newRHS);
		}
		return this;
	}

}
