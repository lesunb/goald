package goald.exputil;

import java.util.List;

import goald.evaluation.ExperimentTimerImpl.Split;

public interface ExperimentTimer {

	void begin();

	Number split(String string);

	void finish();

	List<Split> result();

}