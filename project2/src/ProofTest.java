import static org.junit.Assert.*;

import org.junit.Test;


public class ProofTest {

	@Test
	public void testTheoremSetPut() {
		
		Expression expression1 = new Expression();
		
		try {
			expression1 = new Expression ("(x=>y)");
		} catch (IllegalLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String theorem1 = "TestTheorem";
		String theorem1content = "=>";
		
		TheoremSet myTheorems;
		myTheorems = new TheoremSet( );
		
		
		myTheorems.put (theorem1, expression1);
		System.out.println(myTheorems.get(theorem1).toString());
	
	}
	
	@Test
	public void testRight2() {
		
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
	public class OgLineNumberTest {

		@Test
		public void testLineNumber() {
			OgLineNumber a = new OgLineNumber();
			OgLineNumber.currentLine(null);
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("show ((a=>b)=>((b=>c)=>(a=>c)))");
			System.out.println("print line 2: "+ a.toString());
			OgLineNumber.currentLine("assume (a=>b)");
			System.out.println("print line 3: "+ a.toString());
			OgLineNumber.currentLine("show ((b=>c)=>(a=>c))");
			System.out.println("print line 3.1: "+ a.toString());
			OgLineNumber.currentLine("assume (b=>c)");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("show (a=>c)");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("assume a");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("show c");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("show ((a=>b)=>((b=>c)=>(a=>c)))");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("show ((a=>b)=>((b=>c)=>(a=>c)))");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("3.2.2.4 co 3.2.2.3 3.2.1 c");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("ic 3.2.2 (a=>c)");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("ic 3.2 ((b=>c)=>(a=>c))");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("ic 3.2 ((b=>c)=>(a=>c))");
			System.out.println("print line 1: "+ a.toString());
			OgLineNumber.currentLine("ic 3.2 ((b=>c)=>(a=>c))");
			System.out.println("print line 1: "+ a.toString());
		}

	}
	
}
