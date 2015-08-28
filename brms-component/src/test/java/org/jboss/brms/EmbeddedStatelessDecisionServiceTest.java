package org.jboss.brms;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.brms.api.StatelessDecisionService;
import org.jboss.brms.builder.BrmsHelper;
import org.jboss.brms.response.TestResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:brms-test-context.xml" })
public class EmbeddedStatelessDecisionServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private StatelessDecisionService springService;
	
	@Test
	public void shouldRunTestRulesAndReturnATestResponse() {
		// Given
		StatelessDecisionService service = BrmsHelper.newStatelessDecisionServiceBuilder().build();
		Collection<Object> facts = new ArrayList<>();
		Integer foo = new Integer(1);
		facts.add(foo);

		// When
		TestResponse response = service.runRules(facts, "Ruleflow", TestResponse.class);

		// Then
		Assert.assertNotNull(response.getStrings());
		Assert.assertEquals(1, response.getStrings().size());
		Assert.assertEquals("test", response.getFirstString());
	}
	
	
	@Test
	public void shouldRunTestRulesAndReturnATestResponseWithAuditLogAndDebug() {
		// Given
		StatelessDecisionService service = BrmsHelper.newStatelessDecisionServiceBuilder().auditLogName("audit").enableDebugLogging().build();
		Collection<Object> facts = new ArrayList<>();
		Integer foo = new Integer(1);
		facts.add(foo);

		// When
		TestResponse response = service.runRules(facts, "Ruleflow", TestResponse.class);

		// Then
		Assert.assertNotNull(response.getStrings());
		Assert.assertEquals(1, response.getStrings().size());
		Assert.assertEquals("test", response.getFirstString());
	}
	
	@Test
	public void shouldRunTestRulesAndReturnATestResponseWithDebugWithSpring() {
		// Given
		Collection<Object> facts = new ArrayList<>();
		Integer foo = new Integer(1);
		facts.add(foo);

		// When
		TestResponse response = springService.runRules(facts, "Ruleflow", TestResponse.class);

		// Then
		Assert.assertNotNull(response.getStrings());
		Assert.assertEquals(1, response.getStrings().size());
		Assert.assertEquals("test", response.getFirstString());
	}
}
