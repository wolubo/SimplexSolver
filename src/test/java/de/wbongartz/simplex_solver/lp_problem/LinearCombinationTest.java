/**
 * 
 */
package lp_problem.unittest;

import static org.junit.Assert.*;

import java.util.TreeSet;

import lp_problem.LinearCombination;
import lp_problem.VarComponent;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Wolfgang Bongartz
 *
 */
public class LinearCombinationTest {
	
	private LinearCombination empty, lc1, lc1_eq, lc2, lc2_eq;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		empty = new LinearCombination();
		
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

		lc2_eq = new LinearCombination();
		lc2_eq.addConstComponent(-5.0);
		lc2_eq.addConstComponent(10.0);
		lc2_eq.addVarComponent(new VarComponent(2.0, "x1"));
		lc2_eq.addVarComponent(new VarComponent(-1.0, "x2"));
	}

	/**
	 * Test method for {@link lp_problem.LinearCombination#hashCode()}.
	 */
	@Test
	public final void testHashCode() {
		try {
//			if(empty.hashCode()!=0) fail();
			
			if(lc1.hashCode()!=lc1_eq.hashCode()) fail();
			if(lc2.hashCode()!=lc2_eq.hashCode()) fail();

			if(lc1.hashCode()==lc2.hashCode()) fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 */
	@Test
	public final void testAddComponent() {
	}

	/**
	 * Test method for {@link lp_problem.LinearCombination#toString()}.
	 */
	@Test
	public final void testToString() {
		try {
			empty.toString();
			lc1.toString();
			lc2.toString();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test method for {@link lp_problem.LinearCombination#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		try {
			if(!empty.equals(empty)) fail();
			if(!lc1.equals(lc1)) fail();
			if(!lc1.equals(lc1_eq)) fail();
			if(!lc2.equals(lc2_eq)) fail();
			
			if(empty.equals(lc1)) fail();
			if(lc1.equals(empty)) fail();
			if(lc1.equals(lc2)) fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test method for {@link lp_problem.LinearCombination#clone()}.
	 */
	@Test
	public final void testClone() {
	}
	
	@Test
	public final void testCollectVariables() {
		{
			TreeSet<String> varList = new TreeSet<String>();
			lc1.collectVariables(varList);
			if(varList.size()!=1) fail();
			String[] v = new String[1];
			v=varList.toArray(v);
			if(v[0].compareTo("x1")!=0) fail();
		}
		
		{
			TreeSet<String> varList = new TreeSet<String>();
			lc2.collectVariables(varList);
			if(varList.size()!=2) fail();
			String[] v = new String[2];
			v=varList.toArray(v);
			if(v[0].compareTo("x1")!=0) fail();
			if(v[1].compareTo("x2")!=0) fail();
		}
	}

}
