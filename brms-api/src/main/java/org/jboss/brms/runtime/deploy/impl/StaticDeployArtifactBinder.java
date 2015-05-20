package org.jboss.brms.runtime.deploy.impl;

import org.jboss.brms.runtime.deploy.IDeployArtifactFactory;

public class StaticDeployArtifactBinder {

	private static final StaticDeployArtifactBinder SINGLETON = new StaticDeployArtifactBinder();

	public static String REQUESTED_API_VERSION = "1.1.0";

	public static final StaticDeployArtifactBinder getSingleton() {
		return SINGLETON;
	}

	private StaticDeployArtifactBinder() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public IDeployArtifactFactory getDeployArtifactFactory() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

	public String getDeployArtifactFactoryClassStr() {
		throw new UnsupportedOperationException("This code should have never made it into brms-api.jar ");
	}

}
