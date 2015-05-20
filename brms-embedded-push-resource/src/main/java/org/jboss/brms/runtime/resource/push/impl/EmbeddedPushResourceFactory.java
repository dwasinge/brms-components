package org.jboss.brms.runtime.resource.push.impl;

import org.jboss.brms.runtime.resource.push.IPushResourceFactory;
import org.jboss.brms.runtime.resource.push.PushResource;

public class EmbeddedPushResourceFactory implements IPushResourceFactory {

	private EmbeddedPushResource pushResource;

	public EmbeddedPushResourceFactory() {
		this.pushResource = new EmbeddedPushResource();
	}

	public EmbeddedPushResourceFactory(EmbeddedPushResource pushResource) {
		this.pushResource = pushResource;
	}

	@Override
	public PushResource getPushResource() {
		return pushResource;
	}

}
