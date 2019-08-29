package goald.evaluation.exec;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import goald.eval.exec.ExperimentSetup;
import goald.evaluation.model.RepoSpec;
import goald.exputil.RandomPrismRepositoryUtil;
import goald.model.Bundle;
import goald.model.DependencyModifier;
import goald.model.DependencyModifier.Type;
import goald.model.util.BundleBuilder;
import goald.repository.IRepository;
import goald.repository.RepositoryBuilder;

public class PrismRepositoryFactory {
	
	private IRepository repository;
	private Map<Integer, List<String>> rootGaoalsMap;
	private List<String> contextSpace;
	
	protected PrismRepositoryFactory(){
		
	}
	
	public static PrismRepositoryFactory create(){
		return new PrismRepositoryFactory();
	}
		
	public PrismRepositoryFactory buildBySpec(RepoSpec repoSpec){
		Integer depth = repoSpec.getInteger("depth");
		Integer numOfDependencies = repoSpec.getInteger("numOfDependencies");
		Integer numOfContextConditionsPerGoal = repoSpec.getInteger("contextVariabilityP");
		Integer numOfContextConditionsPerBundle = repoSpec.getInteger("contextVariabilityK");
		
		@SuppressWarnings("unchecked")
		List<String> contextSpace = repoSpec.getObject(List.class, "contextSpace");
		
		RepositoryBuilder builder = RepositoryBuilder.create();
		
		int[] variabilityRange = (int[]) repoSpec.getRepoSpec().get("variabilityRange");

		Map<Integer, List<String>> rootGaoalsMap = new HashMap<>();

		int numberOfTrees = repoSpec.getInteger("numberOfTrees");

		for(int variability = variabilityRange[0]; variability <= variabilityRange[1]; variability++){
			List<String> rootGoals = new ArrayList<String>();

			//crate a forest of n trees
			for(int j = 0; j < numberOfTrees; j++){
				String rootGoal = createBundleTree(builder,
						numOfDependencies, depth, j, variability, contextSpace, 
						numOfContextConditionsPerGoal, numOfContextConditionsPerBundle, 0.5f);
				rootGoals.add(rootGoal);
			}
			rootGaoalsMap.put(variability, rootGoals);
		}
		
		this.repository = builder.build();
		this.rootGaoalsMap = rootGaoalsMap;
		this.contextSpace = contextSpace;
		
		return this;
	}
	
	@SuppressWarnings("serial")
	private String createBundleTree(RepositoryBuilder builder, int numOfDependencies, int depth, int treeId, int variability,			
			List<String> contexts,  int numOfContextConditionsPerGoal,  int numOfContextConditionsPerArtifact, 
			float andRefinementsProportion){
		if(depth==0){
			/** create a means-end **/
			
			// create a definition bundle
			String goalId = "goald" + treeId + ":" + randonLabel();
			
			Bundle def = BundleBuilder.create()
					.identification(goalId+"-def")
					.defines(goalId)
					.build();			
			builder.add(def);
			// create the implementations bundle
			Deque<String> contextSpace = new ArrayDeque<>();
			Set<String> artifactVariabilityContextSpace = RandomPrismRepositoryUtil.getPElements(contexts, numOfContextConditionsPerGoal);
			contextSpace.addAll(artifactVariabilityContextSpace);
			
			Deque<Deque<String>> combinations = RandomPrismRepositoryUtil.getCombinations(contextSpace, numOfContextConditionsPerArtifact);
			for(int i = 0; i<variability; i++){
				if(variability > combinations.size()) {
					throw new IllegalStateException("variability of "+ variability + 
							" can't be achieved with p,k <"+ numOfContextConditionsPerGoal + "," +numOfContextConditionsPerArtifact + ">");
				}
				
				Deque<String> contextSelection = combinations.pop();
				String[] ctxs = {};
				String[] contextSelectionStr = contextSelection.toArray(ctxs);
				//leaf artifact, do not depends on any other
				String contextLabel  = RandomPrismRepositoryUtil.concat(contextSelection);
				String artifactLabel = "goald" + treeId + ":" + randonLabel() + contextLabel + "-impl";
				
				Bundle leaf = BundleBuilder.create()
						.identification(artifactLabel)
						.provides(goalId)
						.condition(contextSelectionStr) // generated the conditions after the selection
						.build();
				
				builder.add(leaf);
			}
			
			return goalId;
		} else {
			//goal
			String goaldId = "goald" + treeId + "goalDeep"+ depth + randonLabel();

			// definition
			Bundle def =  BundleBuilder.create()
					.identification(goaldId+"-def")
					.defines(goaldId)
					.build();
			builder.add(def);
			
			
			// dependencies			
			String[] dependencies = new String[ numOfDependencies ];
			
			//create 'numOfDependencies' subtrees  
			for(int i = 0; i < numOfDependencies; i++){
				//create 'depth deep' artifact chains
				String provide = createBundleTree(builder, numOfDependencies, 
						depth -1, treeId, variability, contexts, numOfContextConditionsPerGoal, 
						numOfContextConditionsPerArtifact, andRefinementsProportion);
				
				dependencies[i] = provide;
			}			
			
			// implementation
			BundleBuilder bundleBuilder = BundleBuilder.create()
					.identification(goaldId+"-impl")
					.provides(goaldId);
			
			// AND-refinement / OR-refinement
			DependencyModifier.Type modifier = RandomPrismRepositoryUtil
					.randomChoice(andRefinementsProportion, 
					Type.ANY, Type.ONE);

			for(String dependency: dependencies) {
				//TODO insert conds
				bundleBuilder.dependsOn(modifier, dependency);	
			}
			
			Bundle implementation =  bundleBuilder.build();
			builder.add(implementation);
			return goaldId;
		}
	}
	
	private String randonLabel(){
		Double seed = Math.random();
		return seed.toString().replace("0.","_");
	}
	
	/** Set an ExperimentSetup with infos about the built repo **/
	
	public PrismRepositoryFactory setSetupWithRepo(ExperimentSetup setup){
		setup.setRepository(repository);
		return this;
	}

	public PrismRepositoryFactory setSetupRootGoals(ExperimentSetup setup){
		setup.setRootGoalsMap(rootGaoalsMap);
		return this;
	}
	
	public PrismRepositoryFactory setCtxSpace(ExperimentSetup setup){
		setup.setCtxSpace(this.contextSpace);
		return this;
	}
	
}
