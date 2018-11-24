package goald.evaluation.assertives;

import java.util.function.Consumer;

public class Assertion implements Consumer<Assertion>{

	public enum AssertionResult {
		FALSE_POSITIVE,
		FALSE_NEGATIVE,
		TRUE_POSITIVE,
		TRUE_NEGATIVE
	}
	
	public AssertionResult analysisResult;
	public boolean passAfterChange;
	public String[] infos;
	public boolean allPass;
	
	public Assertion() {
		
	}
	
	public Assertion(AssertionResult analysisResult, boolean passAfterChange, String ...infos) {
		this.analysisResult = analysisResult;
		this.passAfterChange = passAfterChange;
		this.infos = infos;
	}

	@Override
	public void accept(Assertion t) {
		if(this.analysisResult == null) {
			this.analysisResult = t.analysisResult;
			this.passAfterChange = t.passAfterChange;
			this.allPass = t.passAfterChange;
		} else if(!checkCompatibleAssertions(this.analysisResult, t.analysisResult)) {
			throw new IllegalStateException("mismatch");
		} else {
			this.analysisResult = combine(this.analysisResult, t.analysisResult);
			this.allPass &= t.passAfterChange;
		}
	}
	
	private AssertionResult combine(AssertionResult a, AssertionResult b) {
		//less priority
		// FALSE_POSITIVE -> has no evidence that the change was required 
		// TRUE_NEGATIVE -> has no evidence that the change was not required
		
		//hight priority
		// FALSE_NEGATIVE, TRUE_POSITIVE -> only one case proves
		if(b == AssertionResult.FALSE_NEGATIVE || b == AssertionResult.TRUE_POSITIVE ) {
			return b;
		}else {
			return a;
		}
	}

	private boolean checkCompatibleAssertions(AssertionResult a, AssertionResult b) {

		return ( (a == AssertionResult.TRUE_POSITIVE || a == AssertionResult.FALSE_POSITIVE)
				&& (b == AssertionResult.TRUE_POSITIVE || b == AssertionResult.FALSE_POSITIVE)
				|| (a == AssertionResult.TRUE_NEGATIVE || a == AssertionResult.FALSE_NEGATIVE)
				&& (b == AssertionResult.TRUE_NEGATIVE || b == AssertionResult.FALSE_NEGATIVE));
	}

	public void combine(Assertion t) {
		System.out.println("combining..");
	}

	public AssertionResult getResult() {
		return analysisResult;
	}

	public void setResult(AssertionResult result) {
		this.analysisResult = result;
	}

	@Override
	public String toString() {
		return "Assertion [result=" + analysisResult + ", allPass="+ allPass + "]";
	}
	
}
