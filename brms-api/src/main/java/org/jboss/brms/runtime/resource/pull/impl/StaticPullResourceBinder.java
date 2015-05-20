package org.jboss.brms.runtime.resource.pull.impl;

import org.jboss.brms.runtime.resource.pull.IPullResourceFactory;

public class StaticPullResourceBinder {

	private static final StaticPullResourceBinder SINGLETON = new StaticPullResourceBinder();

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticPullResourceBinder getSingleton() {
		return SINGLETON;
	}

	private StaticPullResourceBinder() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public IPullResourceFactory getPullResourceFactory() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public String getPullResourceFactoryClassStr() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

}
