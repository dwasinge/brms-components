package org.jboss.brms.runtime.impl;

import org.jboss.brms.runtime.IStatelessDecisionServiceFactory;
import org.jboss.brms.runtime.StatelessDecisionService;

public class EmbeddedStatelessDecisionServiceFactory implements IStatelessDecisionServiceFactory {

	private EmbeddedStatelessDecisionService embeddedStatelessDecisionService;

	public EmbeddedStatelessDecisionServiceFactory() {
		embeddedStatelessDecisionService = new EmbeddedStatelessDecisionService();
	}

	@Override
	public StatelessDecisionService getStatelessDecisionService() {
		return embeddedStatelessDecisionService;
	}

}
