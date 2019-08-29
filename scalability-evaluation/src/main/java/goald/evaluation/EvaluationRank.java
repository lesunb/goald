package goald.evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import goald.evaluation.exec.ExecuteExperiment.SetupAndEvaluation;

public class EvaluationRank {

	private int size;
	
	private Map<String, List<SetupAndEvaluation>> ranks = new HashMap<>();
	
	private String[]  factors, measures;
	
	public EvaluationRank(int size, String[] factors, String[] measures) {
		this.measures = measures;
		this.factors = factors;
		this.size = size;
	}
	
	public void rankEvaluation(SetupAndEvaluation se) {
		for(String factor: this.factors) {
			for(String measure: this.measures) {
				rankEvaluation(factor, measure, se);	
			}
		}
	}
	
	public void rankEvaluation(String factor, String measure, SetupAndEvaluation se) {
		Number seFactor = se.getEvaluation().getFactors().get(factor);
		String key = seFactor + ":" + measure;
		
		List<SetupAndEvaluation> rank = ranks.get(key);
		
		if(rank == null ) {
			rank = new ArrayList<SetupAndEvaluation>();
			ranks.put(key, rank);
		} 
	

		if(rank.size() < size) {
			rank.add(se);
		}else {
			Long last = rank.get(size-1).getMeasures().get(measure);
			
			if(last < se.getMeasures().get(measure)) {
				rank.remove(size-1);
				rank.add(se);
			}
		}
		rank.sort(  (a, b) -> 
		(int) (b.getMeasures().get(measure) - a.getMeasures().get(measure)));

	}	
	
	
	public Collection<List<SetupAndEvaluation>> getTopRanked() {
		return this.ranks.values();
	}
	
	public Map<String, List<SetupAndEvaluation>>  getRanks() {
		return this.ranks;
	}

}
