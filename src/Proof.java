import java.util.*;

public class Proof {

	public ProofNode myTree;//use this for isComplete: "logicCheck(myTree.myExpr)". Holds expressions? not variables
	public static ProofNode currentLevel;
	public static TreeMap<String, Boolean> truthTable = new TreeMap<String, Boolean>();
	public ArrayList<String> validReasons = new ArrayList<String>();
	public static TheoremSet myThrms;
	public String prevLine;//for LineNumber class
	public LineNumber thisLine;
	public OgLineNumber lineNumber;

	public Proof (TheoremSet theorems) {
		myThrms = theorems;
		myTree = null;
		currentLevel = null;
		prevLine = null;	//set previous line to String.
		lineNumber = new OgLineNumber();
		thisLine = new LineNumber(OgLineNumber.printCountS);            //LineNumber needs this for instantiation
	}

	public LineNumber nextLineNumber ( ) {
		if (OgLineNumber.isValid) {
			OgLineNumber.currentLine(prevLine);             //needs to be stored
			thisLine = new LineNumber(OgLineNumber.printCountS);
		}
		return thisLine;        //intake previous line

	}



	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {

		OgLineNumber.isValid = false;
		OgLineNumber.movedUp = false;

		//for LineNumber class

		String input = x.trim();
		//check if it's one word


		if (input.contains("print")){//need to handle print input
			if (!input.equals("print")){
				throw new IllegalLineException("Nothing can follow print");
			}

			if (input.equals("print") && prevLine == null){
				throw new IllegalLineException("There are no lines to print yet");

			}

			System.out.print(this.toString());

			return;
		}

		String splitted[] = input.split(" ");
		int i = input.indexOf(' ');
		String word = splitted[0];
		String expr = splitted[splitted.length - 1];


		if (ProofChecker.iAmDebugging){						// print test
			System.out.println("Expression here:" + expr);	// print test
			System.out.println("Word here:" + word);		// print test
		}													// print test


		String[] valReasons = {"show","assume","mt", "mp", "co", "ic", "repeat"};
		for (String str : valReasons) {
			validReasons.add(str);
		}

		if (!(validReasons.contains(word)) ) {
			if (!(myThrms.containsTheorem(word)) ) {
				throw new IllegalLineException("Invalid reason characters.");
			}
		}

		if (ProofChecker.iAmDebugging){						// print test
			System.out.println("Valid first word");			// print test
		}		



		// Trying to catch potential errors with regards to constructing the actual expression that was split

		Expression ex = null;
		try {
			ex = new Expression(expr);
		} catch (IllegalLineException ln) {
			throw new IllegalLineException(ln.getMessage());
		}

		// Temporary variable to store proof nodes prior to adding them to the proof tree.


		//****************************************
		//***CHECKING OF OVERALL LINE VALIDITY****
		//****************************************

		ProofNode next;

		switch (word){

		case "show": if (splitted.length != 2) {
			throw new IllegalLineException("Wrong number of arguments.");
		}

		if (truthTable.isEmpty()) {
			myTree = new ProofNode(word, ex, thisLine);
			currentLevel = myTree;
			break;
		}


		//if truth table is not empty

		next = new ProofNode(word, ex, thisLine, currentLevel);
		currentLevel.myChildren.add(next);

		currentLevel = next;
		break;

		case "assume":

			if (splitted.length != 2) {//"assume" must follow show (show does NOT need to be followed by assume)
				throw new IllegalLineException("Wrong number of arguments.");
			}

			String checkLeft = "";
			if (currentLevel.myExpr.eTree.myLeft != null) {
				checkLeft = currentLevel.myExpr.eTree.myLeft.toString();//LHS of previous expression
			}

			if (!expr.equals(checkLeft)) {

				if (ProofChecker.iAmDebugging){							// print test
					System.out.println("THIS: " + checkLeft);						// print test
				}														// print test

				if (ProofChecker.iAmDebugging){							// print test
					System.out.println("!expr.equals(checkLeft)");		// print test
				}														// print test

				if (!Expression.ExprNode.isInverse(ex.eTree, currentLevel.myExpr.eTree)) {
					if (ProofChecker.iAmDebugging){																		// print test
						System.out.println("!Expression.ExprNode.isInverse(ex.eTree, currentLevel.myExpr.eTree)");		// print test
						System.out.println("Inverse?: " + Expression.ExprNode.isInverse(ex.eTree, currentLevel.myExpr.eTree));
					}																									// print test


					throw new IllegalLineException("Invalid argument.");
				}
			}

			next = new ProofNode(word, ex, thisLine, currentLevel);

			currentLevel.myChildren.add(next);

			truthTable.put(ex.toString(), new Boolean(true));//add expression in the truth tables. Given to be assumed as true

			if (ProofChecker.iAmDebugging){										// print test
				System.out.println("variable added");							// print test
				System.out.println(truthTable.containsKey(ex.toString()));		// print test
				System.out.println(ex.toString());								// print test
			}																	// print test


			break;


		case "mp":

			if (splitted.length != 4) {
				throw new IllegalLineException("Wrong number of arguments.");
			}




			//Setting new Expression object based on the first line number input.
			Expression e1 = new Expression();
			LineNumber line1 = new LineNumber(splitted[1]);
			e1 = myTree.getExpression(line1);

			//Setting new Expression object based on the second line number input.			
			Expression e2 = new Expression();
			LineNumber line2 = new LineNumber(splitted[2]);
			System.out.println("CURRENT LINE1 CHECK:" + line1);
			System.out.println("CURRENT LINE2 CHECK:" + line2);
			e2 = myTree.getExpression(line2);

			if (!OgLineNumber.lineArray.contains(line1.toString()) || !OgLineNumber.lineArray.contains(line2.toString())){
				throw new IllegalLineException("Invalid line argument.");
			}
			//if (e1.toString().equals(e2.left()) || e2.toString().equals(e1.left())){


			if (ProofChecker.iAmDebugging){										// print test
				System.out.println("MP E1" + e1);							// print test
				System.out.println("MP E2" + e2);							// print test
				System.out.println(expr);										// print test
			}	


			if (truthTable.containsKey(e2.toString()) && truthTable.containsKey(e1.toString())){


				if (ProofChecker.iAmDebugging){										// print test
					System.out.println("E1" + e1);							// print test
					System.out.println("E2" + e2);							// print test
					System.out.println(expr);										// print test
				}																	// print test

				int e1Count = e1.toString().length();
				int e2Count = e2.toString().length();

				Expression whole, part;
				if (e1Count > e2Count){
					whole = e1;
					part = e2;
				} else {
					whole = e2;
					part = e1;
				}

				if (!(logicCheck(whole.eTree.myLeft) == logicCheck(part.eTree))) {
					throw new IllegalInferenceException("Expressions aren't equal.");
				} else 

					if (!(logicCheck(whole.eTree.myRight) == logicCheck(ex.eTree))) {
						throw new IllegalInferenceException("Expression not valid.");	
					}

				//if ((expr.equals(e2.right()) && e1 == null)|| ( expr.equals(e1.right()) && e2 == null)) {

				truthTable.put(expr, new Boolean (true));	
				//}

				//}
			}

			next = new ProofNode(word, line1, line2, ex, thisLine, currentLevel);
			currentLevel.myChildren.add(next);

			if (expr.equals(currentLevel.myExpr.toString())) {
				if (currentLevel.myParent != null) {
					currentLevel = currentLevel.myParent;
					OgLineNumber.movedUp = true;
				}
			}

			break;

			// Modus Tollens Case

		case "mt":

			if (splitted.length != 4) {
				throw new IllegalLineException("Wrong number of arguments.");
			}

			//Setting new Expression object based on the first line number input.
			Expression e11 = new Expression();
			LineNumber line11 = new LineNumber(splitted[1]);
			e11 = myTree.getExpression(line11);

			//Setting new Expression object based on the second line number input.			
			Expression e22 = new Expression();
			LineNumber line22 = new LineNumber(splitted[2]);

			if (ProofChecker.iAmDebugging){										// print test
				System.out.println("CURRENT LINE11 CHECK:" + line11);				// print test
				System.out.println("CURRENT LINE22 CHECK:" + line22);				// print test
			}																	// print test

			e22 = myTree.getExpression(line22);

			if (ProofChecker.iAmDebugging){										// print test
				System.out.println("MT E11" + e11);								// print test
				System.out.println("MT E22" + e22);								// print test
				System.out.println(expr);										// print test
			}	



			if (!OgLineNumber.lineArray.contains(line11.toString()) || !OgLineNumber.lineArray.contains(line22.toString())){
				throw new IllegalLineException("Invalid line argument.");
			}

			//Counting "~" signs
			if (!(truthTable.containsKey(e22.toString()) && truthTable.containsKey(e11.toString()))){
				throw new IllegalInferenceException("Referenceing bad expressions");
			}

			int e11Count = e11.toString().length();
			int e22Count = e22.toString().length();

			Expression whole, part;
			if (e11Count > e22Count){
				whole = e11;
				part = e22;
			} else {
				whole = e22;
				part = e11;
			}

			if (logicCheck(whole.eTree.myRight) == logicCheck(part.eTree)) {
				throw new IllegalInferenceException("Expressions aren't inverses.");
			} else if (logicCheck(whole.eTree.myLeft) == logicCheck(ex.eTree)) {
				throw new IllegalInferenceException("Expression not valid.");	
			}

			next = new ProofNode(word, line11, line22, ex, thisLine, currentLevel);
			currentLevel.myChildren.add(next);

			if (expr.equals(currentLevel.myExpr.toString())) {
				if (currentLevel.myParent != null) {
					currentLevel = currentLevel.myParent;
					OgLineNumber.movedUp = true;
				}
			}

			truthTable.put(expr, new Boolean(true));

			break;

		case "co":


			if (splitted.length != 4) {
				throw new IllegalLineException("Wrong number of arguments.");
			}


			//Setting new Expression object based on the first line number input.
			Expression e111 = new Expression();
			LineNumber line111 = new LineNumber(splitted[1]);
			e111 = myTree.getExpression(line111);

			//Setting new Expression object based on the second line number input.			
			Expression e222 = new Expression();
			LineNumber line222 = new LineNumber(splitted[2]);
			e222 = myTree.getExpression(line222);

			if (!OgLineNumber.lineArray.contains(line111.toString()) || !OgLineNumber.lineArray.contains(line222.toString())){
				throw new IllegalLineException("Invalid line argument.");
			}
			if (!truthTable.containsKey(e222.toString()) || !truthTable.containsKey(e111.toString())) {
				throw new IllegalInferenceException("Referencing bad expressions");
			}


			if (ProofChecker.iAmDebugging){										// print test
				System.out.println("E111" + e111.eTree);				// print test
				System.out.println("E222" + e222.eTree);				// print test
				System.out.println(Expression.ExprNode.isInverse(e111.eTree, e222.eTree));				// print test
			}																	// print test

			if (!Expression.ExprNode.isInverse(e111.eTree, e222.eTree)){
				throw new IllegalInferenceException("This contradiction is not valid.");
			}


			truthTable.put(expr, new Boolean(true));
			next = new ProofNode(word, line111, line222, ex, thisLine, currentLevel);
			currentLevel.myChildren.add(next);

			if (ProofChecker.iAmDebugging){										// print test
				System.out.println(currentLevel.myExpr.toString());				// print test
			}																	// print test

			if (expr.equals(currentLevel.myExpr.toString())) {
				if (currentLevel.myParent != null) {
					currentLevel = currentLevel.myParent;
					OgLineNumber.movedUp = true;
				}
			}

			break;

		case "ic":
		case "repeat":

			if (splitted.length != 3) {
				throw new IllegalLineException("Wrong number of arguments.");
			}


			if (input.substring(i,i+1)!=" " ){

				try {
					Integer.parseInt(input.substring(i+1,i+2));
				} catch(NumberFormatException e) {
					throw new IllegalLineException("String following these reasons not correct (integer)");
				}
				//break;
				next = new ProofNode(word, new LineNumber(splitted[1]), ex, thisLine, currentLevel);
				currentLevel.myChildren.add(next);

			} else{
				throw new IllegalLineException("String following these reasons not correct (blank space)");
			}

			// Checking if line number exists.
			LineNumber icLine = new LineNumber(splitted[1]);
			if (!OgLineNumber.lineArray.contains(icLine.toString())){
				throw new IllegalLineException("Invalid line argument.");
			}


			// Checking if line number is valid for ic method.
			int icLineCount = 0; 
			int thisLineCount = 0;

			for (int ii = 0; ii < icLine.toString().length(); ii++){
				if (icLine.toString().charAt(ii) == '.'){
					icLineCount++;
				}
			}

			for (int ii = 0; ii < thisLine.toString().length(); ii++){
				if (thisLine.toString().charAt(ii) == '.'){
					thisLineCount++;
				}
			}


			if (icLineCount > thisLineCount){
				throw new IllegalLineException("Invalid line reference.");
			}


			if (ProofChecker.iAmDebugging){										// print test
				System.out.println(icLine);										// print test
			}																	// print test

			Expression ref = myTree.getExpression(icLine);


			if (ProofChecker.iAmDebugging){										// print test
				System.out.println(ref);										// print test
				System.out.println(ref);										// print test
			}																	// print test

			if (!truthTable.containsKey(ref.toString())) {
				throw new IllegalInferenceException("Invalid expression referenced.");
			}

			if (ex.right().equals(ref.toString())) {
				truthTable.put(expr, new Boolean(true));
			}

			next = new ProofNode(word, icLine, ex, thisLine, currentLevel);
			currentLevel.myChildren.add(next);

			if (expr.equals(currentLevel.myExpr.toString())) {
				if (currentLevel.myParent != null) {
					currentLevel = currentLevel.myParent;
					OgLineNumber.movedUp = true;
				}
			}

			break;			

		default://for theorem input
			if (!myThrms.containsTheorem(word)) {//this depends on TheoremSet
				throw new IllegalLineException("Invalid theorem name.");
			}

			next = new ProofNode(word, ex, thisLine, currentLevel);
			currentLevel.myChildren.add(next);
			truthTable.put(ex.toString(), new Boolean(true));

			if (ProofChecker.iAmDebugging){										// print test
				System.out.println("theorem added");							// print test
				System.out.println(truthTable.containsKey(ex.toString()));		// print test
				System.out.println(ex.toString());								// print test
			}																	// print test

		} 

		if (ProofChecker.iAmDebugging){											// print test
			System.out.println("UPDATE prevLine");								// print test
		}																		// print test

		prevLine = input;
		OgLineNumber.isValid = true;
	}

