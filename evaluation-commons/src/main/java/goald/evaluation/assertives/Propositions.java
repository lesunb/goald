package goald.evaluation.assertives;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Propositions {

	public List<String> has;
	public List<String> hasNot;
	
	public void clean() {
		has = new ArrayList<>();
		hasNot = new ArrayList<>();
	}
	
	public Propositions() {
		clean();
	}
	
	public Propositions (List<String> has, List<String> hasNot) {
		this.has = has;
		this.hasNot = hasNot;
	}
	
	public Propositions has(String ...propositions) {
		this.has.addAll(Arrays.asList(propositions));
		return this;
	}

	public Propositions hasnot(String ...propositions) {
		this.hasNot.addAll(Arrays.asList(propositions));
		return this;
	}

	public Propositions close() {
		List<String> has = this.has;
		List<String> hasNot = this.hasNot;
		Propositions return_ = new Propositions(has, hasNot);
		clean();
		return return_;
	}

	@Override
	public String toString() {
		return has + ", !(" + hasNot +")";
	}

}
