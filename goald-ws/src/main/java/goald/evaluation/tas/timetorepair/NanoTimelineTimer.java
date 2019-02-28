package goald.evaluation.tas.timetorepair;

import goald.evaluation.timeline.TimelineTimer;

//@TimelineBased
public class NanoTimelineTimer extends TimelineTimer {
	
	public static NanoTimelineTimer create() {
		return new NanoTimelineTimer();
	}
	
	protected long currentTime() {
		return System.nanoTime();
	}

}
