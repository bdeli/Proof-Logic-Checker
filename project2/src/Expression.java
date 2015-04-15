import java.util.*;


public class Expression {

	public ArrayList<String> valid = new ArrayList<String>();
	public ExprNode eTree;
	//public ExprNode myRoot;

	public Expression() {
		eTree = null;
	}

	public Expression (String s) throws IllegalLineException {

		if (s.equals("")) {
			throw new IllegalLineException("No expression input.");
		}

		/* Syntax checks for expressions. Does not account for line number arguments. */
		String[] expr = new String[s.length()];
		for (int k = 0; k < s.length(); k += 1) {
			expr[k] = s.substring(k, k + 1);
		}

		String[] valChars = {"(",")","~", "&", "|", "=", ">"};
		for (String str : valChars) {
			valid.add(str);
		}

		/* Check for valid characters. */
		for (int ind = 0; ind < expr.length; ind += 1) {
			if ( !(valid.contains(expr[ind])) ) {
				if ( !(Character.isLowerCase( s.charAt(ind) )) ) {
					throw new IllegalLineException("Invalid input characters.");
				}
			}
		}

		/* Check parens match. */
		int parenCount = 0;
		for (int indx = 0; indx < expr.length; indx += 1) {
			if (expr[indx].equals("(")) {
				parenCount += 1;
			} else if (expr[indx].equals(")")) {
				parenCount -= 1;
			}
		}
		if (parenCount != 0) {
			throw new IllegalLineException("Invalid parentheses formatting.");
		}

		try{
			eTree = exprTree(s);
		} catch (IllegalLineException ile) {
			throw new IllegalLineException(ile.getMessage());
		}
	}

	public String toString(){
		return eTree.toString();
	}

	public String left(){
		return eTree.myLeft.toString();
	}

	public String right(){
		return eTree.myRight.toString();
	}


	public Expression (ExprNode t) {
		eTree = t;
	}

	public static ExprNode exprTree(String s) throws IllegalLineException {
		ExprNode result = new ExprNode();
		try {
			result = exprTreeHelper(s);
		} catch (IllegalLineException e) {
			throw new IllegalLineException(e.getMessage());
		}
		return result;
	}

	private static ExprNode exprTreeHelper(String expr) throws IllegalLineException {
		if (expr == null) {
			return new ExprNode();
		}
		if (expr.charAt(0) != '('){
			if (expr.charAt(0) == '~') {
				ExprNode simpleT = new ExprNode(expr.substring(0, 1));
				simpleT.myLeft = exprTreeHelper(expr.substring(1));
				return simpleT;
			} else if (!Character.isLowerCase(expr.charAt(0))) {
				throw new IllegalLineException("Variables must be a lowercase character.");
			} else if (expr.length() != 1) {
				throw new IllegalLineException("Invalid variable format.");
			}
			return new ExprNode(expr.substring(0));
		}else{
			if (expr.charAt(expr.length() - 1) != ')') {
				throw new IllegalLineException("Invalid parentheses.");
			}
			int nesting = 0;
			int opPosOne = 0;
			int opPosTwo = 0;
			for(int k =1; k < expr.length()-1; k++){
				//System.out.println("k = " + k);
				if (expr.charAt(k)=='('){
					nesting ++;
				}else if (expr.charAt(k)== ')'){
					nesting--;
				}else if (nesting == 0) {
					if((expr.charAt(k)=='&' ||expr.charAt(k)=='|')){
						opPosOne = k;
						opPosTwo = k;
						break;
					}else if (expr.charAt(k) == '=') {
						if (expr.charAt(k+1) != '>') {
							throw new IllegalLineException("Invalid operator.");
						}
						opPosOne = k;
						opPosTwo = k + 1;
						break;
					}else if ((expr.charAt(k)) == '~') {
						if (expr.charAt(k - 1) == '(' && expr.charAt(k + 2) == ')') {
							throw new IllegalLineException("No parens needed.");
						}
						//ExprNode t = new ExprNode(expr.substring(k, k + 1));
						//t.myLeft = exprTreeHelper(expr.substring(k+1, expr.length()-1));
						//return t;
					}else if (Character.isLowerCase(expr.charAt(k))) {
						if (expr.charAt(k - 1) == '(' && expr.charAt(k + 1) == ')') {
							throw new IllegalLineException("No parens needed.");
						}
						//return new ExprNode(expr.substring(k, k+1));
					}
				}else{
					continue;
				}
			}
			String opnd1 = expr.substring(1, opPosOne);
			String opnd2 = expr.substring(opPosTwo+1, expr.length()-1);
			String op = expr.substring(opPosOne, opPosTwo+1);
			ExprNode rtn = new ExprNode();
			try {
				rtn = new ExprNode(op);
			} catch (IllegalLineException ln) {}//may come back to this
			rtn.myLeft = exprTreeHelper(opnd1);
			rtn.myRight = exprTreeHelper(opnd2);
			return rtn;

		}
	}

