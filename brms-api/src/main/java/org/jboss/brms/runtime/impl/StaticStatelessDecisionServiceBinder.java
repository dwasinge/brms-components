package org.jboss.brms.runtime.impl;

import org.jboss.brms.runtime.IStatelessDecisionServiceFactory;

public class StaticStatelessDecisionServiceBinder {

	private static final StaticStatelessDecisionServiceBinder SINGLETON = new StaticStatelessDecisionServiceBinder();

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticStatelessDecisionServiceBinder getSingleton() {
		return SINGLETON;
	}

	private StaticStatelessDecisionServiceBinder() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public IStatelessDecisionServiceFactory getStatelessDecisionServiceFactory() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public String getStatelessDecisionServiceFactoryClassStr() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

}
