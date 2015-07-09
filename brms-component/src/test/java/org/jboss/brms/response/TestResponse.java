package org.jboss.brms.response;

import java.util.Collection;

import org.jboss.brms.api.KieQuery;

public class TestResponse {

	@KieQuery(binding = "$string", queryName = "Get All Strings")
	private Collection<String> strings;

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

}
