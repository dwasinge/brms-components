package org.jboss.brms.api;

import java.util.Collection;

public interface StatelessDecisionService {

	< Response > Response runRules( Collection<Object> facts, String processId, Class< Response > responseClazz );
	
	boolean createOrUpgradeRulesWithVersion(String group, String artifact, String version);
	
	String getCurrentRulesVersion();

}