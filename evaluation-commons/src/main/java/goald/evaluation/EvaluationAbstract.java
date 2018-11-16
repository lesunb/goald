package goald.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EvaluationAbstract<M> implements Evaluation<M> {

	protected Map<String, Number> factors = new HashMap<>();
	protected Map<String, Object> constant = new HashMap<>();
	private Map<Integer, List<M>> indexedMeasures;

	public EvaluationAbstract() {
		super();
	}

	/* (non-Javadoc)
	 * @see goald.evaluation.Evaluation#getFactors()
	 */
	@Override
	public Map<String, Number> getFactors() {
		if(factors == null){
			factors = new HashMap<String, Number>();
		}
		return factors;
	}

	/* (non-Javadoc)
	 * @see goald.evaluation.Evaluation#getFactorList()
	 */
	@Override
	public List<String> getFactorList() {
		List<String> list = new ArrayList<>();
		for(String factor:getFactors().keySet()){
			list.add(factor);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see goald.evaluation.Evaluation#setFactors(java.util.Map)
	 */
	@Override
	public void setFactors(Map<String, Number> factors) {
		this.factors = factors;
	}

	/* (non-Javadoc)
	 * @see goald.evaluation.Evaluation#putFactor(java.lang.String, java.lang.Number)
	 */
	@Override
	public void putFactor(String factor, Number value) {
		getFactors().put(factor, value);
	}

	/* (non-Javadoc)
	 * @see goald.evaluation.Evaluation#getMeasures(java.lang.Integer)
	 */
	@Override
	public List<M> getMeasures(Integer execIndex) {
		if(this.indexedMeasures == null) {
			this.indexedMeasures = new HashMap<>();
		}
		if(this.indexedMeasures.get(execIndex) == null) {
			this.indexedMeasures.put(execIndex, new ArrayList<>());
		}
		return this.indexedMeasures.get(execIndex);
	}

	/* (non-Javadoc)
	 * @see goald.evaluation.Evaluation#getIndexedMeasures()
	 */
	@Override
	public Map<Integer, List<M>> getIndexedMeasures() {
		return indexedMeasures;
	}

	/* (non-Javadoc)
	 * @see goald.evaluation.Evaluation#getConstants()
	 */
	@Override
	public Map<String, Object> getConstants() {
		if(constant == null) {
			constant = new HashMap<>();
		}
		return constant;
	}

}