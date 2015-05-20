package org.jboss.brms.runtime.template.impl;

import org.jboss.brms.runtime.template.ITemplateFactory;
import org.jboss.brms.runtime.template.Template;

public class EmbeddedTemplateFactory implements ITemplateFactory {

	private EmbeddedTemplate embeddedTemplate;

	public EmbeddedTemplateFactory() {
		embeddedTemplate = new EmbeddedTemplate();
	}

	@Override
	public Template getTemplate() {
		return embeddedTemplate;
	}

}
