package goalp.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.artemis.Component;

import goald.beliefs.model.Bundle;
import goald.beliefs.model.IRepository;
import goald.desires.model.Goal;

public class HashMapRepository extends Component implements IRepository {

	protected HashMap<String, List<Bundle>> knownDefinitions;
	
	protected HashMap<String, List<Bundle>> knownImplementations;
	
	public enum BundleType {
		DEFINITION,
		IMPLEMENTATION
	}
	
	
	int repoSize = 0;

	/* (non-Javadoc)
	 * @see goalp.systems.IRepository#getKnownArtifacts()
	 */
	protected HashMap<String, List<Bundle>> getKnownBundles(BundleType type) {
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
		List<Bundle> list = getKnownBundles(type).get(goal.getIdentication());
		if (list == null) {
			list = new ArrayList<>();
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
	public Bundle queryForDefinition(Goal goal){
		List<Bundle> bundles = new ArrayList<>();
		List<Bundle> bundlesList = getKnownBundles(BundleType.DEFINITION).get(goal.getIdentication());
		if(bundlesList != null) {
			bundles.addAll(bundlesList);
		}
		return bundles.get(0);
	}
	
	/* (non-Javadoc)
	 * @see goalp.systems.IRepository#getArtifactsThatProvideGoal(java.lang.String)
	 */
	@Override
	public List<Bundle>  queryForImplementations(Goal goal){
		List<Bundle> bundles = new ArrayList<>();
		List<Bundle> bundlesList = getKnownBundles(BundleType.IMPLEMENTATION).get(goal.getIdentication());
		if(bundlesList != null) {
			bundles.addAll(bundlesList);
		}
		return bundles;
	}

	@Override
	public List<Bundle> queryForDefinitions(Goal goal) {
		List<Bundle> bundles = new ArrayList<>();
		List<Bundle> bundlesList = getKnownBundles(BundleType.DEFINITION).get(goal.getIdentication());
		if(bundlesList != null) {
			bundles.addAll(bundlesList);
		}
		return bundles;
	}

	@Override
	public Bundle queryForDefinition(String goalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bundle> queryForImplementations(String goalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bundle> queryForDefinitions(String goalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bundle> queryFor(BundleType type, String goalId) {
		return getKnownBundles(type).get(goalId);
	}

}