package goald.beliefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.artemis.Component;

import goald.beliefs.model.Bundle;
import goald.beliefs.model.IRepository;
import goald.desires.model.Goal;
import goalp.repository.HashMapRepository.BundleType;

public class HashMapRepository extends Component implements IRepository {

	protected HashMap<String, Set<Bundle>> knownDefinitions;
	
	protected HashMap<String, Set<Bundle>> knownImplementations;
		
	int repoSize = 0;

	/* (non-Javadoc)
	 * @see goalp.systems.IRepository#getKnownArtifacts()
	 */
	protected HashMap<String, Set<Bundle>> getKnownBundles(BundleType type) {
		if(type == BundleType.DEFINITION ) {
			if(knownDefinitions == null){
				knownDefinitions = new HashMap<>();
			}
			return knownDefinitions;
		} else {
			if( knownImplementations == null){
				knownImplementations = new HashMap<>();
			}
			return knownImplementations;
		}
	}

	@Override
	public void add(Bundle bundle) {
		repoSize++;
		
		bundle.getDefines().forEach(goal ->{
			put(BundleType.DEFINITION, goal, bundle);
		});
		
		bundle.getProvides().forEach(goal ->{
			put(BundleType.IMPLEMENTATION, goal, bundle);
		});
	}
	
	private void put(BundleType type, Goal goal, Bundle artifact) {
		Set<Bundle> list = getKnownBundles(type).get(goal.getIdentication());
		if (list == null) {
			list = new HashSet<>();
			getKnownBundles(type).put(goal.getIdentication(), list);
		}
		list.add(artifact);
	}
	
	public int getSize(){
		return repoSize;
	}
	
	/* (non-Javadoc)
	 * @see goalp.systems.IRepository#getArtifactsThatProvideGoal(java.lang.String)
	 */
	@Override
	public Bundle queryForDefinition(String goalId){
		List<Bundle> bundles = new ArrayList<>();
		Set<Bundle> bundlesSet = getKnownBundles(BundleType.DEFINITION).get(goalId);
		if(bundlesSet != null) {
			bundles.addAll(bundlesSet);
		}
		return bundles.get(0);
	}
	
	/* (non-Javadoc)
	 * @see goalp.systems.IRepository#getArtifactsThatProvideGoal(java.lang.String)
	 */
	@Override
	public List<Bundle>  queryForImplementations(String goalId){
		List<Bundle> bundles = new ArrayList<>();
		Set<Bundle> bundlesSet = getKnownBundles(BundleType.IMPLEMENTATION).get(goalId);
		if(bundlesSet != null) {
			bundles.addAll(bundlesSet);
		}
		return bundles;
	}

	@Override
	public List<Bundle> queryForDefinitions(String goalId) {
		List<Bundle> bundles = new ArrayList<>();
		Set<Bundle> bundlesSet = getKnownBundles(BundleType.DEFINITION).get(goalId);
		if(bundlesSet != null) {
			bundles.addAll(bundlesSet);
		}
		return bundles;
	}

	@Override
	public Set<Bundle> queryFor(BundleType type, String goalId) {
		return getKnownBundles(type).get(goalId);
	}

	@Override
	public Bundle queryForDefinition(Goal goal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bundle> queryForImplementations(Goal goal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bundle> queryForDefinitions(Goal goal) {
		// TODO Auto-generated method stub
		return null;
	}

}