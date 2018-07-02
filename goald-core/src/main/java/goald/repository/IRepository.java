package goald.repository;

import java.util.List;

import goald.model.Bundle;
import goald.model.Goal;

public interface IRepository {

	void add(Bundle bundle);
	
	int getSize();

	Bundle queryForDefinition(Goal goal);

	List<Bundle> queryForImplementations(Goal goal);

	List<Bundle> queryForDefinitions(Goal goal);
	
}