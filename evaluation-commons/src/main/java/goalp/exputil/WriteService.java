package goalp.exputil;

import java.io.BufferedWriter;
import java.io.IOException;

import goalp.evaluation.model.ExecResult;
import goalp.model.Artifact;
import goalp.model.Goal;
import goalp.systems.DeploymentPlan;

public abstract class WriteService {



	protected void printTree(Goal goal, DeploymentPlan plan, int level, BufferedWriter writer) throws IOException {
		if(level == 0){
			writer.write("## Root Goal");
			newLine(writer);
		}
		printLevel(level, writer);
		writer.write(goal.getIdentication());
		newLine(writer);
		for(Artifact artifact:plan.getSelectedArtifacts()){
			if(artifact.isProvider(goal)){
				printTree(artifact, plan, level+1, writer);
			}
		}
	}


	private void printTree(Artifact artifact, DeploymentPlan plan, int level, BufferedWriter writer) throws IOException {
		printLevel(level, writer);
		writer.write(artifact.getIdentification());
		if(artifact.getDependencies().size() == 0){
			writer.write("::");
			writer.write(artifact.getContextConditions().toString());
			newLine(writer);
		}else {
			newLine(writer);
			for(Goal goal:artifact.getDependencies()){
				printTree(goal, plan, level, writer);			
			}
		}
		
	}
	
	private void printLevel(int level, BufferedWriter writer) throws IOException{
		for(int i =0; i<level; i++){
			writer.write(" ");
		}
		writer.write("|_");
	}
	
	private void newLine(BufferedWriter writer) throws IOException {
		writer.write("\n");
	}


	public abstract void it(ExecResult result);
}
