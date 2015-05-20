package org.jboss.brms.runtime.deploy;

import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;

public interface DeployArtifact {

	public void deployResourcesAsStringsToBrmsRuntime( List<String> drlStringList, List<String> bpmnStringList ) throws BrmsException;

}
