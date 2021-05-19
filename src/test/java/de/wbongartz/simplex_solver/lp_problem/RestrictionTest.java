package de.wbongartz.simplex_solver.lp_problem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Wolfgang Bongartz
 *
 */
public class RestrictionTest {
	
	private Restriction r1, r1_eq, r2;

	@BeforeEach
	public void setUp() {
		LinearCombination lc1, lc1_eq, lc2;
		
		lc1 = new LinearCombination();
		lc1.addConstComponent(100.0);
		lc1.addVarComponent(new VarComponent(1.0, "x1"));

		lc1_eq = new LinearCombination();
		lc1_eq.addConstComponent(100.0);
		lc1_eq.addVarComponent(new VarComponent(1.0, "x1"));

		lc2 = new LinearCombination();
		lc2.addConstComponent(-5.0);
		lc2.addConstComponent(10.0);
		lc2.addVarComponent(new VarComponent(2.0, "x1"));
		lc2.addVarComponent(new VarComponent(-1.0, "x2"));

		r1 = new Restriction(lc1, Operator.LESS_OR_EQUAL, 100.0);
		r1_eq = new Restriction(lc1_eq, Operator.LESS_OR_EQUAL, 100.0);
		r2 = new Restriction(lc2, Operator.EQUAL, -100.0);
	}

	@Test
	public final void testRestriction() {
		
		// 1.0 < 0.0
		try {
			LinearCombination lc = new LinearCombination();
			lc.addConstComponent(1.0);
			new Restriction(lc, Operator.LESS, 0.0);
			fail();
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// 1.0 <= 0.0
		try {
			LinearCombination lc = new LinearCombination();
			lc.addConstComponent(1.0);
			new Restriction(lc, Operator.LESS_OR_EQUAL, 0.0);
			fail();
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// 1.0 = 0.0
		try {
			LinearCombination lc = new LinearCombination();
			lc.addConstComponent(1.0);
			new Restriction(lc, Operator.EQUAL, 0.0);
			fail();
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// 1.0 > 2.0
		try {
			LinearCombination lc = new LinearCombination();
			lc.addConstComponent(1.0);
			new Restriction(lc, Operator.MORE, 2.0);
			fail();
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// 1.0 >= 2.0
		try {
			LinearCombination lc = new LinearCombination();
			lc.addConstComponent(1.0);
			new Restriction(lc, Operator.MORE_OR_EQUAL, 2.0);
			fail();
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// 1.0 >= 0.0
		try {
			LinearCombination lc = new LinearCombination();
			lc.addConstComponent(1.0);
			Restriction r = new Restriction(lc, Operator.MORE_OR_EQUAL, 0.0);
			String res = r.toString();
			if(res.compareTo("0 >= -1")!=0) fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test method for {@link Restriction#hashCode()}.
	 */
	@Test
	public final void testHashCode() {
		if(r1.hashCode()!=r1_eq.hashCode()) fail();
		if(r1.hashCode()==r2.hashCode()) fail();
	}

	/**
	 * Test method for {@link Restriction#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		if(!r1.equals(r1_eq)) fail();
		if(r1.equals(r2)) fail();
	}

	/**
	 * Test method for {@link Restriction#clone()}.
	 */
	@Test
	public final void testClone() {
	}

	/**
	 * Test method for {@link Restriction#toString()}.
	 */
	@Test
	public final void testToString() {
	}

}
