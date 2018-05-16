package goald.dam.model;

import java.util.List;

public class Alternative {
	private Dame parentDame;
	
	private List<String> dependencyGoals;
	
	private Boolean ctxSatisfied;
	
	private Boolean resolved;
	
	private List<Dame> listDepDame;
	
}
