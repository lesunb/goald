package goalp.exputil;

import java.util.List;

import goalp.evaluation.model.Experiment;

public class EvalUtil {
	

	public static List<String> getFactors(Experiment exp){
		return exp.getEvaluation().getFactorList();
	}
	
	public static String getOneFactor(Experiment exp){
		if(exp.getEvaluation().getFactorList().size() != 1){
			throw new IllegalStateException("should not call if has not exacly one factor");
		}
		return exp.getEvaluation().getFactorList().get(0);
	}
	
	public static String getFactor(Experiment exp, int factor){
		return exp.getEvaluation().getFactorList().get(factor);
	}
	
	public static String getResponseVariable(Experiment exp){
		return exp.getEvaluation().getResponseVariable();
	}
}
