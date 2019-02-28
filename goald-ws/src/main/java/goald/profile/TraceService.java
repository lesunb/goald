package goald.profile;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class TraceService {

	private List<Trace> traces = new ArrayList<>();
	
	private int executionIndex = 0;
	
	public void succes(String service){
		traces.add(new Trace(service, "success"));
	}
	
	public void error(String service){
		traces.add(new Trace(service, "error"));
	}
	
	public void startNewExectution() {
		executionIndex++;
	}
	
	public List<Trace> getTraces() {
		return traces;
	}
	
	public class Trace {
		public String call;
		
		public String status;
		
		public int execution;
		
		public Trace(String call, String status) {
			this.call = call;
			this.status = status;
			this.execution = executionIndex;
		}
		
		public String toString() {
			return "[" + execution + ":"+ call + ":" + status + "]\n";
		}
	}
}
