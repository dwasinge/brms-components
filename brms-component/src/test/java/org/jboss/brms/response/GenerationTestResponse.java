package org.jboss.brms.response;

import java.util.Collection;

import org.jboss.brms.api.KieQuery;
import org.jboss.brms.facts.GenericResponse;

public class GenerationTestResponse {

	@KieQuery(binding = "$string", queryName = "Get All Strings")
	private Collection<String> strings;

	@KieQuery(binding = "$genericResponseList", queryName = "Get All Generic Responses")
	private Collection<GenericResponse> responses;

	public Collection<String> getStrings() {
		return strings;
	}

	public void setStrings(Collection<String> strings) {
		this.strings = strings;
	}

	public String getFirstString(){
		if (strings == null || strings.isEmpty()){
			return "";
		} else {
			return strings.iterator().next();
		}
	}

	public Collection<GenericResponse> getResponses() {
		return responses;
	}

	public void setResponses(Collection<GenericResponse> responses) {
		this.responses = responses;
	}

	public GenericResponse getFirstResponse() {
		if (responses == null || responses.isEmpty()) {
			return null;
		} else {
			return responses.iterator().next();
		}
	}

}