	public int countNot(){		//counts # of ~ in an expression

		return eTree.countNotNode();
	}


	public static class ExprNode {

		public Object myItem;
		public ExprNode myLeft;
		public ExprNode myRight;

		public ExprNode() throws IllegalLineException {
			myItem = null;
			myLeft = myRight = null;
		}

		public ExprNode (Object obj) throws IllegalLineException {
			myItem = obj;
			myLeft = myRight = null;
		}

		public ExprNode (Object obj, ExprNode left, ExprNode right) throws IllegalLineException {
			myItem = obj;
			myLeft = left;
			myRight = right;
		}

		public static boolean isInverse(ExprNode a, ExprNode b){
			int aNotCount = a.countNotNode();
			int bNotCount = b.countNotNode();
			if (aNotCount==bNotCount){
				return false;

			}

			String aString = a.toString().substring(aNotCount); 
			String bString = b.toString().substring(bNotCount);
			if (!aString.equals(bString)) {
				return false;
			}

			if ((bNotCount % 2 == 0 && aNotCount % 2 == 1) || (bNotCount % 2 == 1 && aNotCount % 2 == 0) ){
				return true;
			}

			/*			
			else{
				int aLength = a.toString().length();
				int bLength = b.toString().length();
				String bigString, smallString;

				if (aLength>bLength){
					bigString = a.toString().substring(aNotCount-(aNotCount % 2));
					smallString  = b.toString().substring(bNotCount-(bNotCount % 2));
				}else{
					bigString  = b.toString().substring(bNotCount-(bNotCount % 2));
					smallString = a.toString().substring(aNotCount-(aNotCount % 2));
				}
				if (bigString.equals("~" + smallString)) {
					return true;
				}
			}*/

			return false;
		}

		public String toString(){
			if (this.myLeft == null && this.myRight == null){
				return this.myItem.toString();
			} else {

				String rtn = "";
				if (this.myItem.toString().equals("~")) {
					rtn = rtn + this.myItem.toString();
					rtn = rtn + this.myLeft.toString();
				} else {
					rtn = rtn + "(";
					rtn = rtn + myLeft.toString();
					rtn = rtn + myItem.toString();
					rtn = rtn + myRight.toString();
					rtn = rtn + ")";

					if (ProofChecker.iAmDebugging){					// print test
						System.out.println("rtn: " + rtn);				// print test
					}												// print test
				}return rtn;
			}

		}

		public int countNotNode(){		//counts # of ~ in an expression
			int notCounter = 0;
			String eString  = this.toString();

			for(int k = 0; k<eString.length(); k++){
				if ( eString.charAt(k)=='~'){
					notCounter++;
				} else{
					break;
				}
			}	return notCounter;
		}
	}

	public boolean equals(Expression comp) {
		String toCheck = comp.toString();
		if (!this.toString().equals(toCheck)) {
			return false;
		} else {
			return true;
		}
	}


}