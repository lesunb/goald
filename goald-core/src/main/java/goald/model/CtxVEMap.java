package goald.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CtxVEMap {
	
	Map<String, Set<VE>> ctxDameMap;
	
	public CtxVEMap() {
		this.ctxDameMap = new HashMap<>();
	}
	
	public void add(List<ContextCondition> ctxs, VE dame) {
		for(ContextCondition ctx:ctxs) {
			add(ctx.getLabel(), dame);
		}
	}
	
	public void add(String ctx, VE dame) {
		Set<VE> dames = this.ctxDameMap.get(ctx);
		if( dames == null) {
			Set<VE> newSet = new HashSet<>();
			this.ctxDameMap.put(ctx, newSet);
			dames = newSet;
		}
		dames.add(dame);
	}
	
	public Set<VE> get(String ctx) {
		return this.ctxDameMap.get(ctx);
	}

	@Override
	public String toString() {
		return "CtxDameMap [ctxDameMap=" + ctxDameMap + "]";
	}
}
