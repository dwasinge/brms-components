package org.jboss.brms.facts;

import java.util.Collection;

import org.jboss.brms.api.KieQuery;

public class Rewards {

	@KieQuery(binding="$reward", queryName="Get All Rewards")
	private Collection<PromotionalReward> rewards;

	public Collection<PromotionalReward> getRewards() {
		return rewards;
	}

	public void setRewards(Collection<PromotionalReward> rewards) {
		this.rewards = rewards;
	}

}
