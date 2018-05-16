package goalp.evaluation.model;

public class Execution {

	private ExecSpec spec;

	private EvaluationComponent evaluation;
	
	public ExecSpec getSpecification() {
		return spec;
	}
	
	public void setSpecification(ExecSpec spec){
		this.spec = spec;
	}

	public EvaluationComponent getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(EvaluationComponent evaluation) {
		this.evaluation = evaluation;
	}
}