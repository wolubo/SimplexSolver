/**
 * 
 */
package lp_problem.unittest;

import static org.junit.Assert.*;
import lp_problem.LinearCombination;
import lp_problem.TargetFunction;
import lp_problem.TargetFunctionType;
import lp_problem.VarComponent;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Wolfgang Bongartz
 *
 */
public class TargetFunctionTest {
	
	private TargetFunction tf1, tf1_eq, tf2;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
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

	/**
	 * Test method for {@link lp_problem.TargetFunction#TargetFunction(java.lang.String, lp_problem.TargetFunctionType, lp_problem.LinearCombination)}.
	 */
	@Test
	public final void testTargetFunction() {
	}

	/**
	 * Test method for {@link lp_problem.TargetFunction#hashCode()}.
	 */
	@Test
	public final void testHashCode() {
		if(tf1.hashCode()!=tf1_eq.hashCode()) fail();
		if(tf1.hashCode()==tf2.hashCode()) fail();
	}

	/**
	 * Test method for {@link lp_problem.TargetFunction#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		if(!tf1.equals(tf1_eq)) fail();
		if(tf1.equals(tf2)) fail();
	}

	/**
	 * Test method for {@link lp_problem.TargetFunction#clone()}.
	 */
	@Test
	public final void testClone() {
	}

	/**
	 * Test method for {@link lp_problem.TargetFunction#getIdentifier()}.
	 */
	@Test
	public final void testGetIdentifier() {
	}

	/**
	 * Test method for {@link lp_problem.TargetFunction#getType()}.
	 */
	@Test
	public final void testGetType() {
	}

	/**
	 * Test method for {@link lp_problem.TargetFunction#toString()}.
	 */
	@Test
	public final void testToString() {
	}

}
