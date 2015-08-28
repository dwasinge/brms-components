package org.jboss.brms.facts;

public class PromotionalReward {

	private double amount;

	public PromotionalReward(double amount) {
		this.amount = amount;
	}
	
	public PromotionalReward(){
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
