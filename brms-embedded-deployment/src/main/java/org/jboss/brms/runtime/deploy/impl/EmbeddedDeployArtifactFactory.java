package org.jboss.brms.runtime.deploy.impl;

import org.jboss.brms.runtime.deploy.DeployArtifact;
import org.jboss.brms.runtime.deploy.IDeployArtifactFactory;

public class EmbeddedDeployArtifactFactory implements IDeployArtifactFactory {

	private EmbeddedDeployArtifact embeddedDeployArtifact;

	public EmbeddedDeployArtifactFactory() {
		this.embeddedDeployArtifact = new EmbeddedDeployArtifact();
	}

	public EmbeddedDeployArtifactFactory(EmbeddedDeployArtifact embeddedDeployArtifact) {
		this.embeddedDeployArtifact = embeddedDeployArtifact;
	}

	@Override
	public DeployArtifact getDeployArtifact() {
		return embeddedDeployArtifact;
	}

}
