package org.jboss.brms.runtime.impl;

import org.jboss.brms.runtime.IStatelessDecisionServiceFactory;
import org.jboss.brms.runtime.impl.StaticStatelessDecisionServiceBinder;
import org.jboss.brms.runtime.spi.StatelessDecisionServiceFactoryBinder;

public class StaticStatelessDecisionServiceBinder implements StatelessDecisionServiceFactoryBinder {

	private static final StaticStatelessDecisionServiceBinder SINGLETON = new StaticStatelessDecisionServiceBinder();

	private static final String loggerFactoryClassStr = EmbeddedStatelessDecisionServiceFactory.class.getName();

	private final IStatelessDecisionServiceFactory statelessDecisionServiceFactory;

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticStatelessDecisionServiceBinder getSingleton() {
		return SINGLETON;
	}

	private StaticStatelessDecisionServiceBinder() {
		statelessDecisionServiceFactory = new EmbeddedStatelessDecisionServiceFactory();
	}

	public IStatelessDecisionServiceFactory getStatelessDecisionServiceFactory() {
		return statelessDecisionServiceFactory;
	}

	public String getStatelessDecisionServiceFactoryClassStr() {
		return loggerFactoryClassStr;
	}

}
