package goald.model.util;

import java.util.ArrayList;
import java.util.List;

import goald.model.ContextCondition;
import goald.model.CtxEvaluator;

public class CtxEvaluatorBuilder {
	
	protected CtxEvaluator evaluator;
	
	protected List<ContextCondition> ctx;
	
	protected CtxEvaluatorBuilder(){
		this.ctx = new ArrayList<>();
	}
	
	public static CtxEvaluatorBuilder create(){
		return new CtxEvaluatorBuilder();
	}
	
	public CtxEvaluator build(){
		CtxEvaluator built = new CtxEvaluator(ctx);
		this.ctx = null;
		return built;
	}

	public CtxEvaluatorBuilder with(String identification){
		this.ctx.add(new ContextCondition(identification));
		return this;
	}
	
	public CtxEvaluatorBuilder with(String ...identifications){
		for(String identification: identifications) {
			this.ctx.add(new ContextCondition(identification));	
		}
		return this;
	}

	public CtxEvaluatorBuilder with(List<String> ctxs) {
		for(String identification: ctxs) {
			this.ctx.add(new ContextCondition(identification));	
		}
		return this;
	}
}
