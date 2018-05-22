package goald.dam.planning;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import goald.dam.model.ContextCondition;

public class CtxEvaluator {
	
	Map<String, ContextCondition> ctxMap;
	
	public CtxEvaluator(Collection<ContextCondition> actualCtx) {
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
}
