public class LineNumber {

	String nonStaticLineNumber;


	public LineNumber(String b){
		nonStaticLineNumber = b;
	}
	public String toString(){
		return nonStaticLineNumber;
	}
	
	public boolean equals(Object ln){
		return (ln.toString().equals(this.toString())); 
	}
	
}
