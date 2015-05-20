package org.jboss.brms.runtime.resource.pull.impl;

import org.jboss.brms.runtime.resource.pull.IPullResourceFactory;
import org.jboss.brms.runtime.resource.pull.PullResource;

public class EmbeddedPullResourceFactory implements IPullResourceFactory {

	private EmbeddedPullResource pullResource;

	public EmbeddedPullResourceFactory() {
		pullResource = new EmbeddedPullResource();
	}

	@Override
	public PullResource getPullResource() {
		return pullResource;
	}

}
