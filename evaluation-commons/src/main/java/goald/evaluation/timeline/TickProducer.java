package goald.evaluation.timeline;

import java.util.Iterator;

public class TickProducer implements Iterable<Long>{

	Long tick, end, current, start;
	
	public static TickProducer create(Long tick, Long start, Long end) {
		return new TickProducer(tick, start, end);
	}
	
	private TickProducer(Long tick, Long start, Long end) {
		this.tick = tick;
		this.end = end;
		this.start = start;
		this.reset();
		
	}
	
	@Override
	public Iterator<Long> iterator() {
		return new Iterator<Long>() {
			@Override
			public boolean hasNext() {
				return current <= end;
			}
			@Override
			public Long next() {
				long return_ = current;
				current += tick;
				return return_;
			}
		};
	}

	public void reset() {
		this.current = start;
	}
}
