package org.jboss.brms.remote;

import org.jboss.brms.api.StatelessDecisionService;

public class RemoteStatelessDecisionServiceFactory {

	private static final String DEFAULT_URL = "http://localhost:8080/kie-server/services/rest/server";
	private static final String DEFAULT_CONTAINER_ID = "default";
	private static final String DEFAULT_USER = null;
	private static final String DEFAUTL_PASSWORD = null;
	private static final int DEFAULT_TIMEOUT_IN_SECONDS = 0;  // this will use the BRMS default, 5 seconds as of 6.1.0
	
	public static StatelessDecisionService getDefaultRemoteService() {
		return new RemoteStatelessDecisionService(DEFAULT_URL, DEFAULT_USER, DEFAUTL_PASSWORD, DEFAULT_TIMEOUT_IN_SECONDS, DEFAULT_CONTAINER_ID);
	}

}
