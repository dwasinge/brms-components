package org.jboss.brms.runtime;

import org.jboss.brms.runtime.ReflectiveExecutionResultsTransformer;
import org.junit.Assert;
import org.junit.Test;

public class ReflectiveExecutionResultsTransformerTest {

	@Test
	public void shouldReturnCorrectFieldNameForTheRuleListener() {
		// given
		Class<?> responseClazz = TestResponse.class;	

		// when
		String fieldName = ReflectiveExecutionResultsTransformer.getRuleListenerFieldNameOnResponseClass( responseClazz );

		// then
		Assert.assertEquals( "listener", fieldName );
	}
	
	@Test
	public void shouldReturnNullWhenResponseClassHasNoRuleListener() {
		// given
		Class<?> responseClazz = String.class;	

		// when
		String fieldName = ReflectiveExecutionResultsTransformer.getRuleListenerFieldNameOnResponseClass( responseClazz );

		// then
		Assert.assertNull( fieldName );
	}

}
