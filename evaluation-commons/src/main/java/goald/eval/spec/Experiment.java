package goald.eval.spec;

import java.util.ArrayList;
import java.util.List;

import goald.eval.exec.Execution;
import goald.evaluation.response.ResponseEvaluation;

public class Experiment {
	
	public String name;
	
	private ResponseEvaluation evaluation;
		
	private List<Execution> executions;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Execution> getExecutions() {
		if(executions == null){
			executions = new ArrayList<Execution>();
		}
		return executions;
	}
	

	public void toExecute(Execution execution) {
		getExecutions().add(execution);
	}

	public ResponseEvaluation getEvaluation() {
		if(evaluation == null){
			evaluation = new ResponseEvaluation();
		}
		return evaluation;
	}

	public void setEvaluation(ResponseEvaluation evaluation) {
		this.evaluation = evaluation;
	}

	

}
