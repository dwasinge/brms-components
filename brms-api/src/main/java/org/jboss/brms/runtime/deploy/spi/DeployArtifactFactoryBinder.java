package org.jboss.brms.runtime.deploy.spi;

import org.jboss.brms.runtime.deploy.IDeployArtifactFactory;

public interface DeployArtifactFactoryBinder {

	public IDeployArtifactFactory getDeployArtifactFactory();

	public String getDeployArtifactFactoryClassStr();

}
