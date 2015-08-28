package org.jboss.brms;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.brms.api.StatelessDecisionService;
import org.jboss.brms.builder.BrmsHelper;
import org.jboss.brms.exception.BrmsException;
import org.jboss.brms.facts.PromotionalReward;
import org.jboss.brms.facts.Rewards;
import org.jboss.brms.test.PromotionTestUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.KieResources;

public class RuleJoinTest {

	private static StatelessDecisionService decisionService = BrmsHelper.newStatelessDecisionServiceBuilder().auditLogName("audit2").build();

	@BeforeClass
	public static void init() {
		KieFileSystem kfs = KieServices.Factory.get().newKieFileSystem();
		KieResources kieResources = KieServices.Factory.get().getResources();

		kfs.write(kieResources.newClassPathResource("join/test.drl"));
		kfs.write(kieResources.newClassPathResource("offers/rules/query.drl"));
		kfs.write(kieResources.newClassPathResource("offers/ruleflow/Ruleflow.bpmn"));

		KieBuilder builder = KieServices.Factory.get().newKieBuilder(kfs);
		ReleaseId id = builder.buildAll().getKieModule().getReleaseId();

		decisionService.createOrUpgradeRulesWithVersion(id.getGroupId(), id.getArtifactId(), id.getVersion());
	}

	@Test
	public void shouldCreateTwoRewardsForFourPurchases() throws BrmsException {

		// given
		Collection<Object> facts = new ArrayList<>();
		facts.addAll(PromotionTestUtil.getOffers());
		facts.addAll(PromotionTestUtil.getPurchases());

		// when
		Rewards rewards = decisionService.runRules(facts, "Offers", Rewards.class);

		// then
		Assert.assertNotNull(rewards);
		Assert.assertNotNull(rewards.getRewards());
		Assert.assertEquals(2, rewards.getRewards().size());
		for (PromotionalReward reward : rewards.getRewards()) {
			Assert.assertEquals(10.00, reward.getAmount(), 0.001);
		}

	}

}
