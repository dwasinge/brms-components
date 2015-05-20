package org.jboss.brms.runtime.deploy.impl;

import org.jboss.brms.runtime.deploy.IDeployArtifactFactory;
import org.jboss.brms.runtime.deploy.impl.StaticDeployArtifactBinder;
import org.jboss.brms.runtime.deploy.spi.DeployArtifactFactoryBinder;

public class StaticDeployArtifactBinder implements DeployArtifactFactoryBinder {

	private static final StaticDeployArtifactBinder SINGLETON = new StaticDeployArtifactBinder();

	private static final String loggerFactoryClassStr = EmbeddedDeployArtifactFactory.class.getName();

	private final IDeployArtifactFactory deployArtifactFactory;

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticDeployArtifactBinder getSingleton() {
		return SINGLETON;
	}

	private StaticDeployArtifactBinder() {
		deployArtifactFactory = new EmbeddedDeployArtifactFactory();
	}

	public IDeployArtifactFactory getDeployArtifactFactory() {
		return deployArtifactFactory;
	}

	public String getDeployArtifactFactoryClassStr() {
		return loggerFactoryClassStr;
	}

}
