package org.jboss.brms.runtime.spi;

import org.jboss.brms.runtime.IStatelessDecisionServiceFactory;

public interface StatelessDecisionServiceFactoryBinder {

	public IStatelessDecisionServiceFactory getStatelessDecisionServiceFactory();

	public String getStatelessDecisionServiceFactoryClassStr();

}