	public static boolean logicCheck(Expression.ExprNode e) {

		if (e == null){
			return false;

		} else if (truthTable.containsKey(e.toString()) /*&& truthTable.get(e.toString())*/) {
			if (ProofChecker.iAmDebugging){						// print test
				System.out.println("e.toString()");				// print test
			}													// print test
			return true;

		} else if (e.myItem.equals("~")) {
			if (ProofChecker.iAmDebugging){						// print test
				System.out.println("~");						// print test
			}													// print test
			return (!logicCheck(e.myLeft));		

		} else if (e.myItem.equals("|")) {
			if (ProofChecker.iAmDebugging){						// print test
				System.out.println("|");						// print test
			}													// print test
			return (logicCheck(e.myLeft) || logicCheck(e.myRight));
		} else if (e.myItem.equals("&")) {
			if (ProofChecker.iAmDebugging){						// print test
				System.out.println("&");						// print test
			}													// print test
			return (logicCheck(e.myLeft) && logicCheck(e.myRight));
		} else if (e.myItem.equals("=>")) {
			if (ProofChecker.iAmDebugging){						// print test
				System.out.println("=>");						// print test
			}													// print test
			return (!(logicCheck(e.myLeft) && !logicCheck(e.myRight)));

		} else {
			return false;
		}
	}

