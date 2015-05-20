package org.jboss.brms.runtime;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.brms.runtime.DefaultStatelessDecisionService;
import org.jboss.brms.runtime.StatelessDecisionService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultStatelessDecisionServiceTest {

	private static StatelessDecisionService statelessDecisionService;

	@BeforeClass
	public static void init() {
		statelessDecisionService = new DefaultStatelessDecisionService();
	}

	@Test
	public void shouldFireTestRuleAndReturnRuleListener() {
		// given
		Assert.assertNotNull( statelessDecisionService );
		Collection<Object> facts = new ArrayList<Object>();
		facts.add( new String( "test" ) );

		// when
		TestResponse response = statelessDecisionService.execute( facts, TestResponse.class );

		// then
		Assert.assertNotNull( response );
		Assert.assertNotNull( response.getListener() );
		Assert.assertEquals( 1, response.getListener().getRuleNames().size() );
	}
	
	@Test
	public void shouldFireTestRulesAndRuleNullResponse() {
		// given
		Assert.assertNotNull( statelessDecisionService );
		Collection<Object> facts = new ArrayList<Object>();
		facts.add( new String( "test" ) );

		// when
		String response = statelessDecisionService.execute( facts, String.class );

		// then
		Assert.assertNotNull( response );
	}

}
