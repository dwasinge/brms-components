package org.jboss.brms.runtime.resource.push.spi;

import org.jboss.brms.runtime.resource.push.IPushResourceFactory;

public interface PushResourceFactoryBinder {

	public IPushResourceFactory getPushResourceFactory();

	public String getPushResourceFactoryClassStr();

}
