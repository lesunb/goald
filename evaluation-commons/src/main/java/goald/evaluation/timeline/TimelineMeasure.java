package goald.evaluation.timeline;

public class TimelineMeasure {
	
	String label;
	Long start;
	Long end;
	String type; //context, bundle, lifecycle...
	private int index;
	
	public TimelineMeasure(String label, Long start, Long end){
		this.label = label;
		this.start = start;
		this.end = end;
	}
	
	public TimelineMeasure(String label, String type, Long start, Long end){
		this.label = label;
		this.type = type;
		this.start = start;
		this.end = end;
	}

	public String getLabel(){
		return this.label;
	}
	
	public String getType(){
		return this.type;
	}
	
	public Long getStart(){
		return this.start;
	}
	
	public Long getEnd(){
		return this.end;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public int compareTo(TimelineMeasure other) {
		if( (this.start == null && other.start == null) 
				||  this.start == other.start) {
			return 0;
		}else if(this.start == null || this.start < other.start) {
			return -1;
		}else if(other.start == null || this.start > other.start) {
			return 1;
		}else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		return "[" + label + " from:" + start + ", to " + end + "]";
	}
}
