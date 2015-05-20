package org.jboss.brms.runtime;

import java.util.Collection;
import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.StatelessDecisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StubbedStatelessDecisionService implements StatelessDecisionService {

	private static final Logger LOG = LoggerFactory.getLogger( StubbedStatelessDecisionService.class );

	@Override
	public <Response> Response execute( Collection<Object> facts, String processId, Class<Response> responseClazz, String logName ) {
		LOG.info( "execute called on a StubbedStatelessDecisionService" );
		return null;
	}

	@Override
	public <Response> Response execute( Collection<Object> facts, String processId, Class<Response> responseClazz ) {
		LOG.info( "execute called on a StubbedStatelessDecisionService" );
		return null;
	}

	@Override
	public <Response> Response execute( Collection<Object> facts, String processId, String logName ) {
		LOG.info( "execute called on a StubbedStatelessDecisionService" );
		return null;
	}

	@Override
	public <Response> Response execute( Collection<Object> facts, Class<Response> responseClazz ) {
		LOG.info( "execute called on a StubbedStatelessDecisionService" );
		return null;
	}

	@Override
	public void createNewKieContainer(List<String> drlAsStringList, List<String> bpmnStringList) throws BrmsException {
		LOG.info( "execute called on a StubbedStatelessDecisionService" );
		
	}

}
