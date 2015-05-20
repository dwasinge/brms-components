package org.jboss.brms.runtime.template.impl;

import java.util.HashMap;
import java.util.Map;

import org.jboss.brms.runtime.template.Template;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

public class EmbeddedTemplate implements Template {

	@Override
	public String generateResourceFromTemplate(String template, Map<String, Object> inputMap) {

		CompiledTemplate compiledTemplate = TemplateCompiler.compileTemplate(template);
		return (String) TemplateRuntime.execute(compiledTemplate, inputMap, new HashMap<String, Object>());

	}

}
