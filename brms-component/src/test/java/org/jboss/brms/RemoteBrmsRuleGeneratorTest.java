package org.jboss.brms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.brms.api.RuleGeneratorService;
import org.jboss.brms.builder.RuleGeneratorHelper;
import org.jboss.brms.exception.BrmsException;
import org.jboss.brms.facts.GenericRequest;
import org.jboss.brms.facts.GenericResponse;
import org.jboss.brms.response.GenerationTestResponse;
import org.jboss.brms.test.KieTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;

public class RemoteBrmsRuleGeneratorTest extends KieTestSupport {

	@Test
	@Ignore
	public void testRuleGenerationWithRemoteDecisionService() throws BrmsException {

		// Create map for template input
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("year", "2015");

		// Add required classes for Kjar
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(GenericRequest.class);
		classes.add(GenericResponse.class);

		// Get Rule Generation Service
		RuleGeneratorService ruleGeneratorService =
				RuleGeneratorHelper
					.newBrmsGeneratorBuilder()
					.kjarGroupId(GROUP_ID)
					.kjarArtifactId(ARTIFACT_ID)
					.templatePath("templates")
					.ruleflowPath("ruleflow")
					.rulePath("rules")
					.classes(classes)
					.inputMap(inputMap)
					.build();

		// Generate Kjar and deploy to Maven repo
		String version = ruleGeneratorService.generateKjarFromTemplates();

		// Load Kjar into engine
		getRemoteDecisionServiceWithDefaultValues().createOrUpgradeRulesWithVersion(GROUP_ID, ARTIFACT_ID, version);

		// Create facts
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("year", "2015");
		GenericRequest request = new GenericRequest("one", 1);

		Collection<Object> facts = new ArrayList<Object>();
		facts.add(requestMap);
		facts.add(request);

		// Execute Rules against facts
		GenerationTestResponse response = getRemoteDecisionServiceWithDefaultValues().runRules(facts, RULEFLOW, GenerationTestResponse.class);

		// Validate Response
		Assert.assertNotNull(response);
		Assert.assertEquals(1, response.getResponses().size());
		Assert.assertEquals("one", response.getFirstResponse().getResponseString());
		Assert.assertEquals(Integer.valueOf(1), response.getFirstResponse().getResponseInteger());

	}

}
