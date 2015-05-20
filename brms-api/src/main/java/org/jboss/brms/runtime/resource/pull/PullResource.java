package org.jboss.brms.runtime.resource.pull;

import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;

public interface PullResource {

	public List<String> pullResources(List<String> resourceNameList) throws BrmsException ;

}
