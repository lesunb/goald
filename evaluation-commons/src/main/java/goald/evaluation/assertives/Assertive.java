package goald.evaluation.assertives;

public class Assertive {

	public Propositions premise;
	public Propositions conclusion;
	
	
	public Assertive(Propositions premise, Propositions conclusion) {
		this.premise = premise;
		this.conclusion = conclusion;
	}


	@Override
	public String toString() {
		return premise + " -> " + conclusion;
	}
	
}
