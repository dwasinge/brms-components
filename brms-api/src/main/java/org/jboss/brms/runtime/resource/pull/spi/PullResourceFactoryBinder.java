package org.jboss.brms.runtime.resource.pull.spi;

import org.jboss.brms.runtime.resource.pull.IPullResourceFactory;

public interface PullResourceFactoryBinder {

	public IPullResourceFactory getPullResourceFactory();

	public String getPullResourceFactoryClassStr();

}