	public String toString ( ) {
		String rtn = stringHelper(myTree);
		return rtn;
	}

	private static String stringHelper(ProofNode p) {

		//String toRtn = p.toString();
		String toRtn = "";
		if (ProofChecker.iAmDebugging){											// print test
			System.out.println("current toPrint node:" + toRtn);				// print test
		}																		// print test



		for (ProofNode child : p.myChildren) {
			toRtn = toRtn + stringHelper(child);
		}


		if (ProofChecker.iAmDebugging){											// print test
			System.out.println("final return:" + toRtn);						// print test
		}																		// print test


		toRtn = p.toString() + toRtn;

		return toRtn;
	}

	public boolean isComplete ( ) {


		String line1 = this.lineNumber.toString();

		if (!line1.contains(".")){

			if (prevLine.contains("co") || prevLine.contains("ic")  || prevLine.contains("repeat")){
				return logicCheck(this.myTree.expr().eTree);	
			} 

		} return false;	

	}

	public static class ProofNode {

		public String myReason; // ProofNode's reason
		public LineNumber lineArg1;//first line number argument
		public LineNumber lineArg2;//second line number argument
		public Expression myExpr;//expression associated with this node

		public LineNumber myLine;//line number for this line

		public ProofNode myParent; // ProofNode's parent
		public ArrayList<ProofNode> myChildren; // ProofNode's children

