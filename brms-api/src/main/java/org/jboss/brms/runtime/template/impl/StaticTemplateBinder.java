package org.jboss.brms.runtime.template.impl;

import org.jboss.brms.runtime.template.ITemplateFactory;

public class StaticTemplateBinder {

	private static final StaticTemplateBinder SINGLETON = new StaticTemplateBinder();

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticTemplateBinder getSingleton() {
		return SINGLETON;
	}

	private StaticTemplateBinder() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public ITemplateFactory getTemplateFactory() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public String getTemplateFactoryClassStr() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

}
