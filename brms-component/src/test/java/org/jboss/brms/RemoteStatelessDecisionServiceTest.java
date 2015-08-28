package org.jboss.brms;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.brms.api.StatelessDecisionService;
import org.jboss.brms.builder.BrmsHelper;
import org.jboss.brms.builder.RuntimeType;
import org.jboss.brms.facts.GenericResponse;
import org.jboss.brms.response.TestResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;
import org.kie.api.builder.helper.FluentKieModuleDeploymentHelper;
import org.kie.api.builder.helper.KieModuleDeploymentHelper;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieSessionModel;

/**
 * 
 * These test require a local decision service running. Future versions will
 * hopefully leverage a cloud somewhere and only require network access, or
 * we'll figure out how to embed.
 * 
 */
public class RemoteStatelessDecisionServiceTest {

	private StatelessDecisionService service = BrmsHelper.newStatelessDecisionServiceBuilder().runtimeType(RuntimeType.REMOTE).build();

	private StatelessDecisionService service2 = BrmsHelper.newStatelessDecisionServiceBuilder().runtimeType(RuntimeType.REMOTE)
																								.url("http://localhost:8080/kie-server/services/rest/server")
																								.kieContainerId("testing")
																								.build();

	@Test
	@Ignore
	public void shouldDynamicallyDeployTestRulesToRemoteServerAndThenReturnCorrectResponse() {
		// Given
		Collection<Object> facts = new ArrayList<>();
		Integer foo = new Integer(1);
		facts.add(foo);

		String time = Long.toString(System.currentTimeMillis()); // ensure we
																	// always
																	// have a
																	// new
																	// artifact
		FluentKieModuleDeploymentHelper helper = KieModuleDeploymentHelper.newFluentInstance();
		createDefaultKieBase(helper);
		helper
			.addResourceFilePath("rules/query.drl")	
			.addResourceFilePath("rules/test.drl")
			.addResourceFilePath("ruleflow/Ruleflow.bpmn")
			.addClass(GenericResponse.class)
			.setGroupId("com.rhc")
			.setArtifactId("test")
			.setVersion(time)
			.createKieJarAndDeployToMaven();

		// When
		service.createOrUpgradeRulesWithVersion("com.rhc", "test", time);
		TestResponse response = service.runRules(facts, "Ruleflow", TestResponse.class);

		// Then
		Assert.assertNotNull(response.getStrings());
		Assert.assertEquals(1, response.getStrings().size());
		Assert.assertEquals("test", response.getFirstString());
	}
	
	@Test
	@Ignore
	public void shouldDynamicallyDeployTestRulesToRemoteServerAndThenReturnCorrectResponseWithNonDefaultValues() {
		// Given
		Collection<Object> facts = new ArrayList<>();
		Integer foo = new Integer(1);
		facts.add(foo);

		String time = Long.toString(System.currentTimeMillis()); // ensure we
																	// always
																	// have a
																	// new
																	// artifact
		FluentKieModuleDeploymentHelper helper = KieModuleDeploymentHelper.newFluentInstance();
		createDefaultKieBase(helper);
		helper.addResourceFilePath("rules/test.drl")
			.addResourceFilePath("rules/query.drl")
			.addResourceFilePath("ruleflow/Ruleflow.bpmn")
			.addClass(GenericResponse.class)
			.setGroupId("com.rhc")
			.setArtifactId("test")
			.setVersion(time)
			.createKieJarAndDeployToMaven();

		// When
		service2.createOrUpgradeRulesWithVersion("com.rhc", "test", time);
		TestResponse response = service2.runRules(facts, "Ruleflow", TestResponse.class);

		// Then
		Assert.assertNotNull(response.getStrings());
		Assert.assertEquals(1, response.getStrings().size());
		Assert.assertEquals("test", response.getFirstString());
	}

	private void createDefaultKieBase(FluentKieModuleDeploymentHelper helper) {
		KieBaseModel kieBaseModel = helper.getKieModuleModel().newKieBaseModel("defaultKieBase").addPackage("*").setDefault(true);
		kieBaseModel.newKieSessionModel("defaultKieSession").setDefault(true);
		kieBaseModel.newKieSessionModel("defaultStatelessKieSession").setType(KieSessionModel.KieSessionType.STATELESS).setDefault(true);
	}
}
