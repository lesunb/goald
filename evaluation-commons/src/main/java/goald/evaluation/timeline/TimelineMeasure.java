package goald.evaluation.timeline;

public class TimelineMeasure {
	
	String label;
	Long start;
	Long end;
	
	public TimelineMeasure(String label, Long start, Long end){
		this.label = label;
		this.start = start;
		this.end = end;
	}

	public String getLabel(){
		return this.label;
	}
	
	public Long getStart(){
		return this.start;
	}
	
	public Long getEnd(){
		return this.end;
	}
	
	@Override
	public String toString() {
		return "[" + label + " from:" + start + ", to " + end + "]";
	}
}
