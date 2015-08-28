package org.jboss.brms.facts;

public class Purchase {

	private int zipCode;
	private double transactionAmount;

	public Purchase(int zipCode, double transactionAmount) {
		super();
		this.zipCode = zipCode;
		this.transactionAmount = transactionAmount;
	}

	public Purchase() {
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

}
