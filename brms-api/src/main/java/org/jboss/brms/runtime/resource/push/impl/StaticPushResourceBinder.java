package org.jboss.brms.runtime.resource.push.impl;

import org.jboss.brms.runtime.resource.push.IPushResourceFactory;

public class StaticPushResourceBinder {

	private static final StaticPushResourceBinder SINGLETON = new StaticPushResourceBinder();

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticPushResourceBinder getSingleton() {
		return SINGLETON;
	}

	private StaticPushResourceBinder() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public IPushResourceFactory getPushResourceFactory() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public String getPushResourceFactoryClassStr() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

}
