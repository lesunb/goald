package goald.evaluation.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Measure {

	
	Map<String, String> labels;
	String value;
	
	public Measure(String label, Long duration){
		this.labels = new HashMap<>();
		labels.put("label", label);
		this.value = Long.toString(duration);
	}
	
	public Measure(String name, Long duration, Map<String, String> otherLabels) {
//		this.name = name;
//		this.duration = duration;
//		this.otherLabels = otherLabels;
	}

//	public String getLabel(){
//		return this.name;
//	}
//	
//	public Long getDuration(){
//		return this.duration;
//	}
	
//	@Override
//	public String toString() {
//		return "Split [name=" + name + ", duration=" + duration + " ns, " + 
//				 TimeUnit.NANOSECONDS.toMillis(duration) +"ms]";
//	}		
}
