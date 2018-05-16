package goalp.exputil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import goalp.evaluation.model.ExecResult;
import goalp.model.DeploymentRequest;
import goalp.model.Goal;
import goalp.systems.DeploymentPlan;

public class ConsoleDeploymentPlanWriteService extends WriteService {


	public void it(DeploymentRequest request, DeploymentPlan plan) {
		
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
			write(request, plan, writer);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private void write(DeploymentRequest request, DeploymentPlan plan, BufferedWriter writer) throws IOException{
		for(Goal goal: request.getGoals()){
			printTree(goal, plan, 0, writer);			
		}
	}

	@Override
	public void it(ExecResult result) {
		it(result.getRequest(), result.getResultPlan().getPlan());
	}
}
