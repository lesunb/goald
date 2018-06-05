package goald.exputil;

import java.io.BufferedWriter;
import java.io.IOException;

import goald.eval.exec.ExecResult;
import goald.model.Dame;

public abstract class WriteService {



	protected void printTree(Dame dame, int level, BufferedWriter writer) throws IOException {
		if(level == 0){
			writer.write("## Root Dame");
			newLine(writer);
		}
		printLevel(level, writer);
		writer.write(dame.getDefinition().getIdentification());
		writer.write(dame.getChosenAlt().getImpl().getIdentification());
		newLine(writer);
		if(dame.getChosenAlt() != null) {
			for(Dame dep:dame.getChosenAlt().getListDepDame()){
				printTree(dep, level+1, writer);
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
