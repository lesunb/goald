package goald.monitoring;

import goald.model.ContextChange;
import goald.model.ContextChange.OP;

public class CtxMonitorBuilder {

	private CtxMonitor built;
	
	public static CtxMonitorBuilder create() {
		
		return new CtxMonitorBuilder();
	}
	
	private CtxMonitorBuilder() {
		this.built = new CtxMonitor();
	}

	public CtxMonitor build() {
		return built;
	}

	public CtxMonitorBuilder add(Long time, String context) {
		built.addToChanges(new ContextChange(OP.ADDED, context, time));
		return this;
	}
	
	public CtxMonitorBuilder remove(Long time, String context) {
		built.addToChanges(new ContextChange(OP.REMOVED, context, time));
		return this;
	}
}
