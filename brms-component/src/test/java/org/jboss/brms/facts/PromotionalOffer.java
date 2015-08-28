package org.jboss.brms.facts;

public class PromotionalOffer {

	private int zipCode;
	private double transactionMinimum;
	private double rewardAmount;

	public PromotionalOffer(int zipCode, double transactionMinimum, double rewardAmount) {
		super();
		this.zipCode = zipCode;
		this.transactionMinimum = transactionMinimum;
		this.rewardAmount = rewardAmount;
	}

	public PromotionalOffer() {

	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public double getTransactionMinimum() {
		return transactionMinimum;
	}

	public void setTransactionMinimum(double transactionMinimum) {
		this.transactionMinimum = transactionMinimum;
	}

	public double getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(double rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	@Override
	public String toString() {
		return "PromotionalOffer [zipCode=" + zipCode + ", transactionMinimum=" + transactionMinimum + ", rewardAmount=" + rewardAmount + "]";
	}

}
