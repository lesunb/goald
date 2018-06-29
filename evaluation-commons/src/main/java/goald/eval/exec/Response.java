package goald.eval.exec;

import java.util.List;

import goald.exputil.ExperimentTimer.Split;

public class Response {

	List<Split> splits;

	public Response(List<Split> splits){
		this.splits = splits;
	}

	public Long getDuration(String label){
		for(Split split: splits){
			if(label.equals(split.getLabel())){
				return split.getDuration();
			}
		}
		throw new IllegalArgumentException("split label not found");
	}
	
	public List<Split> getSplits() {
		return splits;
	}

}
