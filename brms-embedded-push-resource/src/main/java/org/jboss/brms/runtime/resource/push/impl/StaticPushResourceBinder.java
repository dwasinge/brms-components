package org.jboss.brms.runtime.resource.push.impl;

import org.jboss.brms.runtime.resource.push.IPushResourceFactory;
import org.jboss.brms.runtime.resource.push.impl.StaticPushResourceBinder;
import org.jboss.brms.runtime.resource.push.spi.PushResourceFactoryBinder;

public class StaticPushResourceBinder implements PushResourceFactoryBinder {

	private static final StaticPushResourceBinder SINGLETON = new StaticPushResourceBinder();

	private static final String loggerFactoryClassStr = EmbeddedPushResourceFactory.class.getName();

	private final IPushResourceFactory pushResourceFactory;

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticPushResourceBinder getSingleton() {
		return SINGLETON;
	}

	private StaticPushResourceBinder() {
		pushResourceFactory = new EmbeddedPushResourceFactory();
	}

	public IPushResourceFactory getPushResourceFactory() {
		return pushResourceFactory;
	}

	public String getPushResourceFactoryClassStr() {
		return loggerFactoryClassStr;
	}

}
