package goalp.evaluation.exec;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import goald.eval.exec.ExperimentSetup;
import goald.exputil.RandomPrismRepositoryUtil;
import goald.model.Bundle;
import goald.model.util.BundleBuilder;
import goald.repository.IRepository;
import goald.repository.RepositoryBuilder;
import goalp.evaluation.model.RepoSpec;

public class PrismRepositoryFactory {
	
	private IRepository repository;
	private Map<Integer, List<String>> rootGaoalsMap;
	private List<String> contextSpace;
	
	protected PrismRepositoryFactory(){
		
	}
	
	public static PrismRepositoryFactory create(){
		return new PrismRepositoryFactory();
	}
	
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
	
	public PrismRepositoryFactory buildBySpec(RepoSpec repoSpec){
		Integer depth = repoSpec.getInteger("depth");
		Integer numOfDependencies = repoSpec.getInteger("numOfDependencies");
		Integer numOfContextConditionsPerGoal = repoSpec.getInteger("contextVariabilityP");
		Integer numOfContextConditionsPerArtifact = repoSpec.getInteger("contextVariabilityK");
		
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
				String rootGoal = createArtifactChain(builder,
						numOfDependencies, depth, j, variability, contextSpace, numOfContextConditionsPerGoal, numOfContextConditionsPerArtifact);
				rootGoals.add(rootGoal);
			}
			rootGaoalsMap.put(variability, rootGoals);
		}
		
		this.repository = builder.build();
		this.rootGaoalsMap = rootGaoalsMap;
		this.contextSpace = contextSpace;
		
		return this;
	}
	
	private String createArtifactChain(RepositoryBuilder builder, int numOfDependencies, int depth, int treeId, int variability,			
			List<String> contexts,  int numOfContextConditionsPerGoal,  int numOfContextConditionsPerArtifact){
		if(depth==0){
			//create  context dependent plans

			Deque<String> contextSpace = new ArrayDeque<>();
			Set<String> artifactVariabilityContextSpace = RandomPrismRepositoryUtil.getPElements(contexts, numOfContextConditionsPerGoal);
			contextSpace.addAll(artifactVariabilityContextSpace);
			
			String goalId = "goald" + treeId + ":" + randonLabel();
			
			Bundle deaf = BundleBuilder.create()
					.identification(goalId+"-def")
					.defines(goalId)
					.build();
			
			builder.add(deaf);
			
			Deque<Deque<String>> combinations = RandomPrismRepositoryUtil.getCombinations(contextSpace, numOfContextConditionsPerArtifact);
			for(int i = 0; i<variability; i++){
				Deque<String> contextSelection = combinations.pop();
				String[] ctxs = {};
				String[] contextSelectionStr = contextSelection.toArray(ctxs);
				//leaf artifact, do not depends on any other
				String contextLabel  = RandomPrismRepositoryUtil.concat(contextSelection);
				String artifactLabel = "goald" + treeId + ":" + randonLabel() + contextLabel + "-impl";
				
				
				Bundle leaf = BundleBuilder.create()
						.identification(artifactLabel)
						.provides(goalId)
						.requires(contextSelectionStr) // generated the conditions after the selection
						.build();
				builder.add(leaf);
			}
			
			return goalId;
		}else{
			//create strategies
			
			String[] branchsProvidedGoal = new String[ numOfDependencies ];
			
			//create 'width number' artifacts  
			for(int i = 0; i < numOfDependencies; i++){
				//create 'depth deep' artifact chains
				String provide = createArtifactChain(builder, numOfDependencies, depth -1, treeId, variability, contexts, numOfContextConditionsPerGoal, numOfContextConditionsPerArtifact);
				branchsProvidedGoal[i] = provide;
			}
			
			//no leaf
			String goaldId = "goald" + treeId + "goalDeep"+ depth + randonLabel();

			Bundle def =  BundleBuilder.create()
					.identification(goaldId+"-def")
					.defines(goaldId)
					.build();
			builder.add(def);
			
			Bundle strategy =  BundleBuilder.create()
					.identification(goaldId+"-impl")
					.provides(goaldId)
					.dependsOn(branchsProvidedGoal) //TODO should be the provided goal ?
					.build();
			builder.add(strategy);
			return goaldId;
		}
	}
	
	private String randonLabel(){
		Double seed = Math.random();
		return seed.toString().replace("0.","_");
	}
	
}
