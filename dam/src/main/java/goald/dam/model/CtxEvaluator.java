package goald.dam.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import goald.dam.model.ContextChange.OP;

public class CtxEvaluator {
	
	Map<String, ContextCondition> ctxMap;
	
	Collection<ContextCondition> actualCtx;
	
	public CtxEvaluator(Collection<ContextCondition> actualCtx) {
		this.actualCtx = actualCtx;
		this.ctxMap = new HashMap<>();
		for(ContextCondition ctx: actualCtx) {
			ctxMap.put(ctx.getLabel(), ctx);
		}
	}

	public boolean check(List<ContextCondition> ctxReqs) {
		for(ContextCondition ctx:ctxReqs) {
			if(this.ctxMap.get(ctx.getLabel()) == null ) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Update context
	 * @param change
	 * @return true if context was changed
	 */
	public boolean update(ContextChange change) {
		String label = change.getLabel();
		if(change.getOp() == OP.REMOVED) {
			ContextCondition ctx = ctxMap.get(label);
			if(ctx == null) { return false; }
			ctxMap.remove(label);
			actualCtx.remove(ctx);
			return true;
		}else { // context added
			boolean IsNewCtx = this.ctxMap.get(label) == null;
			if(IsNewCtx) {
				ContextCondition newCtx = new ContextCondition(label);
				actualCtx.add(newCtx);
				ctxMap.put(label, newCtx);
			}
			return IsNewCtx;
		}
	}
}
