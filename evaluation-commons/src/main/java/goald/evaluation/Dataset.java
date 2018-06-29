package goald.evaluation;

import java.util.ArrayList;
import java.util.List;

public class Dataset {

	List<String> lines;
	
	public static Dataset create(){
		Dataset instance = new Dataset();
		instance.lines = new ArrayList<>();
		return instance;
	}
	
	public void log(Object... columns){
		StringBuilder sb = new StringBuilder();
		for(Object column:columns){
			sb.append(column.toString()+'\t'); 
		}
		this.lines.add(sb.toString()+"\r\n");
	}
}
