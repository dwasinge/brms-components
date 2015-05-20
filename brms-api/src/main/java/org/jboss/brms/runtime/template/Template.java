package org.jboss.brms.runtime.template;

import java.util.Map;

public interface Template {

	public String generateResourceFromTemplate(String template, Map<String, Object> inputMap);

}
