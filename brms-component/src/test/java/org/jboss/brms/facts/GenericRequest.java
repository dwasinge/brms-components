package org.jboss.brms.facts;

public class GenericRequest {

	private String requestString;
	private Integer requestInteger;

	public GenericRequest() {}

	public GenericRequest(String requestString, Integer requestInteger) {
		this.requestString = requestString;
		this.requestInteger = requestInteger;
	}

	public String getRequestString() {
		return requestString;
	}

	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	public Integer getRequestInteger() {
		return requestInteger;
	}

	public void setRequestInteger(Integer requestInteger) {
		this.requestInteger = requestInteger;
	}

	@Override
	public String toString() {
		return "GenericRequest [requestString=" + requestString
				+ ", requestInteger=" + requestInteger + "]";
	}

}
