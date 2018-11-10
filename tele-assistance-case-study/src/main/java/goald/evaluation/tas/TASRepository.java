package goald.evaluation.tas;

import goald.model.util.BundleBuilder;
import goald.repository.IRepository;
import goald.repository.RepositoryBuilder;

public class TASRepository {

	public static IRepository getRepo() {
		return RepositoryBuilder.create()
		.add(
			BundleBuilder.create()
			.identification("ProvideHealthSupport.def")
			.defines("ProvideHealthSupport")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ProvideHealthSupport.impl")
			.provides("ProvideHealthSupport")
			.dependsOn("HandleButtonPush")
			.dependsOn("ProvideAutomatedLifeSupport")
			.build())
		/* 2nd level definitions */
		.add(
			BundleBuilder.create()
			.identification("HandleButtonPush-definition")
			.defines("HandleButtonPush")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ProvideAutomatedLifeSupport-definition")
			.defines("ProvideAutomatedLifeSupport")
			.build())
		/* Provide Automated Life Support plans*/
		.add(
			BundleBuilder.create()
			.identification("GetVitalParams-definition")
			.defines("GetVitalParams")
			.build())
		.add(
			BundleBuilder.create()
			.identification("AnalyzeData-definition")
			.defines("AnalyzeData")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ChangeDose-definition")
			.defines("ChangeDose")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ChangeDrug-definition")
			.defines("ChangeDrug")
			.build())
		.add(
			BundleBuilder.create()
			.identification("SendAlarm-definition")
			.defines("SendAlarm")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ProvideAutomatedLifeSupport")
			.provides("ProvideAutomatedLifeSupport")
			.dependsOn("GetVitalParams")
			.dependsOn("AnalyzeData")
			.dependsOn("ChangeDose")
			.dependsOn("ChangeDrug")
			.dependsOn("SendAlarm")
			.build())
		/* Provide Automated Life Support dependencies impl */
		.add(
			BundleBuilder.create()
			.identification("GetVitalParams-impl")
			.provides("GetVitalParams")
			.build())
		.add(
			BundleBuilder.create()
			.identification("AnalyzeData-impl")
			.provides("AnalyzeData")
			.requires("internet-connection")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ChangeDose-impl")
			.provides("ChangeDose")
			.requires("internet-connection")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ChangeDrug-impl")
			.provides("ChangeDrug")
			.requires("internet-connection")
			.build())
		.add(
			BundleBuilder.create()
			.identification("SendAlarm-impl")
			.provides("SendAlarm")
			.requires("internet-connection")
			.build())
		/* Handle Button Push Impl */
		.add(
			BundleBuilder.create()
			.identification("HandleButtonPush-impl")
			.provides("HandleButtonPush")
			.dependsOn("SendAlarm")
			.build())
		.build();
	}
}
