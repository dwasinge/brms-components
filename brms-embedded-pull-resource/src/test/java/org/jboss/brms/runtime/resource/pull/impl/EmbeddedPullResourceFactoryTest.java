package org.jboss.brms.runtime.resource.pull.impl;

import java.util.ArrayList;
import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.resource.pull.PullResource;
import org.jboss.brms.runtime.resource.pull.impl.EmbeddedPullResourceFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedPullResourceFactoryTest {

	private EmbeddedPullResourceFactory embeddedPullResourceFactory;

	private static final String CLASSPATH_RESOURCES = "testA/testA.template,testB/testB.template,testC.template";

	@Before
	public void setup() {
		embeddedPullResourceFactory = new EmbeddedPullResourceFactory();
	}

	@After
	public void tearDown() {
		embeddedPullResourceFactory = null;
	}

	@Test
	public void testEmbeddedPullResourceFactory() throws BrmsException {

		/*
		 * Get Resource Names to Retrieve
		 */
		boolean aFound = false;
		boolean bFound = false;
		boolean cFound = false;

		List<String> resourceList = new ArrayList<String>();

		for(String name : CLASSPATH_RESOURCES.split(",")) {
			resourceList.add(name);
		}

		Assert.assertEquals(3, resourceList.size());

		/*
		 * Get Pull Resource from Factory
		 */

		PullResource pullResource = embeddedPullResourceFactory.getPullResource();
		Assert.assertNotNull(pullResource);

		/*
		 * Pull Resources from Classpath
		 */

		List<String> resourceContentList = pullResource.pullResources(resourceList);
		Assert.assertEquals(3, resourceContentList.size());

		/*
		 * Validate Results
		 */

		for(String content : resourceContentList) {
			if(content.contains("Test Rule A")) {
				aFound = true;
			} else if(content.contains("Test Rule B")) {
				bFound = true;
			} else if(content.contains("Test Rule C")) {
				cFound = true;
			}
		}

		Assert.assertTrue(aFound);
		Assert.assertTrue(bFound);
		Assert.assertTrue(cFound);

	}

}
