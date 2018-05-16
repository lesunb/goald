package goalp.evaluation.plans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import goalp.evaluation.goals.ICreateExperiments;
import goalp.evaluation.model.ExecSpec;
import goalp.evaluation.model.Experiment;
import goalp.evaluation.model.ExperimentBuilder;

public abstract class AbstractCreateExperiments  implements ICreateExperiments {

	public AbstractCreateExperiments() {
		super();
	}
	
	@Override
	public List<Experiment> exec() {
		List<Experiment> experiments = new ArrayList<>();
		experiments.add(createExperiments());
		return experiments;
	}
	
	abstract protected Experiment createExperiments();

	protected void addExecSpecsWithTwoInRangeSetter(ExecSpec model, int r1From, int r1To, int r1Step, int r2From,
			int r2To, int r2Step, BiConsumer<ExecSpec, Integer[]> setter, ExperimentBuilder parentBuilder) {
				for (int i = r1From; i <= r1To; i += r1Step) {
					for (int j = r2From; j <= r2To; j += r2Step) {
						Integer[] params = {i,j};
						ExecSpec spec = model.clone();
						setter.accept(spec, params);
						parentBuilder.addSpec(spec);
					}
				}
	}

	protected void addExecSpecsWithInRangeSetter(ExecSpec model, int r1From, int r1To, int r1Step,
			BiConsumer<ExecSpec, Integer> setter, ExperimentBuilder parentBuilder) {
				for (int i = r1From; i <= r1To; i += r1Step) {
					Integer params = i;
					ExecSpec spec = model.clone();
					setter.accept(spec, params);
					parentBuilder.addSpec(spec);
				}
	}

	protected List<String> getContextSpace(int size) {
		String[] space = new String[size];
		for(int i = 0; i< size; i++){
			space[i] = "C"+i;
		}
		return Arrays.asList(space);
	}

}