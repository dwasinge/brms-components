package org.jboss.brms.runtime.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.DefaultStatelessDecisionService;
import org.jboss.brms.runtime.QueryUtils;
import org.jboss.brms.runtime.ReflectiveExecutionResultsTransformer;
import org.jboss.brms.runtime.RuleListener;
import org.jboss.brms.runtime.StatelessDecisionService;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedStatelessDecisionService implements StatelessDecisionService {

	private static final Logger LOG = LoggerFactory.getLogger( DefaultStatelessDecisionService.class );

	private KieCommands commandFactory;
	private KieContainer kieContainer;

	public EmbeddedStatelessDecisionService() {
		kieContainer = KieServices.Factory.get().getKieClasspathContainer();
//		StatelessKieSession statelessKieSession = kieContainer.newStatelessKieSession();
//		LOG.debug( statelessKieSession.getKieBase().toString() );

		/**
		 * Break point here to find what rules are in the KIE Base
		 */
		commandFactory = KieServices.Factory.get().getCommands();
	}

	@Override
	public void createNewKieContainer( List<String> drlAsStringList, List<String> bpmnStringList ) throws BrmsException {

		KieServices ks = KieServices.Factory.get();
		KieFileSystem kfs = ks.newKieFileSystem();
		int index = 0;

		for(String drlString : drlAsStringList) {
			if(null != drlString) {
				Resource resource = ks.getResources().newByteArrayResource(drlString.getBytes()).setSourcePath("drl_"+index).setResourceType(ResourceType.DRL);
				kfs.write(resource);
				index++;
			}
		}

		index = 0;

		for(String bpmnString : bpmnStringList) {
			if(null != bpmnString) {
				Resource resource = ks.getResources().newByteArrayResource(bpmnString.getBytes()).setSourcePath("bpmn_"+index).setResourceType(ResourceType.BPMN2);
				kfs.write(resource);
				index++;
			}
		}

		KieBuilder kieBuilder = ks.newKieBuilder(kfs);
		Results results = kieBuilder.buildAll().getResults();

		List<Message> errors = results.getMessages(Message.Level.ERROR);
		if(!errors.isEmpty()) {
			for(Message error : errors) {
				throw new BrmsException("KieBuilder failed to compile generated DRL: " + error.getText());
			}
		}

		this.kieContainer =  ks.newKieContainer(kieBuilder.getKieModule().getReleaseId());

	}

	@Override
	public <Response> Response execute( Collection<Object> facts, String processId, Class<Response> responseClazz, String logName ) {
		BatchExecutionCommand batchExecutionCommand = createBatchExecutionCommand( facts, processId, responseClazz );

		StatelessKieSession session = kieContainer.newStatelessKieSession();
		RuleListener ruleListener = new RuleListener();

		if ( logName != null ) {
			KieServices.Factory.get().getLoggers().newFileLogger( session, logName );
		}
		// this is purely for debugging
		else if ( LOG.isDebugEnabled() ) {
//			session.addEventListener( new DebugRuleRuntimeEventListener() );
//			session.addEventListener( new DebugAgendaEventListener() );
		}
		// this is used capture the enrichments run in the service
		String fieldName = ReflectiveExecutionResultsTransformer.getRuleListenerFieldNameOnResponseClass( responseClazz );
		if ( fieldName != null ) {
			LOG.debug( "response class has a rule listener field. adding a rule listener to the rule session." );
			session.addEventListener( ruleListener );
		} else {
			LOG.debug( "response class does not have a rule listener field. no listener created." );
		}

		ExecutionResults results = session.execute( batchExecutionCommand );

		Response response = ReflectiveExecutionResultsTransformer.transform( results, responseClazz, ruleListener, fieldName );

		return response;
	}

	public BatchExecutionCommand createBatchExecutionCommand( Collection<Object> facts, String processId, Class<?> responseClazz ) {
		List<Command<?>> commands = new ArrayList<Command<?>>();

		if ( facts != null ) {
			commands.add( commandFactory.newInsertElements( facts ) );
		}
		if ( processId != null && !processId.isEmpty() ) {
			commands.add( commandFactory.newStartProcess( processId ) );
		}

		commands.add( commandFactory.newFireAllRules() );

		// creates commands to run the queries at the end of process
		commands.addAll( QueryUtils.buildQueryCommands( responseClazz ) );

		return commandFactory.newBatchExecution( commands );
	}

	public KieContainer getKieContainer() {
		return kieContainer;
	}

	@Override
	public <Response> Response execute( Collection<Object> facts, String processId, Class<Response> responseClazz ) {
		return execute( facts, processId, responseClazz, null );
	}

	@Override
	public <Response> Response execute( Collection<Object> facts, String processId, String logName ) {
		return execute( facts, processId, null, logName );
	}

	@Override
	public <Response> Response execute( Collection<Object> facts, Class<Response> responseClazz ) {
		return execute( facts, null, responseClazz, null );
	}


}
