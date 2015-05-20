package org.jboss.brms.runtime.template.impl;

import org.jboss.brms.runtime.template.ITemplateFactory;
import org.jboss.brms.runtime.template.impl.StaticTemplateBinder;
import org.jboss.brms.runtime.template.spi.TemplateFactoryBinder;

public class StaticTemplateBinder implements TemplateFactoryBinder {

	private static final StaticTemplateBinder SINGLETON = new StaticTemplateBinder();

	private static final String loggerFactoryClassStr = EmbeddedTemplateFactory.class.getName();

	private final ITemplateFactory templateFactory;

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticTemplateBinder getSingleton() {
		return SINGLETON;
	}

	private StaticTemplateBinder() {
		templateFactory = new EmbeddedTemplateFactory();
	}

	public ITemplateFactory getTemplateFactory() {
		return templateFactory;
	}

	public String getTemplateFactoryClassStr() {
		return loggerFactoryClassStr;
	}

}
