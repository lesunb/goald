package goald.eval.exec;

import goald.evaluation.Dataset;
import goald.evaluation.ClockTimer;

public class ExecResult {
	
	Dataset ds;
	
	ClockTimer timer;
	
	private static ExecResult instance;
	
	private ExecResult(){
		
	}

	public static ExecResult create() {
		instance = new ExecResult();
		instance.ds = Dataset.create();
		instance.timer = ClockTimer.create();
		instance.timer.begin();
		return instance;
	}
	
	public void split(int execIndex, String label) {
		ds.log(execIndex, label, timer.split(label));
	}
}
