package org.jboss.brms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.brms.api.RuleGeneratorService;
import org.jboss.brms.api.StatelessDecisionService;
import org.jboss.brms.builder.BrmsHelper;
import org.jboss.brms.builder.RuleGeneratorHelper;
import org.jboss.brms.exception.BrmsException;
import org.jboss.brms.facts.PromotionalOffer;
import org.jboss.brms.facts.PromotionalReward;
import org.jboss.brms.facts.Purchase;
import org.jboss.brms.facts.Rewards;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataDrivenRulesGenerationTest {

	private static final String GROUP_ID = "com.redhat";
	private static final String ARTIFACT_ID = "offers";
	private static String version = "";
	private static StatelessDecisionService decisionService = BrmsHelper.newStatelessDecisionServiceBuilder().auditLogName("audit").build();
	
	@BeforeClass
	public static void init() throws BrmsException{
		version = generateOffersRules();
		Assert.assertTrue( decisionService.createOrUpgradeRulesWithVersion(GROUP_ID, ARTIFACT_ID, version) );
		System.out.println( decisionService.getCurrentRulesVersion() );
	}
	
	@Test
	public void test() throws BrmsException {
		// given
		Collection<Object> facts = getPurchases();
		
		// when
		Rewards rewards = decisionService.runRules(facts, "Offers", Rewards.class);

		// then
		Assert.assertNotNull(rewards);
		Assert.assertNotNull(rewards.getRewards());
		Assert.assertEquals(2, rewards.getRewards().size());
	}
	
	private static Collection<PromotionalOffer> getOffers(){
		
		// TODO provide a JPA implementation
		
		Collection<PromotionalOffer> offers = new ArrayList<>();
		offers.add( new PromotionalOffer(10002, 150.00, 10.00));
		offers.add( new PromotionalOffer(21228, 100.00, 10.00));
		return offers;
	}
	
	private static Collection<Object> getPurchases(){
		Collection<Object> purchases = new ArrayList<>();
		purchases.add( new Purchase(10002, 120.00) );
		purchases.add( new Purchase(10002, 170.00) );
		purchases.add( new Purchase(21228, 70.00) );
		purchases.add( new Purchase(21228, 120.00) );
		return purchases;
	}
	
	private static String generateOffersRules() throws BrmsException{
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("offers", getOffers());

		// Add required classes for Kjar
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(PromotionalOffer.class);
		classes.add(PromotionalReward.class);
		classes.add(Purchase.class);

		// Get Rule Generation Service
		RuleGeneratorService ruleGeneratorService =
				RuleGeneratorHelper
					.newBrmsGeneratorBuilder()
					.kjarGroupId(GROUP_ID)
					.kjarArtifactId(ARTIFACT_ID)
					.templatePath("offers/templates")
					.ruleflowPath("offers/ruleflow")
					.rulePath("offers/rules")
					.classes(classes)
					.inputMap(inputMap)
					.build();

	    // Generate Kjar and deploy to Maven repo
		return ruleGeneratorService.generateKjarFromTemplates();
	}
}
