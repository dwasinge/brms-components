package org.jboss.brms.runtime.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.brms.runtime.StatelessDecisionService;
import org.jboss.brms.runtime.TestResponse;
import org.jboss.brms.runtime.impl.EmbeddedStatelessDecisionServiceFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedStatelessDecisionServiceFactoryTest {

	private EmbeddedStatelessDecisionServiceFactory embeddedStatelessDecisionServiceFactory;

	@Before
	public void setup() {
		embeddedStatelessDecisionServiceFactory = new EmbeddedStatelessDecisionServiceFactory();
	}

	@After
	public void tearDown() {
		embeddedStatelessDecisionServiceFactory = null;
	}

	@Test
	public void testEmbeddedStatelessDecisionFactory() {
		Assert.assertNotNull(embeddedStatelessDecisionServiceFactory.getStatelessDecisionService());
	}

	@Test
	public void testFireTestRuleAndReturnRuleListener() {

		StatelessDecisionService statelessDecisionService = embeddedStatelessDecisionServiceFactory.getStatelessDecisionService();
		Assert.assertNotNull(statelessDecisionService);

		Collection<Object> facts = new ArrayList<Object>();
		facts.add(new String("test"));

		TestResponse response = statelessDecisionService.execute(facts, TestResponse.class);

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getListener());
		Assert.assertEquals(1, response.getListener().getRuleNames().size());

	}

	@Test
	public void testFireTestRulesAndRuleNullResponse() {

		StatelessDecisionService statelessDecisionService = embeddedStatelessDecisionServiceFactory.getStatelessDecisionService();
		Assert.assertNotNull(statelessDecisionService);

		Collection<Object> facts = new ArrayList<Object>();
		facts.add(new String("test"));

		String response = statelessDecisionService.execute(facts, String.class);

		Assert.assertNotNull(response);

	}

}
