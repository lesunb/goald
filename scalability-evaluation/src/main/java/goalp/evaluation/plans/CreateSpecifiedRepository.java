package goalp.evaluation.plans;


import goald.beliefs.model.IRepository;
import goalp.evaluation.model.ExecSpec;
import goalp.model.Artifact;
import goalp.model.ArtifactBuilder;
import goalp.systems.RepositoryBuilder;

public class CreateSpecifiedRepository {
	
	public static IRepository exec(ExecSpec spec, String rootGoal){
		Integer width = spec.getInteger("width");
		Integer depth = spec.getInteger("depth");
		
		RepositoryBuilder builder = RepositoryBuilder.create();
		
		createArtifactWithWidthDependenciesOfDepthDepencies(builder, rootGoal,
				width, depth);
		
		return builder.build();
	}
	
	/**
	 * Create a beaded curtain repository without duplication
	 * 
	 * @param builder
	 * @param width
	 * @param depth
	 * @return
	 */
	static Artifact createArtifactWithWidthDependenciesOfDepthDepencies(RepositoryBuilder builder, String rootGoal, 
			int width, int depth){
		
		String[] branchsProvidedGoal = new String[ width ];
		
		//create 'width number' artifacts  
		for(int i = 0; i < width; i++){
			//create 'depth deep' artifact chains
 			Artifact artifact = createArtifactChain(builder, depth);
			branchsProvidedGoal[i] = artifact.getIdentification();
		}
		
		String artifactLabel = "br.unb:artifactRoot:0.0.1";
		String artifactProvide = rootGoal;
		
		Artifact rootArtifact = ArtifactBuilder.create()
				.identification(artifactLabel)
				.provides(artifactProvide)
				.dependsOn(branchsProvidedGoal)
				.build();
		
		builder.add(rootArtifact);
		return rootArtifact;
	}
	
	
	static Artifact createArtifactChain(RepositoryBuilder builder, int leftDepth){
		if(leftDepth==0){
			//leaf artifact, do not depends on any other
			String artifactLabel = "br.unb:artifactLeaf"+randonLabel() + ":0.0.1";
			String artifactProvide = "br.unb:goalLeaf"+randonLabel() + ":0.0.1";
			
			Artifact leaf = ArtifactBuilder.create()
					.identification(artifactLabel)
					.provides(artifactProvide)
					.build();
			
			builder.add(leaf);
			return leaf;
		}else{
			//no leaf
			String artifactLabel = "br.unb:artifactDeep"+ leftDepth + randonLabel() + ":0.0.1";
			String artifactProvide = "br.unb:goalDeep"+ leftDepth + randonLabel() + ":0.0.1";

			Artifact dependency = createArtifactChain(builder, leftDepth - 1);
			Artifact dependee =  ArtifactBuilder.create()
					.identification(artifactLabel)
					.provides(artifactProvide)
					.dependsOn(dependency.getIdentification()) //TODO should be the provided goal ?
					.build();
			builder.add(dependee);
			return dependee;
		}
	}
	
	static String randonLabel(){
		Double seed = Math.random();
		return seed.toString().replace("0.","_");
	}
	
}
