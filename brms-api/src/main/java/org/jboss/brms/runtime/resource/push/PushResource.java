package org.jboss.brms.runtime.resource.push;

import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;

public interface PushResource {

	public void pushResources(List<String> resourcesAsStringList, List<String> bpmnAsStringList) throws BrmsException ;

}
