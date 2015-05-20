package org.jboss.brms.runtime.deploy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.StatelessDecisionService;
import org.jboss.brms.runtime.deploy.DeployArtifact;
import org.jboss.brms.runtime.deploy.impl.EmbeddedDeployArtifact;
import org.jboss.brms.runtime.deploy.impl.EmbeddedDeployArtifactFactory;
import org.jboss.brms.runtime.impl.EmbeddedStatelessDecisionService;
import org.jboss.brms.runtime.misc.KieResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedDeployArtifactFactoryTest {

	private StatelessDecisionService statelessDecisionService;
	private EmbeddedDeployArtifact embeddedDeployArtifact;
	private EmbeddedDeployArtifactFactory embeddedDeployArtifactFactory;

	private final String generatedDrl = 
			"package test.rules\n" +
			"import java.util.Map;\n\n" +
			"rule \"Test Rule 1\"\n" +
			"	dialect \"mvel\"\n" +
			"	when\n" +
			"		$m : Map( this['year'] == \"2015\")\n" +
			"	then" +
			"		$m['message'] = \"hello\";" +
			"end";

	@Before
	public void setup() {
		statelessDecisionService = new EmbeddedStatelessDecisionService();
		embeddedDeployArtifact = new EmbeddedDeployArtifact(statelessDecisionService);
		embeddedDeployArtifactFactory = new EmbeddedDeployArtifactFactory(embeddedDeployArtifact);
	}

	@After
	public void tearDown() {
		embeddedDeployArtifactFactory = null;
	}

	@Test
	public void testDeployResourcesAsDrlStrings() throws BrmsException {

		Assert.assertNotNull(embeddedDeployArtifactFactory);
		Assert.assertNotNull(statelessDecisionService);

		DeployArtifact deployArtifact = embeddedDeployArtifactFactory.getDeployArtifact();
		Assert.assertNotNull(deployArtifact);
	
		// Deploy the DRL
		List<String> generatedDrlList = new ArrayList<String>( Arrays.asList( generatedDrl ) );
		deployArtifact.deployResourcesAsStringsToBrmsRuntime(generatedDrlList, new ArrayList<String>());

		// Create request
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("year", "2015");
		Collection<Object> facts = new ArrayList<Object>();
		facts.add(inputMap);

		// Run Rules
		statelessDecisionService.execute(facts, KieResponse.class);

		Assert.assertEquals("hello", inputMap.get("message") );

	}

}
