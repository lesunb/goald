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
		for(int i =1; i<=1000; i++){
			System.out.println("\n\n exec " + i);
			doCaseStudy(i);
		}
	}
	
	public void doCaseStudy(int execIndex) {
		/* 
		 * Study case scenarios. Each one define a different set of contexts. 
		 * For each one the deployment will be planned
		 */
		scenario("s1", execIndex, (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
					"antenna_triangulation", 
					"protocol_get_fuel_level_and_mileage",
					"storage",
					"audio_player");
			},
			(qualityWeights) -> {
				qualityWeights.put("precision", 1);
				qualityWeights.put("responseTime", 1);
				qualityWeights.put("storegedSize", 1);
				qualityWeights.put("usability", 1);
			},
			(goalsChangeBuilding)->{
				goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
			},
			(changesList) -> {
				changesList.add(ContextChangeBuilder.create()
						.remove("antenna_triangulation").build());
				changesList.add(ContextChangeBuilder.create()
						.add("antenna_triangulation").build());	
				changesList.add(ContextChangeBuilder.create()
						.remove("protocol_get_fuel_level_and_mileage").build());
				changesList.add(ContextChangeBuilder.create()
						.add("protocol_get_fuel_level_and_mileage").build());	
		});
		
		scenario("s2", execIndex, (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability",
				"protocol_on_board_computer_get_distante_to_empty",
				"internet_connection",
				"synthesized_voice");
			},
			(qualityWeights) -> {
				qualityWeights.put("precision", 1);
				qualityWeights.put("responseTime", 1);
				qualityWeights.put("storegedSize", 1);
				qualityWeights.put("usability", 1);
			},
			(goalsChangeBuilding)->{
				goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
			},
			(changesList) -> {
				changesList.add(ContextChangeBuilder.create()
						.remove("internet_connection").build());
				changesList.add(ContextChangeBuilder.create()
						.add("internet_connection").build());	
				changesList.add(ContextChangeBuilder.create()
						.remove("synthesized_voice").build());
				changesList.add(ContextChangeBuilder.create()
						.add("synthesized_voice").build());	
			});
		
		scenario("s3", execIndex ,(ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability", 
				"internet_connection",
				"synthesized_voice");
		},
		(qualityWeights) -> {
			qualityWeights.put("precision", 1);
			qualityWeights.put("responseTime", 1);
			qualityWeights.put("storegedSize", 1);
			qualityWeights.put("usability", 1);
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("gps_capability").build());
			changesList.add(ContextChangeBuilder.create()
					.add("gps_capability").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("internet_connection").build());
			changesList.add(ContextChangeBuilder.create()
					.add("internet_connection").build());	
		});
	
		scenario("s4", execIndex, (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"storage",
				"visible_graphical_interface");
		},
		(qualityWeights) -> {
			qualityWeights.put("precision", 1);
			qualityWeights.put("responseTime", 1);
			qualityWeights.put("storegedSize", 1);
			qualityWeights.put("usability", 1);
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("protocol_on_board_computer_get_distante_to_empty").build());
			changesList.add(ContextChangeBuilder.create()
					.add("protocol_on_board_computer_get_distante_to_empty").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("visible_graphical_interface").build());
			changesList.add(ContextChangeBuilder.create()
					.add("visible_graphical_interface").build());	
		});
	
		scenario("s5", execIndex, (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"internet_connection",
				"interface_navigation_system");
		},
		(qualityWeights) -> {
			qualityWeights.put("precision", 1);
			qualityWeights.put("responseTime", 1);
			qualityWeights.put("storegedSize", 1);
			qualityWeights.put("usability", 1);
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("protocol_on_board_computer_get_distante_to_empty").build());
			changesList.add(ContextChangeBuilder.create()
					.add("protocol_on_board_computer_get_distante_to_empty").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("interface_navigation_system").build());
			changesList.add(ContextChangeBuilder.create()
					.add("interface_navigation_system").build());	
		});
	
		
		scenario("s6", execIndex, (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"protocol_on_board_computer_get_distante_to_empty", 
				"storage",
				"synthesized_voice");
		},
		(qualityWeights) -> {
			qualityWeights.put("precision", 1);
			qualityWeights.put("responseTime", 1);
			qualityWeights.put("storegedSize", 1);
			qualityWeights.put("usability", 1);
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("storage").build());
			changesList.add(ContextChangeBuilder.create()
					.add("storage").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("protocol_on_board_computer_get_distante_to_empty").build());
			changesList.add(ContextChangeBuilder.create()
					.add("protocol_on_board_computer_get_distante_to_empty").build());	
		});
	
		scenario("s7", execIndex, (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"interface_navigation_system");
		},
		(qualityWeights) -> {
			qualityWeights.put("precision", 1);
			qualityWeights.put("responseTime", 1);
			qualityWeights.put("storegedSize", 1);
			qualityWeights.put("usability", 1);
		},
		(goalsChangeBuilding)->{
			goalsChangeBuilding.addGoal("vehicle-refueling-is-assisted");
		},
		(changesList) -> {
			changesList.add(ContextChangeBuilder.create()
					.remove("protocol_on_board_computer_get_distante_to_empty").build());
			changesList.add(ContextChangeBuilder.create()
					.add("protocol_on_board_computer_get_distante_to_empty").build());	
			changesList.add(ContextChangeBuilder.create()
					.add("interface_navigation_system").build());	
			changesList.add(ContextChangeBuilder.create()
					.remove("interface_navigation_system").build());	
		});
	}
	
	protected IRepository getRepo(){
		return FSARepository.getRepo();
	}

}
