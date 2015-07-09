package org.jboss.brms.common;

import org.jboss.brms.common.FileResource;
import org.junit.Assert;
import org.junit.Test;

public class FileResourceTest {

	@Test
	public void testName() {

		FileResource fileResource = new FileResource();
		fileResource.setName("name");
		Assert.assertEquals("name", fileResource.getName());

	}

	@Test
	public void testContent() {

		FileResource fileResource = new FileResource();
		fileResource.setContent("content");
		Assert.assertEquals("content", fileResource.getContent());

	}

}
