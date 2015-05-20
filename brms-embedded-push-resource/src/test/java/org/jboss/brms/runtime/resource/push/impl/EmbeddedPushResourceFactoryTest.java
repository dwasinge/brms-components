package org.jboss.brms.runtime.resource.push.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.deploy.impl.StubbedDeployArtifact;
import org.jboss.brms.runtime.resource.push.PushResource;
import org.jboss.brms.runtime.resource.push.impl.EmbeddedPushResource;
import org.jboss.brms.runtime.resource.push.impl.EmbeddedPushResourceFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedPushResourceFactoryTest {

	private EmbeddedPushResource embeddedPushResource;
	private EmbeddedPushResourceFactory embeddedPushResourceFactory;
	private StubbedDeployArtifact stubbedDeployArtifact;

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

		stubbedDeployArtifact = new StubbedDeployArtifact();
		embeddedPushResource = new EmbeddedPushResource(stubbedDeployArtifact);
		embeddedPushResourceFactory = new EmbeddedPushResourceFactory(embeddedPushResource);

	}

	@After
	public void tearDown() {

		stubbedDeployArtifact = null;
		embeddedPushResource = null;
		embeddedPushResourceFactory = null;

	}

	@Test
	public void testEmbeddedPushResourceFactory() throws BrmsException {

		// Push the Generated DRL
		List<String> generatedDrlList = new ArrayList<String>( Arrays.asList( generatedDrl ) );
		List<String> bpmnList = new ArrayList<String>();

		PushResource pushService = embeddedPushResourceFactory.getPushResource();
		pushService.pushResources(generatedDrlList, bpmnList);

		// Validate List Sizes
		Assert.assertEquals(0, stubbedDeployArtifact.getBpmnStringList().size());
		Assert.assertEquals(1, stubbedDeployArtifact.getDrlStringList().size());

		// Validate Content
		Assert.assertEquals(generatedDrl, stubbedDeployArtifact.getDrlStringList().get(0));

	}

}
