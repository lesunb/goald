package goald.evaluation;

import java.util.List;
import java.util.Map;

public interface Evaluation<M> {

	Map<String, Number> getFactors();

	List<String> getFactorList();

	void setFactors(Map<String, Number> factors);

	void putFactor(String factor, Number value);

	List<M> getMeasures(Integer execIndex);

	Map<Integer, List<M>> getIndexedMeasures();

	Map<String, Object> getConstants();
	
	String getResultToPrint();

}