package org.jboss.brms.runtime.deploy.impl;

import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.deploy.DeployArtifact;

public class StubbedDeployArtifact implements DeployArtifact {

	private List<String> drlStringList;
	private List<String> bpmnStringList;

	@Override
	public void deployResourcesAsStringsToBrmsRuntime(List<String> drlStringList, List<String> bpmnStringList) throws BrmsException {
		this.drlStringList = drlStringList;
		this.bpmnStringList = bpmnStringList;
	}

	public List<String> getDrlStringList() {
		return drlStringList;
	}

	public List<String> getBpmnStringList() {
		return bpmnStringList;
	}


}
