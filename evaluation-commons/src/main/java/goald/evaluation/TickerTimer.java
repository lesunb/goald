package goald.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.jboss.weld.exceptions.IllegalStateException;

import goald.exputil.ExperimentTimer;
import goalp.eval.qualifier.TimelineBased;

@TimelineBased
public class TickerTimer implements ExperimentTimer {
	 
	List<Split> measures;
	
	Long startClockTime;
	
	/* used to fast forward the time stamp ahead*/
	Long baseTime = 0l;
	
	public static TickerTimer create() {
		return new TickerTimer();
	}
	
	private TickerTimer() { }
	
	public void forwardTimerTo(Long baseTime) {
		this.baseTime = baseTime;
	}
	
	public ExperimentTimer clone() {
		TickerTimer timer = new TickerTimer();
		timer.startClockTime = this.startClockTime;
		timer.baseTime = this.baseTime;
		
		return timer;
	}
	
	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#begin()
	 */
	@Override
	public void begin() {
		measures = new ArrayList<>();
		startClockTime = System.nanoTime();
	}
	
	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#split(java.lang.String)
	 */
	@Override
	public Long split(String label) {
		if(startClockTime == null){
			throw new IllegalStateException("Caller tried to split non initialized timer!");
		}
		Long newTime = System.nanoTime();
		/* measured clock time + base time */
		Long timestamp = (newTime - startClockTime) + baseTime;
		
		Long duration = newTime - startClockTime;
		Split split = new Split(label, duration, timestamp);
		measures.add(split);
		startClockTime = System.nanoTime(); // start the timer againg
		return duration;
	}
	
	public long getTimestamp() {
		Long newTime = System.nanoTime();
		/* measured clock time + base time */
		return (newTime - startClockTime) + baseTime;
	}

	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#finish()
	 */
	@Override
	public void finish() {
		startClockTime = null;
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
