package org.jboss.brms.runtime;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jboss.brms.runtime.impl.StaticStatelessDecisionServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatelessDecisionServiceFactory {

	private static Logger logger = LoggerFactory.getLogger(StatelessDecisionServiceFactory.class);

	private StatelessDecisionServiceFactory() {}

	static final int UNINITIALIZED = 0;
	static final int ONGOING_INITIALIZATION = 1;
	static final int FAILED_INITIALIZATION = 2;
	static final int SUCCESSFUL_INITIALIZATION = 3;
	static final int FALLBACK_INITIALIZATION = 4;

	static int INITIALIZATION_STATE = UNINITIALIZED;

	static private final String[] API_COMPATIBILITY_LIST = new String[] { "1.1" };

	private final static void performInitialization() {

		bind();
		if(INITIALIZATION_STATE == SUCCESSFUL_INITIALIZATION) {
			versionSanityCheck();
		}

	}

	private final static void versionSanityCheck() {

		String requested = StaticStatelessDecisionServiceBinder.REQUESTED_API_VERSION;

		try {
			boolean match = false;
			for(String versionPrefix : API_COMPATIBILITY_LIST) {
				if(requested.startsWith(versionPrefix)) {
					match = true;
				}
			}
	
			if(!match) {
				logger.warn("The requested version " + requested + " by your stateless decision service binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
				logger.warn("Verify implementation version matches that of the api.");
			}
		} catch (java.lang.NoSuchFieldError nsfe) {
			// For now just pass through since we gave a warning.
			// May need to do something in the future if there are problems.
		} catch (Throwable t) {
			logger.error("Unexpected problem occurred during the version check");
			t.printStackTrace();
		}

	}

	private final static void bind() {

		try {
			Set<URL> staticStatelessDecisionServiceBinderPathSet = findPossibleStaticStatelessDecisionServicePathSet();
			reportMultipleBindings(staticStatelessDecisionServiceBinderPathSet);
			StaticStatelessDecisionServiceBinder.getSingleton();
			INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
			reportActualBinding(staticStatelessDecisionServiceBinderPathSet);
		} catch (NoClassDefFoundError ncde) {
			String msg = ncde.getMessage();
			if(messageContainsOrgJbossBrmsRuntimeImplStaticResourceBinder(msg)) {
				INITIALIZATION_STATE = FALLBACK_INITIALIZATION;
				logger.error("Failed to load class \"org.jboss.brms.runtime.impl.StaticStatelessDecisionServiceBinder\".");
				logger.error("Defaulting to no-operation stateless decision service implementation");
			} else {
				failedBinding(ncde);
				throw ncde;
			}
		} catch (java.lang.NoSuchMethodError nsme) {
			String msg = nsme.getMessage();
			if(msg != null && msg.indexOf("org.jboss.brms.runtime.impl.StaticStatelessDecisionServiceBinder.getSingleton()") != -1) {
				logger.error("brms-api 1.1.x (or later) is incompatible with this binding.");
				logger.error("Your binding is version 1.0.x or earlier.");
				logger.error("Upgrade your binding to version 1.1.x.");
			}
		} catch (Exception e) {
			failedBinding(e);
			throw new IllegalStateException("Unexpected initialization failure", e);
		}

	}

	private static void failedBinding(Throwable t) {
		INITIALIZATION_STATE = FAILED_INITIALIZATION;
		logger.error("Failed to instantiate StatelessDecisionServiceFactory");
		t.printStackTrace();
	}

	private static boolean messageContainsOrgJbossBrmsRuntimeImplStaticResourceBinder(String msg) {

		if(null == msg) {
			return false;
		} else if(msg.indexOf("org/jboss/brms/runtime/impl/StaticStatelessDecisionServiceBinder") != -1 ) {
			return true;
		} else if(msg.indexOf("org.jboss.brms.runtime.impl.StaticStatelessDecisionServiceBinder") != -1) {
			return true;
		}

		return false;

	}

	private static void reportActualBinding(Set<URL> staticStatelessDecisionServiceBinderPathSet) {
		if(!isAmbiguousStaticStatelessDecisionServiceBinderPathSet(staticStatelessDecisionServiceBinderPathSet)) {
			logger.info("Actual binding is of type [" + StaticStatelessDecisionServiceBinder.getSingleton().getStatelessDecisionServiceFactoryClassStr() + "]");
		}
	}

	private static void reportMultipleBindings(Set<URL> staticStatelessDecisionServiceBinderPathSet) {

		if(isAmbiguousStaticStatelessDecisionServiceBinderPathSet(staticStatelessDecisionServiceBinderPathSet)) {
			logger.warn("Class path contains multiple StatelessDecisionService bindings.");
			Iterator<URL> iterator = staticStatelessDecisionServiceBinderPathSet.iterator();
			while(iterator.hasNext()) {
				URL path = iterator.next();
				logger.warn("Found binding in [" + path + "]");
			}
			logger.warn("Only one StatelessDecisionService binding can be on the classpath");
		}

	}

	private static boolean isAmbiguousStaticStatelessDecisionServiceBinderPathSet(Set<URL> staticStatelessDecisionServiceBinderPathSet) {
		return staticStatelessDecisionServiceBinderPathSet.size() > 1;
	}

	private static String STATIC_STATELESS_DECISION_SERVICE_BINDER_PATH = "org/jboss/brms/runtime/impl/StaticStatelessDecisionServiceBinder.class";

	private static Set<URL> findPossibleStaticStatelessDecisionServicePathSet() {

		Set<URL> staticStatelessDecisionServiceBinderPathSet = new LinkedHashSet<URL>();

		try {
			ClassLoader resourceStatelessDecisionServiceClassLoader = StatelessDecisionServiceFactory.class.getClassLoader();
			Enumeration<URL> paths;
			if(null == resourceStatelessDecisionServiceClassLoader) {
				paths = ClassLoader.getSystemResources(STATIC_STATELESS_DECISION_SERVICE_BINDER_PATH);
			} else {
				paths = resourceStatelessDecisionServiceClassLoader.getResources(STATIC_STATELESS_DECISION_SERVICE_BINDER_PATH);
			}
			while(paths.hasMoreElements()) {
				URL path = (URL) paths.nextElement();
				staticStatelessDecisionServiceBinderPathSet.add(path);
			}
		} catch(IOException ioe) {
			logger.error("Error getting resources from path", ioe);
		}

		return staticStatelessDecisionServiceBinderPathSet;

	}

	public static StatelessDecisionService getStatelessDecisionService() {
		IStatelessDecisionServiceFactory iStatelessDecisionServiceFactory = getIStatelessDecisionServiceFactory();
		return iStatelessDecisionServiceFactory.getStatelessDecisionService();
	}

	public static IStatelessDecisionServiceFactory getIStatelessDecisionServiceFactory() {

		if(INITIALIZATION_STATE == UNINITIALIZED) {
			INITIALIZATION_STATE = ONGOING_INITIALIZATION;
			performInitialization();
		}

		switch(INITIALIZATION_STATE) {
		case SUCCESSFUL_INITIALIZATION:
			return StaticStatelessDecisionServiceBinder.getSingleton().getStatelessDecisionServiceFactory();
		case FALLBACK_INITIALIZATION:
			throw new IllegalStateException("Failed to get NOP fallback factory");
		case FAILED_INITIALIZATION:
			throw new IllegalStateException("Failed to initialize stateless decision service factory");
		case ONGOING_INITIALIZATION:
			throw new IllegalStateException("Failed to get temporary stateless decision service factory");
		}
		throw new IllegalStateException("Unreachable code");

	}

}
