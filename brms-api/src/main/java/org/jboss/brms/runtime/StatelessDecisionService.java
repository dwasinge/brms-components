package org.jboss.brms.runtime;

import java.util.Collection;
import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;

public interface StatelessDecisionService {

	public void createNewKieContainer( List<String> drlAsStringList, List<String> bpmnStringList ) throws BrmsException;

	public < Response > Response execute( Collection<Object> facts, String processId, Class< Response > responseClazz, String logName );

	public < Response > Response execute( Collection<Object> facts, String processId, Class< Response > responseClazz );
	
	public < Response > Response execute( Collection<Object> facts, String processId, String logName );
	
	public < Response > Response execute( Collection<Object> facts, Class< Response > responseClazz );

}