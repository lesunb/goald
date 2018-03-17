package goald.beliefs.model;

import java.util.List;

import goald.desires.model.Goal;

public interface IRepository {

	void add(Bundle bundle);

	Bundle queryForDefinition(Goal goal);

	List<Bundle> queryForImplementations(Goal goal);
	
	List<Bundle> queryForDefinitions(Goal goal);
	
	int getSize();

}