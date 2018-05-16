package goalp.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.weld.exceptions.IllegalStateException;
import org.slf4j.Logger;

import goalp.exputil.ExperimentTimer;

@Singleton
@Default
public class ExperimentTimerImpl implements ExperimentTimer {

	@Inject
	Logger log;
	
	List<Split> measures;
	
	Long startTime;
	
	public class Split {

		String name;
		Long duration;
		
		public Split(String name, Long duration){
			this.name = name;
			this.duration = duration;
		}
		
		public String getLabel(){
			return this.name;
		}
		
		public Long getDuration(){
			return this.duration;
		}
		
		@Override
		public String toString() {
			return "Split [name=" + name + ", duration=" + duration + " ns, " + 
					 TimeUnit.NANOSECONDS.toMillis(duration) +"ms]";
		}		
	}
	
	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#begin()
	 */
	@Override
	public void begin() {
		// TODO Auto-generated method stub
		log.trace("iniating timer");
		measures = new ArrayList<>();
		startTime = System.nanoTime();
	}

	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#split(java.lang.String)
	 */
	@Override
	public Number split(String label) {
		Long newTime = System.nanoTime();
		// TODO Auto-generated method stub
		if(startTime == null){
			throw new IllegalStateException("Caller tried to split non initialized timer!");
		}
		
		Long duration = newTime - startTime;
		Split split = new Split(label, duration);
		log.trace(split.toString());
		measures.add(split);
		startTime = System.nanoTime(); // start the timer againg
		return duration;
	}

	/* (non-Javadoc)
	 * @see goalp.evaluation.ExperimentTimer#finish()
	 */
	@Override
	public void finish() {
		log.trace("finish timer");
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
