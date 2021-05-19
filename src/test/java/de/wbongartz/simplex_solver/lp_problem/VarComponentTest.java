package de.wbongartz.simplex_solver.lp_problem;


import org.apache.commons.math3.fraction.BigFraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


public class VarComponentTest {
	
	private VarComponent x1, x2, x1_eq, x2_neq;

	@BeforeEach
	public void setUp() {
		x1 = new VarComponent(BigFraction.ONE, "x1");
		x2 = new VarComponent(2.0, "x2");
		x1_eq = new VarComponent(BigFraction.ONE, "x1");
		x2_neq = new VarComponent(-2.0, "x2");
	}

	@Test
	public final void testVarComponent() {
	}

	@Test
	public final void testToString() {
	}

	@Test
	public final void testHashCode() {
		if(x1.hashCode()!=x1.hashCode()) fail();
		if(x1.hashCode()!=x1_eq.hashCode()) fail();

		if(x1.hashCode()==x2.hashCode()) fail();
		if(x2.hashCode()==x2_neq.hashCode()) fail();
	}

	@Test
	public final void testEqualsObject() {
		if(!x1.equals(x1)) fail();
		if(!x1.equals(x1_eq)) fail();
		if(!x2.equals(x2)) fail();

		if(x1.equals(x2)) fail();
		if(x2.equals(x1)) fail();
		if(x2.equals(x2_neq)) fail();
	}

	@Test
	public final void testClone() {
	}

	@Test
	public final void testCompareTo() {
		if(x1.compareTo(x1)!=0) fail();
		if(x1.compareTo(x1_eq)!=0) fail();
		if(x2.compareTo(x2)!=0) fail();

		if(x2.compareTo(x2_neq)==0) fail();

		if(x1.compareTo(x2)>=0) fail();
		if(x2.compareTo(x1)<=0) fail();
	}

	@Test
	public final void testIsNegative() {
		if(x1.isNegative()) fail();
		if(x1_eq.isNegative()) fail();
		if(x2.isNegative()) fail();

		if(!x2_neq.isNegative()) fail();
	}

	@Test
	public final void testGetIdentifier() {
		if(x1.getIdentifier().compareTo("x1")!=0) fail();
	}

	@Test
	public final void testAdd() {
		VarComponent r;
		
		r=x1.add(x1_eq);
		if( ! r.getValue().equals(BigFraction.TWO) ) fail();

		r=x2.add(x2_neq);
		if( ! r.getValue().equals(BigFraction.ZERO) ) fail();
		
		try {
			r=x1.add(x2);
			fail();
		} 
		catch(IllegalArgumentException ex) {
		}
		catch(Exception ex) {
			fail();
		}
	}

	@Test
	public final void testMultiply() {
		VarComponent r;
		
		r=(VarComponent) x1.multiply(1.0);
		if( ! r.getValue().equals(BigFraction.ONE) ) fail();

		r=(VarComponent) x2.multiply(-1.0);
		if( ! r.getValue().equals(BigFraction.TWO.negate()) ) fail();
	}

	@Test
	public final void testNegate() {
		VarComponent r;
		
		r = (VarComponent) x1.negate();
		if( ! r.getValue().equals(BigFraction.MINUS_ONE) ) fail();

		r = (VarComponent) x2_neq.negate();
		if( ! r.getValue().equals(BigFraction.TWO) ) fail();
	}

}
