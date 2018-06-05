package goald.evaluation.fillingstation;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.exputil.ExperimentTimer;
import goald.model.util.ContextChangeBuilder;
import goald.repository.IRepository;

public class FillingStationStudyCase extends AbstractStudyCase {
	
	IRepository repo;
	
	@Inject
	Logger log;
	
	@Inject
	ExperimentTimer timer;
	
//	@Inject
//	WriteService write;
	

	public void caseStudy() {
		for(int i =1; i<=1; i++){
			System.out.println("\n\n exec " + i);
			doCaseStudy();
		}
	}
	
	public void doCaseStudy() {
		/* 
		 * Study case scenarios. Each one define a different set of contexts. 
		 * For each one the deployment will be planned
		 */
		scenario("s1", (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
					"antenna_triangulation", 
						"protocol_get_fuel_level_and_mileage",
						"storage",
						"sound");
			},
			(goalsChangeBuilding)->{
				goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
			},
			(changesList) -> {
				changesList.add(ContextChangeBuilder.create()
						.remove("storage").build());
				changesList.add(ContextChangeBuilder.create()
						.add("label").build());	
				changesList.add(ContextChangeBuilder.create()
						.add("storage").build());	
				changesList.add(ContextChangeBuilder.create()
						.remove("antenna_triangulation").build());	
		});
		
		scenario("s2", (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability",
				"protocol_on_board_computer_get_distante_to_empty",
				"internet_connection",
				"synthesized_voice");
			},
			(goalsChangeBuilding)->{
				goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
			},
			(changesList) -> {
				changesList.add(ContextChangeBuilder.create()
						.remove("storage").build());
				changesList.add(ContextChangeBuilder.create()
						.add("label").build());	
				changesList.add(ContextChangeBuilder.create()
						.add("storage").build());	
				changesList.add(ContextChangeBuilder.create()
						.remove("antenna_triangulation").build());	
			});
		
		scenario("s3",(ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability", 
				"internet_connection",
				"synthesized_voice");
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("storage").build());
			changesList.add(ContextChangeBuilder.create()
					.add("label").build());	
			changesList.add(ContextChangeBuilder.create()
					.add("storage").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("antenna_triangulation").build());	
		});
	
		scenario("s4", (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"storage",
				"visible_graphical_interface");
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("storage").build());
			changesList.add(ContextChangeBuilder.create()
					.add("label").build());	
			changesList.add(ContextChangeBuilder.create()
					.add("storage").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("antenna_triangulation").build());	
		});
	
		scenario("s5", (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"internet_connection",
				"interface_navigation_system");
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("storage").build());
			changesList.add(ContextChangeBuilder.create()
					.add("label").build());	
			changesList.add(ContextChangeBuilder.create()
					.add("storage").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("antenna_triangulation").build());	
		});
	
		
		scenario("s6", (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"protocol_on_board_computer_get_distante_to_empty", 
				"storage",
				"synthesized_voice");
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("storage").build());
			changesList.add(ContextChangeBuilder.create()
					.add("label").build());	
			changesList.add(ContextChangeBuilder.create()
					.add("storage").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("antenna_triangulation").build());	
		});
	
		scenario("s7", (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"interface_navigation_system");
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("storage").build());
			changesList.add(ContextChangeBuilder.create()
					.add("label").build());	
			changesList.add(ContextChangeBuilder.create()
					.add("storage").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("antenna_triangulation").build());	
		});
	
	}
	
	protected IRepository getRepo(){
		return FSARepository.getRepo();
	}

}
