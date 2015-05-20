package org.jboss.brms.runtime.template.impl;

import java.util.HashMap;
import java.util.Map;

import org.jboss.brms.runtime.template.Template;
import org.jboss.brms.runtime.template.impl.EmbeddedTemplateFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedTemplateFactoryTest {

	private EmbeddedTemplateFactory embeddedTemplateFactory;

	@Before
	public void setup() {
		embeddedTemplateFactory = new EmbeddedTemplateFactory();
	}

	@After
	public void tearDown() {
		embeddedTemplateFactory = null;
	}

	@Test
	public void testEmbeddedTemplateFactory() {

		Template template = embeddedTemplateFactory.getTemplate();

		String templateStr = "$m : Map( this['year'] == \"@{year}\")";
		String expected = "$m : Map( this['year'] == \"2015\")";
		String actual = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("year", "2015");

		actual = template.generateResourceFromTemplate(templateStr, inputMap);

		Assert.assertEquals(expected, actual);

	}

}
