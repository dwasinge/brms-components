package org.jboss.brms.facts;

public class GenericResponse {

	private String responseString;
	private Integer responseInteger;

	public GenericResponse() {}

	public GenericResponse(String responseString, Integer responseInteger) {
		this.responseString = responseString;
		this.responseInteger = responseInteger;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	public Integer getResponseInteger() {
		return responseInteger;
	}

	public void setResponseInteger(Integer responseInteger) {
		this.responseInteger = responseInteger;
	}

	@Override
	public String toString() {
		return "GenericResponse [responseString=" + responseString
				+ ", responseInteger=" + responseInteger + "]";
	}

}
