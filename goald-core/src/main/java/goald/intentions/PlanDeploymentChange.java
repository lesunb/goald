package goald.intentions;

import java.util.List;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.UuidEntityManager;
import com.artemis.systems.VoidEntitySystem;

import goald.beliefs.DeploymentModel;
import goald.beliefs.HashMapRepository;
import goald.beliefs.model.Bundle;
import goald.beliefs.model.Dependency;
import goald.beliefs.model.Dependency.DependencyStatus;
import goald.beliefs.model.DeploymentUnit;
import goald.beliefs.model.DeploymentUnit.DeploymentUnitType;
import goald.beliefs.model.IRepository;
import goald.beliefs.util.DependencyBuilder;
import goald.beliefs.util.DeploymentUnitBuilder;
import goald.desires.model.Goal;
import goald.events.AddGoal;
import goald.events.DeploymentRequest;
import goald.model.DefinitionBundle;
import goalp.repository.HashMapRepository.BundleType;
import net.mostlyoriginal.api.event.common.Subscribe;

/**
 * Plan for a deployment change
 * @author grodrigues
 *
 */
@Wire
public class PlanDeploymentChange extends VoidEntitySystem{

	UuidEntityManager uuidManager;
	
	ComponentMapper<DeploymentModel> cmModel;
	ComponentMapper<HashMapRepository> cmRepository;
	
	@Override
	protected void processSystem() {
		// TODO Auto-generated method stub
		
	} 
	
	
	public void handleNewGoal(DeploymentRequest request) {
		
		
		
		//
		
		System.out.println("processing");
	}
	
	/** 
	 *  look for definition
	 *  
	 *  dispatch:
	 *   deploymentPlan
	 *   deploymentPlanFailure
	 */
	@Subscribe
	public void handleNewGoal(AddGoal addGoal){ 
		
		Entity agent = uuidManager.getEntity(addGoal.target);
		// deployment model of target agent
		DeploymentModel model = cmModel.getSafe(agent);
		IRepository repository = cmRepository.getSafe(agent);
		
		Dependency newGoalDependency = DependencyBuilder.create()
		.identifier(addGoal.identification)
		.build();
		
		DeploymentUnit unit = DeploymentUnitBuilder.create()
		.status(DependencyStatus.UNDEFINED)
		.type(DeploymentUnitType.ABSTRACT)
		.withDependency(newGoalDependency)
		.build();
		
		/* add a new 1st level goal to deployment model with 
		 * status not resolved */
		
		/* modify deployment graph adding deploymentunits with
		 *  'not resolved' status
		 */
		model.getFistLevelDeploymentUnits().add(unit);
				
		/* plan for the new goal*/
		resolveDependencies(unit, repository);
	}
	
	public void resolveDependencies(DeploymentUnit actualUnit, IRepository repository) {
		
		// update maps
		
		actualUnit.getDependencies().forEach( dependency -> {
			if(dependency.status == DependencyStatus.UNDEFINED) {
				List<Bundle> definitions = repository.queryFor(BundleType.DEFINITION, dependency.identifier);
				Bundle def = definitions.get(0);
				dependency.definition = def;
				
				List<Bundle> alternatives = repository.queryFor(BundleType.IMPLEMENTATION, dependency.identifier);
				dependency.alternatives = alternatives;
				
				// TODO map to DeploymentUnit
				alternatives.forEach( alternative -> {
					DeploymentUnit unit = DeploymentUnitBuilder.create()
							.status(DependencyStatus.UNDEFINED)
							.bundle(alternative)
							.build();
					resolveDependencies(unit,repository);
				});
				
				List<DeploymentUnit> candidates = null;
				
				// sort by quality
				sortAlternatives(def, candidates);
				
				// evaluate is requirements are satisfiable
				DeploymentUnit electedImpl = findBestAlternative(candidates);
				dependency.selected = electedImpl;

			}
			
		});
				
		/* check all dependencies was met */

		
		/* dispatch new plan event */
		
	}

	private void sortAlternatives(Bundle def, List<DeploymentUnit> alternatives) {
		// TODO Auto-generated method stub
		
	}

	
	private DeploymentUnit findBestAlternative(List<DeploymentUnit> alternatives) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public DefinitionBundle lookForDefinition(Goal goal) {
		
		/* look for all possible implementations, 
		 * mapping requirements and contributions */

		/* for each dependency, 
		 * updateDeploymentGraph */
		return null;
	}
	
	public void lookForImplemenations(Goal goal, DefinitionBundle definition) {
		/* query all */
		
		/* for each */
		
		/* for each dependency */
			//updateDeploymentGraph
		
		/* check requirements and contributions */
	
		/* mapping requirements and contributions */

		/* for each dependency, 
		 * updateDeploymentGraph */

	}
	
	
	public void planDeployment(Goal goal) {
		/* look for definition */
		
		/* look for all possible implementations, 
		 * mapping requirements and contributions */

		/* if has only one implementation
		 * 
		 *  */
		
	}

	
	
}
