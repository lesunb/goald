package goald.dam.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CtxDameMap {
	
	Map<String, Set<Dame>> ctxDameMap;
	
	public CtxDameMap() {
		this.ctxDameMap = new HashMap<>();
	}
	
	public void add(List<ContextCondition> ctxs, Dame dame) {
		for(ContextCondition ctx:ctxs) {
			add(ctx.getLabel(), dame);
		}
	}
	
	public void add(String ctx, Dame dame) {
		Set<Dame> dames = this.ctxDameMap.get(ctx);
		if( dames == null) {
			Set<Dame> newSet = new HashSet<>();
			this.ctxDameMap.put(ctx, newSet);
			dames = newSet;
		}
		dames.add(dame);
	}
	
	public Set<Dame> get(String ctx) {
		return this.ctxDameMap.get(ctx);
	}
}
