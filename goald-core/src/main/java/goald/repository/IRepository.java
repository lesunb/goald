package goald.repository;

import java.util.List;

import goald.model.Bundle;

public interface IRepository {

	void add(Bundle bundle);
	
	int getSize();

	Bundle queryForDefinition(String identification);

	List<Bundle> queryForImplementations(String identification);

	List<Bundle> queryForDefinitions(String identification);
	
}