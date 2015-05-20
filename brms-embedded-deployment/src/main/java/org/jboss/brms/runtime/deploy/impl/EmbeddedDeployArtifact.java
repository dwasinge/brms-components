package org.jboss.brms.runtime.deploy.impl;

import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.StatelessDecisionService;
import org.jboss.brms.runtime.StatelessDecisionServiceFactory;
import org.jboss.brms.runtime.deploy.DeployArtifact;

public class EmbeddedDeployArtifact implements DeployArtifact {

	private StatelessDecisionService statelessDecisionService;

	public EmbeddedDeployArtifact() {
		this.statelessDecisionService = StatelessDecisionServiceFactory.getStatelessDecisionService();
	}

	public EmbeddedDeployArtifact(StatelessDecisionService statelessDecisionService) {
		this.statelessDecisionService = statelessDecisionService;
	}

	@Override
	public void deployResourcesAsStringsToBrmsRuntime(List<String> drlStringList, List<String> bpmnStringList) throws BrmsException {
		statelessDecisionService.createNewKieContainer(drlStringList, bpmnStringList);
	}


}
