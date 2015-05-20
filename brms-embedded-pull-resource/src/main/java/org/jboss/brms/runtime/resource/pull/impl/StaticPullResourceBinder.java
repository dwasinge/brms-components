package org.jboss.brms.runtime.resource.pull.impl;

import org.jboss.brms.runtime.resource.pull.IPullResourceFactory;
import org.jboss.brms.runtime.resource.pull.impl.StaticPullResourceBinder;
import org.jboss.brms.runtime.resource.pull.spi.PullResourceFactoryBinder;

public class StaticPullResourceBinder implements PullResourceFactoryBinder {

	private static final StaticPullResourceBinder SINGLETON = new StaticPullResourceBinder();

	private static final String loggerFactoryClassStr = EmbeddedPullResourceFactory.class.getName();

	private final IPullResourceFactory pullResourceFactory;

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticPullResourceBinder getSingleton() {
		return SINGLETON;
	}

	private StaticPullResourceBinder() {
		pullResourceFactory = new EmbeddedPullResourceFactory();
	}

	public IPullResourceFactory getPullResourceFactory() {
		return pullResourceFactory;
	}

	public String getPullResourceFactoryClassStr() {
		return loggerFactoryClassStr;
	}



}
