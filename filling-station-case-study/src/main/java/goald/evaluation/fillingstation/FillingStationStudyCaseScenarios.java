package goald.evaluation.fillingstation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.Evaluation;
import goald.eval.exec.ScenarioBasedExperimentExecutior;
import goald.evaluation.EvaluationBuilder;
import goald.model.util.ContextChangeBuilder;
import goald.planning.DameRespository;
import goald.repository.IRepository;

public class FillingStationStudyCaseScenarios extends ScenarioBasedExperimentExecutior {
	
	@Inject
	Logger log;
	
	public void setup() {
		repo = new DameRespository(getRepo());
	}

	public List<Evaluation>  caseStudy() {
		List<Evaluation> evaluations = new ArrayList<>();
		Evaluation baseEvaluation = EvaluationBuilder.create()
				.setConstant("resultFileName", "fsa_dataset")
				.build();
		/* 
		 * Study case scenarios. Each one define a different set of contexts. 
		 * For each one the deployment will be planned
		 */
		scenarioRepetitions(1000, 1, (ctxEvaluatorBuilder)->{
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
		}, evaluations, baseEvaluation);
		
		scenarioRepetitions(1000, 2, (ctxEvaluatorBuilder)->{
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
		}, evaluations, baseEvaluation);
		
		scenarioRepetitions(1000, 3, (ctxEvaluatorBuilder)->{
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
		}, evaluations, baseEvaluation);
	
		scenarioRepetitions(1000, 4, (ctxEvaluatorBuilder)->{
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
		}, evaluations, baseEvaluation);
	
		scenarioRepetitions(1000, 5, (ctxEvaluatorBuilder)->{
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
		}, evaluations, baseEvaluation);
	
		
		scenarioRepetitions(1000, 6, (ctxEvaluatorBuilder)->{
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
		}, evaluations, baseEvaluation);
	
		scenarioRepetitions(1000, 7, (ctxEvaluatorBuilder)->{
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
		}, evaluations, baseEvaluation);
		
		return evaluations;
	}
	
	protected IRepository getRepo(){
		return FSARepository.getRepo();
	}
}
