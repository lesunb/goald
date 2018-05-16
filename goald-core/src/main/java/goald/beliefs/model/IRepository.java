package goald.beliefs.model;

import java.util.List;
import java.util.Set;

import goald.desires.model.Goal;
import goalp.repository.HashMapRepository.BundleType;

public interface IRepository {

	void add(Bundle bundle);

	Bundle queryForDefinition(String goalId);

	List<Bundle> queryForImplementations(String goalId);
	
	List<Bundle> queryForDefinitions(String goalId);
	
	List<Bundle> queryFor(BundleType type, String goalId);
	
	int getSize();

	Bundle queryForDefinition(Goal goal);

	List<Bundle> queryForImplementations(Goal goal);

	List<Bundle> queryForDefinitions(Goal goal);

}