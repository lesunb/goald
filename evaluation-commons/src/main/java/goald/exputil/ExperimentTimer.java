package goald.exputil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface ExperimentTimer {

	public class Split {

		String name;
		Long duration;
		Map<String, String> otherLabels;
		
		public Split(String name, Long duration){
			this.name = name;
			this.duration = duration;
		}
		
		public Split(String name, Long duration, Map<String, String> otherLabels) {
			this.name = name;
			this.duration = duration;
			this.otherLabels = otherLabels;
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
	
	void begin();

	Long split(String string);

	void finish();

	List<Split> result();

}