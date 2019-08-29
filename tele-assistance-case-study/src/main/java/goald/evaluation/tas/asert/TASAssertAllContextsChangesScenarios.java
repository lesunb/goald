package goald.evaluation.tas.asert;


import static goald.evaluation.assertives.Assertion.AssertionResult.FALSE_NEGATIVE;
import static goald.evaluation.assertives.Assertion.AssertionResult.FALSE_POSITIVE;
import static goald.evaluation.assertives.Assertion.AssertionResult.TRUE_NEGATIVE;
import static goald.evaluation.assertives.Assertion.AssertionResult.TRUE_POSITIVE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.ExperimentAssertDeploymentChangesBased;
import goald.evaluation.assertives.Assertion;
import goald.evaluation.assertives.Assertion.AssertionResult;
import goald.evaluation.response.ResponseEvaluation;
import goald.evaluation.response.SplitTimer;
import goald.evaluation.tas.TASRepository;
import goald.planning.VERespository;
import goald.repository.IRepository;

public class TASAssertAllContextsChangesScenarios extends ExperimentAssertDeploymentChangesBased {
		
		@Inject
		Logger log;
		
		@Inject
		ResponseEvaluation baseEvaluation;


		Map<Assertion.AssertionResult, Integer> summaryMap = new HashMap<>();

		public void setup() {
			repo = new VERespository(getRepo());
			
		}
		

		public List<ResponseEvaluation>  caseStudy() {
			List<ResponseEvaluation> evaluations = new ArrayList<>();
			baseEvaluation.getConstants().put("resultFileName", "tsa_dataset");
			baseEvaluation.setTimer(SplitTimer.create());
			
			summaryMap.put(null, 0);
			
			summaryMap.put(TRUE_POSITIVE, 0);
			summaryMap.put(FALSE_NEGATIVE, 0);
			summaryMap.put(TRUE_NEGATIVE, 0);
			summaryMap.put(FALSE_POSITIVE, 0);
			
			/* 
			 * Study case scenarios. Each one define a different set of contexts. 
			 * For each one the deployment will be planned
			 */
			Map<Integer, Assertion> summary = new HashMap<>();
			
			scenarioRepetitions(100, 1,
					(ctxList)-> {
						ctxList.add("internet-connection");
						ctxList.add("!battery-is-low");
						ctxList.add("doctor-is-present");
						ctxList.add("drug-is-available");
						ctxList.add("!patient-is-ok");
					}
					,
					(qualityWeightsMap) -> {
						qualityWeightsMap.put("precision", 2);
						qualityWeightsMap.put("responseTime", 1);
						qualityWeightsMap.put("storegedSize", 1);
						qualityWeightsMap.put("usability", 1);
					},
					(goalsChangeBuilding)-> goalsChangeBuilding
						.addGoal("ProvideHealthSupport")
						, (assertDeploymentBuilder, p)-> assertDeploymentBuilder
						// not apply
						.withAssert(
							p.hasnot("internet-connection").close(),
							p.hasnot("AlarmService-impl", "RemoteAnalysis-impl").close())
						.withAssert(
							p.hasnot("doctor-is-present").close(),
							p.hasnot("ChangeDrug-impl", "ChangeDrug-def").close())
						.withAssert(
							p.hasnot("drug-is-available").close(),
							p.hasnot("ChangeDose-impl").close())
						.withAssert(
							p.hasnot("!battery-is-low").close(),
							p.hasnot("MonitorPatient-impl", "EnactTreatment-impl").close())
						.withAssert(
							p.hasnot("!patient-is-ok").close(),
							p.hasnot("EnactTreatment-impl").close())
						// should apply
						.withAssert(
								p.has("!battery-is-low").close(),
								p.has("ProvideAutomatedLifeSupport-impl").close())
						.withAssert(
								p.has("!battery-is-low", "!patient-is-ok").close(),
								p.has("EnactTreatment-impl").close())
						.withAssert(
								p.has("!battery-is-low", "!patient-is-ok", "drug-is-available").close(),
								p.has("ChangeDose-impl").close())
						.withAssert(
								p.has("!battery-is-low", "!patient-is-ok", "doctor-is-present").close(),
								p.has("ChangeDrug-impl").close())
						//quality
						.withAssert(
								p.has("internet-connection").close(),
								p.has("AlarmService-impl").close())

					,
					(assertion, execIndex)->{
						System.out.println(assertion);
						summary.put(execIndex, assertion);
					}
					, evaluations, baseEvaluation);

			
			
			summary.forEach((key, assertion) ->{
				System.out.print(key + ":");
				System.out.println(assertion);
				incSummary(assertion.analysisResult);
			});

			System.out.println(">"+ TRUE_POSITIVE + ":" + summaryMap.get(TRUE_POSITIVE));
			System.out.println(">"+ FALSE_NEGATIVE + ":" + summaryMap.get(FALSE_NEGATIVE));
			System.out.println(TRUE_NEGATIVE + ":" + summaryMap.get(TRUE_NEGATIVE));
			System.out.println(FALSE_POSITIVE + ":" + summaryMap.get(FALSE_POSITIVE));
			System.out.println("null" + ":" + summaryMap.get(null));
			
//			summaryMap.forEach((key, assertion) ->{
//				System.out.print(key + ":");
//				System.out.println(assertion);
//			});
			
			System.out.print("Precision:");
			System.out.println(( (float)summaryMap.get(TRUE_POSITIVE)) / 
					(float)(summaryMap.get(TRUE_POSITIVE) + summaryMap.get(FALSE_POSITIVE)));
	
			System.out.print("Recall:");
			System.out.println(( (float)summaryMap.get(TRUE_POSITIVE)) / 
					(float)(summaryMap.get(TRUE_POSITIVE) + summaryMap.get(FALSE_NEGATIVE)));
			return evaluations;
		}

		public void incSummary(AssertionResult result) {
			int actual = summaryMap.get(result);
			summaryMap.put(result, ++actual);
		}
		
		protected IRepository getRepo(){
			return TASRepository.getRepo();
		}
	}
