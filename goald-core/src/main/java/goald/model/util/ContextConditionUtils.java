package goald.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import goald.model.ContextCondition;

public class ContextConditionUtils {

	 
	public static List<ContextCondition> conditions(List<String> contexts) {
		
		List<ContextCondition> conditions = new ArrayList<>();
		for(String context: contexts) {
			conditions.add(new ContextCondition(context));
		}
		return conditions;		
	}

	public static List<ContextCondition> conditions(String ctx) {
		return conditions(Arrays.asList(ctx));	
	}
	
}