		/* Constructor for initial show. */
		public ProofNode(String reason, Expression expr, LineNumber line) {
			myReason = reason;
			lineArg1 = lineArg2 = null;
			myExpr = expr;
			myLine = line;
			myParent = null;
			myChildren = new ArrayList<ProofNode>();
		}

		/* Constructor for reasons without LineNumber arguments. */
		public ProofNode(String reason, Expression expr, LineNumber line, ProofNode parent) {
			myReason = reason;
			lineArg1 = lineArg2 = null;
			myExpr = expr;
			myLine = line;
			myParent = parent;
			myChildren = new ArrayList<ProofNode>();
		}

		/* Constructor for reasons with one LineNumber argument. */
		public ProofNode(String reason, LineNumber line1, Expression expr, LineNumber line, ProofNode parent) {
			myReason = reason;
			lineArg1 = line1;
			lineArg2 = null;
			myExpr = expr;
			myLine = line;
			myParent = parent;
			myChildren = new ArrayList<ProofNode>();
		}

		/* Constructor for reasons with two LineNumber argument. */
		public ProofNode(String reason, LineNumber line1, LineNumber line2, Expression expr, LineNumber line, ProofNode parent) {
			myReason = reason;
			lineArg1 = line1;
			lineArg2 = line2;
			myExpr = expr;
			myLine = line;
			myParent = parent;
			myChildren = new ArrayList<ProofNode>();
		}

		public String toString() {
			String rtn = myLine.toString() + "\t";
			rtn = rtn + myReason + " ";
			if (lineArg1 != null) {
				rtn = rtn + lineArg1.toString() + " ";
			}
			if (lineArg2 != null) {
				rtn = rtn + lineArg2.toString() + " ";
			}
			rtn = rtn + myExpr.toString();
			return rtn + "\n";
		}

		public Expression getExpression(LineNumber ln) {
			if (this.myLine.equals(ln)) {
				return this.myExpr;
			} else if (this.myChildren.isEmpty()) {
				return null;
			} else {
				for (ProofNode child : this.myChildren) {
					Expression found = child.getExpression(ln);
					if (found == null) {
						continue;
					} else {
						return found;
					}
				}
			}
			System.out.println("final null");
			return null;
		}

		public String reason(){
			return myReason;
		}

		public LineNumber getLine1(){
			return lineArg1;
		}

		public LineNumber getLine2(){
			return lineArg2;
		}

		public Expression expr(){
			return myExpr;
		}

		public LineNumber line(){
			return myLine;
		}

		public ProofNode parent() {
			return myParent;
		}

	}
}

