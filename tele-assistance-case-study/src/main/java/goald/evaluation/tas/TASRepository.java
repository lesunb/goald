package goald.evaluation.tas;

import goald.model.util.BundleBuilder;
import goald.repository.IRepository;
import goald.repository.RepositoryBuilder;

public class TASRepository {

	public static IRepository getRepo() {
		return RepositoryBuilder.create()
		.add(
			BundleBuilder.create()
			.identification("ProvideHalthSupport-def")
			.defines("ProvideHealthSupport")
			.build())
		/* Provide Automated Life Support plans*/
		.add(
			BundleBuilder.create()
			.identification("ProvideHalthSupport-impl")
			.provides("ProvideHealthSupport")
			.dependsOnAny("ProvideSelfDiagnosedEmergenciesSupport", "ProvideAutomatedLifeSupport")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ProvideAutomatedLifeSupport-def")
			.defines("ProvideAutomatedLifeSupport")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ProvideAutomatedLifeSupport-impl")
			.provides("ProvideAutomatedLifeSupport")
			.withQuality("precision", 10)
			.withQuality("responseTime", 10)
			.dependsOn("MonitorPatient")
			.dependsOnCond("!patient-is-ok", "EnactTreatment")
			.condition("!battery-is-low")
			.build())
		.add(
			BundleBuilder.create()
			.identification("MonitorPatient-def")
			.defines("MonitorPatient")
			.build())
		.add(
			BundleBuilder.create()
			.identification("EnactTreatment-def")
			.defines("EnactTreatment")
			.build())
		/* Monitor Patient */
		.add(
			BundleBuilder.create()
			.identification("MonitorPatient-impl")
			.provides("MonitorPatient")
			.dependsOn("GetVitalParams")
			.dependsOn("AnalyzeData")
			.build())
		.add(
			BundleBuilder.create()
			.identification("GetVitalParams-def")
			.defines("GetVitalParams")
			.build())
		.add(
			BundleBuilder.create()
			.identification("GetSensedData-impl")
			.provides("GetVitalParams")
			.build())
		.add(
			BundleBuilder.create()
			.identification("AnalyzeData-def")
			.defines("AnalyzeData")
			.build())
		.add(
			BundleBuilder.create()
			.identification("LocalAnalysis-impl")
			.provides("AnalyzeData")
			.withQuality("precision", 8)
			.withQuality("responseTime", 3)
			.build())
		.add(
			BundleBuilder.create()
			.identification("RemoteAnalysis-impl")
			.provides("AnalyzeData")
			.condition("internet-connection")
			.withQuality("precision", 10)
			.withQuality("responseTime", 5)
			.build())
		/* Enact Treatment */
		.add(
			BundleBuilder.create()
			.identification("EnactTreatment-impl")
			.provides("EnactTreatment")
			.condition("!patient-is-ok")
			.dependsOnAny("AdministerMedicine", "NotifyEmergencyMedicalServices")
			.build())
		.add(
			BundleBuilder.create()
			.identification("AdministerMedicine-def")
			.defines("AdministerMedicine")
			.build())
		.add(
			BundleBuilder.create()
			.identification("AdministerMedicine-impl")
			.provides("AdministerMedicine")
			.dependsOnAny("ChangeDose", "ChangeDrug")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ChangeDrug-def")
			.defines("ChangeDrug")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ChangeDose-def")
			.defines("ChangeDose")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ChangeDrug-impl")
			.provides("ChangeDrug")
			.condition("doctor-is-present")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ChangeDose-impl")
			.provides("ChangeDose")
			.condition("drug-is-available")
			.build())
		/* Panic Button Impl */
		.add(
			BundleBuilder.create()
			.identification("ProvideSelfDiagnosedEmergenciesSupport-def")
			.defines("ProvideSelfDiagnosedEmergenciesSupport")
			.build())
		.add(
			BundleBuilder.create()
			.identification("ProvideSelfDiagnosedEmergenciesSupport-impl")
			.provides("ProvideSelfDiagnosedEmergenciesSupport")
			.dependsOn("AllowPushButton")
			.dependsOn("NotifyEmergencyMedicalServices")
			.withQuality("precision", 5)
			.withQuality("responseTime", 5)
			.build())
		.add(
			BundleBuilder.create()
			.identification("AllowPushButton-def")
			.defines("AllowPushButton")
			.build())
		.add(
			BundleBuilder.create()
			.identification("PushButton-impl")
			.provides("AllowPushButton")
			.build())
		.add(
			BundleBuilder.create()
			.identification("NotifyEmergencyMedicalServices-def")
			.defines("NotifyEmergencyMedicalServices")
			.build())
		.add(
			BundleBuilder.create()
			.identification("SendSMS-impl")
			.provides("NotifyEmergencyMedicalServices")
			.withQuality("precision", 5)
			.withQuality("responseTime", 3)
			.build())
		.add(
			BundleBuilder.create()
			.identification("AlarmService-impl")
			.provides("NotifyEmergencyMedicalServices")
			.condition("internet-connection")
			.withQuality("precision", 10)
			.withQuality("responseTime", 5)
			.build())
		.build();
	}
}
