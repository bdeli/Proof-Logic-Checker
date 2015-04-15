import static org.junit.Assert.*;

import org.junit.Test;


public class ExprTest {


	@Test
	public void testEmptyExpressionNoClosingParentheses() {

		Expression expression1 = new Expression();

		try {
			expression1 = new Expression ("((x&y)=>x");
		} catch (IllegalLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testEmptyExpressionNoOpeningParentheses() {

		Expression expression1 = new Expression();

		try {
			expression1 = new Expression ("x=>x)");
		} catch (IllegalLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Test
	public void testLeft() {

		Expression expression1 = new Expression();

		try {
			expression1 = new Expression ("(x=>y)");
		} catch (IllegalLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String leftString = "x";
		assertEquals(expression1.left(), leftString);

	}	


	@Test
	public void testRight() {

		Expression expression1 = new Expression();

		try {
			expression1 = new Expression ("(x=>y)");
		} catch (IllegalLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String rightString = "y";
		assertEquals(expression1.right(), rightString);

	}	


	public void testCons(){
		boolean tf = true;
		Expression exp = new Expression();
		try {
			exp = new Expression("((a&b)=>(a|b))");
			System.out.println("Constructor called.");
		} catch (IllegalLineException e) {
			tf = false;
			System.out.print(e.getMessage());
		}
		System.out.println("Print.");
		System.out.println("opnd1: " + exp.left());// + exp.left()
		System.out.println("op: " + exp.eTree.myItem);
		System.out.println("opnd2: " + exp.right());// + exp.left()
		System.out.println("Done print.");
		System.out.println("exp:" + exp.eTree.toString());
		assertTrue(tf);
		assertEquals(exp.eTree.myItem, "=>");
		assertEquals(exp.left(), "(a&b)");
		assertEquals(exp.right(), "(a|b)");
	}

	public void testEquals(){
		Expression one = new Expression();
		Expression two = new Expression();
		try {
			one = new Expression("(a=>b)");
			two = new Expression("(a=>b)");
		} catch (IllegalLineException ln) {
			System.out.println("cons failed");
		}
		assertTrue(one.equals(two));

		try {
			one = new Expression("(p=>(q|x))");
			two = new Expression("(a=>b)");
		} catch (IllegalLineException ln) {
			System.out.println("cons failed");
		}
		assertFalse(one.equals(two));

	}

}