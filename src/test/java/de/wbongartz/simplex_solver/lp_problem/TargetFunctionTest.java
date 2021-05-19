package de.wbongartz.simplex_solver.lp_problem;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Wolfgang Bongartz
 *
 */
public class TargetFunctionTest {
	
	private TargetFunction tf1, tf1_eq, tf2;

	@BeforeEach
	public void setUp() {
		LinearCombination lc1, lc2;
		
		lc1 = new LinearCombination();
		lc1.addConstComponent(100.0);
		lc1.addVarComponent(new VarComponent(1.0, "x1"));
		
		lc2 = new LinearCombination();
		lc2.addConstComponent(-5.0);
		lc2.addVarComponent(new VarComponent(2.0, "x1"));
		lc2.addVarComponent(new VarComponent(-1.0, "x2"));

		tf1    = new TargetFunction("z", TargetFunctionType.MAX, lc1);
		tf1_eq = new TargetFunction("z", TargetFunctionType.MAX, lc1);
		tf2    = new TargetFunction("z", TargetFunctionType.MIN, lc2);
		
	}

	@Test
	public final void testTargetFunction() {
	}

	/**
	 * Test method for {@link TargetFunction#hashCode()}.
	 */
	@Test
	public final void testHashCode() {
		if(tf1.hashCode()!=tf1_eq.hashCode()) fail();
		if(tf1.hashCode()==tf2.hashCode()) fail();
	}

	/**
	 * Test method for {@link TargetFunction#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		if(!tf1.equals(tf1_eq)) fail();
		if(tf1.equals(tf2)) fail();
	}

	/**
	 * Test method for {@link TargetFunction#clone()}.
	 */
	@Test
	public final void testClone() {
	}

	/**
	 * Test method for {@link TargetFunction#getIdentifier()}.
	 */
	@Test
	public final void testGetIdentifier() {
	}

	/**
	 * Test method for {@link TargetFunction#getType()}.
	 */
	@Test
	public final void testGetType() {
	}

	/**
	 * Test method for {@link TargetFunction#toString()}.
	 */
	@Test
	public final void testToString() {
	}

}
