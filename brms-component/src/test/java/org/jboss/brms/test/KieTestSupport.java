package org.jboss.brms.test;

import org.jboss.brms.api.StatelessDecisionService;
import org.jboss.brms.builder.BrmsHelper;
import org.jboss.brms.builder.RuntimeType;

public class KieTestSupport {

	public static final String CONTAINER_ID = "test";
	public static final String GROUP_ID = "com.rhc";
	public static final String ARTIFACT_ID = "test";
	public static final String RULEFLOW = "Ruleflow";
	public static final String KIE_SERVER_URL = "http://localhost:8080/kie-server/services/rest/server";

	private StatelessDecisionService remoteServiceWithDefaultValues = null;
	private StatelessDecisionService remoteServiceWithNonDefaultValues = null;
	private StatelessDecisionService embeddedService = null;

	public StatelessDecisionService getRemoteDecisionServiceWithDefaultValues() {
		if(remoteServiceWithDefaultValues != null) {
			return remoteServiceWithDefaultValues;
		}
		remoteServiceWithDefaultValues = 
			BrmsHelper
				.newStatelessDecisionServiceBuilder()
				.runtimeType(RuntimeType.REMOTE)
				.build();
		return remoteServiceWithDefaultValues;
	}

	public StatelessDecisionService getRemoteDecisionServiceWithNonDefaultValues() {
		if(remoteServiceWithNonDefaultValues != null) {
			return remoteServiceWithNonDefaultValues;
		}
		remoteServiceWithNonDefaultValues =
			BrmsHelper.newStatelessDecisionServiceBuilder().runtimeType(RuntimeType.REMOTE)
				.url("http://localhost:8080/kie-server/services/rest/server")
				.kieContainerId("testing")
				.build();
		return remoteServiceWithNonDefaultValues;
	}

	public StatelessDecisionService getEmbeddedDecisionService(boolean newService) {
		if(!newService && embeddedService != null) {
			return embeddedService;
		}
		embeddedService = 
			BrmsHelper.newStatelessDecisionServiceBuilder().build();
		return embeddedService;
	}

}
