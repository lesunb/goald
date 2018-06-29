package goald.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.jboss.weld.exceptions.IllegalStateException;

import goald.exputil.ExperimentTimer;

public class Timer implements ExperimentTimer {
	
	List<Split> measures;
	
	Long startTime;
	
	
	public static Timer create() {
		return new Timer();
	}
	
	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#begin()
	 */
	@Override
	public void begin() {
		measures = new ArrayList<>();
		startTime = System.nanoTime();
	}

	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#split(java.lang.String)
	 */
	@Override
	public Long split(String label) {
		Long newTime = System.nanoTime();
		// TODO Auto-generated method stub
		if(startTime == null){
			throw new IllegalStateException("Caller tried to split non initialized timer!");
		}
		
		Long duration = newTime - startTime;
		Split split = new Split(label, duration);
		measures.add(split);
		startTime = System.nanoTime(); // start the timer againg
		return duration;
	}

	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#finish()
	 */
	@Override
	public void finish() {
		startTime = null;
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#result()
	 */
	@Override
	public List<Split> result() {
		List<Split> result = this.measures;
		this.measures = null;
		return result;
	}

}
