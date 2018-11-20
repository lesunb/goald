package goald.repository;

import java.util.List;

import goald.model.Bundle;
import goald.model.Dependency;

public interface IRepository {

	void add(Bundle bundle);
	
	int getSize();

	Bundle queryForDefinition(Dependency goal);

	List<Bundle> queryForImplementations(Dependency goal);

	List<Bundle> queryForDefinitions(Dependency goal);
	
}