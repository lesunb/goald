package goald.evaluation;

public class Measure {
	
	String label;
	String value;
	
	public Measure(String label, Long duration){
		this.label = label;
		this.value = Long.toString(duration);
	}

	public String getLabel(){
		return this.label;
	}
	
	public String getValue(){
		return this.value;
	}	
}
