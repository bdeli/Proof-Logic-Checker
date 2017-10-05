import java.util.ArrayList;

public class OgLineNumber {
	static int showCount;
	static Integer printCount;	//to keep track of the numbers
	static String printCountS;	//to make #.#.# format 
	static String copy;
	static int decimalCount;
	//static LineNumber a;
	static boolean	isValid;
	public static ArrayList<String> lineArray;
	public static boolean movedUp;

	public OgLineNumber(){
		showCount = 0;
		decimalCount  =  0;
		//printCount = 1;
		printCountS = "0";
		isValid = true;
		lineArray = new ArrayList<String>();
	}

	public static void currentLine(String prev){ //prev is the prev expression
		if(prev==null){
			increase();
			lineArray.add(printCountS);
		}
		else if (isValid==true){
			if (prev.contains("show") && showCount==0){	
				//System.out.println("before addDecimal and show0 " + printCountS);
				increase();
				//System.out.println("after addDecimal and show0 " + printCountS);
				showCount++;
				lineArray.add(printCountS);
				//System.out.println(printCountS);	//need to be removed
			}
			else if (prev.contains("show")==false && showCount<2){
				//System.out.println("before addDecimal and show1 " + printCountS);
				increase();
				lineArray.add(printCountS);
			}
			else if(prev.contains("show") && showCount==1){
				//System.out.println("before addDecimal " + printCountS);	//TO DO: delete later
				addDecimal();
				showCount++;
				lineArray.add(printCountS);
			}
			else if (prev.contains("ic") && decimalCount==0){
				//printCount++;
				increase();
				lineArray.add(printCountS);
			}
			else if(showCount>=2){
				if(prev.contains("show")){
					//System.out.println("before addDecimal " + printCountS);	//TO DO: delete later
					addDecimal();
					lineArray.add(printCountS);
				}
				//else if(prev.contains("co") && prev.contains(copy)){
				else if(prev.contains("co")){
					/*String split3 = prev.split(" ")[3];
					String currExpr = Proof.currentLevel.myExpr.toString();
					if (ProofChecker.iAmDebugging){										// print test
						System.out.println("Line exp(co): " + split3);					// print test
					}																	// print test
					if (ProofChecker.iAmDebugging){										// print test
						System.out.println("Line currExpr(co): " + currExpr);			// print test
					}*/																	// print test
					if (movedUp) {
						removeDecimal();
					} else {
						increase();
					}
					lineArray.add(printCountS);
					//System.out.println("remove works");	needs to be removed
				}
				else if(prev.contains("ic")){
					/*String split2 = prev.split(" ")[2];
					String currExpr = Proof.currentLevel.myExpr.toString();
					if (ProofChecker.iAmDebugging){										// print test
						System.out.println("Line exp(ic): " + split2);					// print test
					}																	// print test
					if (ProofChecker.iAmDebugging){										// print test
						System.out.println("Line currExpr(ic): " + currExpr);			// print test
					}*/																	// print test
					if (movedUp) {
						removeDecimal();
					} else {
						increase();
					}
					lineArray.add(printCountS);
				}
				else if(prev.contains("mp") || prev.contains("mt")|| prev.contains("mq") || prev.contains("assume") || prev.contains("repeat")){
					if (movedUp) {
						removeDecimal();
					} else {
					increase();
					}
					lineArray.add(printCountS);
				}
				else if(Proof.myThrms.containsTheorem(prev.split(" ")[0])){//works??
					increase();
					lineArray.add(printCountS);
				}
			}
		}
	}
	public static void addDecimal(){ //after is the string that comes after the line number(show, assume, etc)
		int afterDecimal = 1;
		decimalCount++;
		printCountS = printCountS+"."+ afterDecimal;
		//System.out.println("add works " + printCountS);	//needs to be removed
	}
	public static void removeDecimal(){
		//System.out.println("remove works");	needs to be removed
		decimalCount--;
		int stringLength = printCountS.length();
		if (stringLength==3){
			printCountS = "" + printCountS.charAt(0);
			increase();
		}
		else{
			printCountS = printCountS.substring(0, stringLength-2);
			increase();
			//System.out.println("remove works");	needs to be removed
		}
	}
	public static void increase(){
		//System.out.println("increase works");	//needs to be removed
		Integer length = printCountS.length();
		if (length.equals(1)){
			//System.out.println("gets to 1");	//should be deleted
			String printSubString = "" + printCountS.charAt(0);
			Integer updated = Integer.parseInt(printSubString);
			updated++;
			printCountS = "" + updated;
		}
		else{
			String printSubString = "" + printCountS.charAt(length-1);
			int updated = Integer.parseInt(printSubString);
			updated++;
			printCountS = printCountS.substring(0,length-1) + updated;

		}
	}

	public String toString(){
		return printCountS;
	}
}
