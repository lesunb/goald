package goald.eval.exec;

import goald.evaluation.Dataset;
import goald.evaluation.response.SplitTimer;

public class ExecResult {
	
	Dataset ds;
	
	SplitTimer timer;
	
	private static ExecResult instance;
	
	private ExecResult(){
		
	}

	public static ExecResult create() {
		instance = new ExecResult();
		instance.ds = Dataset.create();
		instance.timer = SplitTimer.create();
		instance.timer.begin();
		return instance;
	}
	
	public void split(int execIndex, String label) {
		ds.log(execIndex, label, timer.split(label));
	}
}
