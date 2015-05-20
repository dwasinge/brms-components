package org.jboss.brms.runtime.resource.push.impl;

import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.deploy.DeployArtifact;
import org.jboss.brms.runtime.deploy.DeployArtifactFactory;
import org.jboss.brms.runtime.resource.push.PushResource;

public class EmbeddedPushResource implements PushResource {

	private DeployArtifact deployArtifact;

	public EmbeddedPushResource() {
		deployArtifact = DeployArtifactFactory.getDeployArtifact();
	}

	public EmbeddedPushResource( DeployArtifact deployArtifact) {
		this.deployArtifact = deployArtifact;
	}

	@Override
	public void pushResources(List<String> resourcesAsStringList, List<String> bpmnAsStringList) throws BrmsException {
		deployArtifact.deployResourcesAsStringsToBrmsRuntime(resourcesAsStringList, bpmnAsStringList);
	}

}
