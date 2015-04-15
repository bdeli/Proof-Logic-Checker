import java.util.*;

/*public class TheoremSet {

public HashMap<String, Expression> thrms;//date structure for storing theorems
public TheoremSet ( ) {
}

public Expression put (String s, Expression e) {
thrms.put(s, e);
return e;
}
public Expression get(String key){
return thrms.get(key);
}*/

public class TheoremSet {
	// Uses the default constructor that takes no arguments

	// Initializes the instance variable thrms to be a TreeMap
	private TreeMap<String, Expression> thrms = new TreeMap<String, Expression>();
	/*
	 * Adds a theorem with its name to the set and associates it with
	 * with the expression
	 */
	public void put (String theoremToAdd, Expression expressionToAdd){
		thrms.put(theoremToAdd, expressionToAdd);
	}

	public Expression get(String theoremToGet){


		if (thrms.get(theoremToGet) == null) {
			throw new IllegalArgumentException("The theorem " + theoremToGet + " was not found in this PhoneBook");
		}
		return thrms.get(theoremToGet);
	}

	public boolean containsTheorem(String thrmName) {
		return thrms.containsKey(thrmName);
	}
}