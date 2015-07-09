package org.jboss.brms.common;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.drools.core.command.runtime.rule.QueryCommand;
import org.jboss.brms.common.QueryUtils;
import org.jboss.brms.response.TestResponse;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.command.Command;

public class QueryUtilsTest {

	@Test
	public void testBuildQueryCommands() {

		List< Command< ? >> commandList = QueryUtils.buildQueryCommands(TestResponse.class);
		Assert.assertNotNull(commandList);
		Assert.assertEquals(1, commandList.size());
		QueryCommand command = (QueryCommand)commandList.get(0);
		Assert.assertEquals("Get All Strings", command.getName());

	}

	@Test
	public void testGetAllFields() {

		Collection<Field> fieldList = QueryUtils.getAllFields(TestResponse.class);
		Assert.assertEquals(1, fieldList.size());
		Field field = fieldList.iterator().next();
		Assert.assertEquals("strings", field.getName());

	}

}
