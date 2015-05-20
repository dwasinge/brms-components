package org.jboss.brms.runtime.deploy;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jboss.brms.runtime.deploy.impl.StaticDeployArtifactBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DeployArtifactFactory {

	private static Logger logger = LoggerFactory.getLogger(DeployArtifactFactory.class);

	private DeployArtifactFactory() {}

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

		String requested = StaticDeployArtifactBinder.REQUESTED_API_VERSION;

		try {
			boolean match = false;
			for(String versionPrefix : API_COMPATIBILITY_LIST) {
				if(requested.startsWith(versionPrefix)) {
					match = true;
				}
			}
	
			if(!match) {
				logger.warn("The requested version " + requested + " by your deploy artifact binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
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
			Set<URL> staticDeployArtifactBinderPathSet = findPossibleStaticDeployArtifactPathSet();
			reportMultipleBindings(staticDeployArtifactBinderPathSet);
			StaticDeployArtifactBinder.getSingleton();
			INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
			reportActualBinding(staticDeployArtifactBinderPathSet);
		} catch (NoClassDefFoundError ncde) {
			String msg = ncde.getMessage();
			if(messageContainsComAexpBrmsResourceDeployImplStaticResourceBinder(msg)) {
				INITIALIZATION_STATE = FALLBACK_INITIALIZATION;
				logger.error("Failed to load class \"com.aexp.brms.runtime.deploy.impl.StaticDeployArtifactBinder\".");
				logger.error("Defaulting to no-operation deploy artifict implementation");
			} else {
				failedBinding(ncde);
				throw ncde;
			}
		} catch (java.lang.NoSuchMethodError nsme) {
			String msg = nsme.getMessage();
			if(msg != null && msg.indexOf("com.aexp.brms.runtime.deploy.StaticDeployArtifactBinder.getSingleton()") != -1) {
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
		logger.error("Failed to instantiate DeployArtifactFactory");
		t.printStackTrace();
	}

	private static boolean messageContainsComAexpBrmsResourceDeployImplStaticResourceBinder(String msg) {

		if(null == msg) {
			return false;
		} else if(msg.indexOf("com/aexp/brms/runtime/deploy/impl/StaticDeployArtifactBinder") != -1 ) {
			return true;
		} else if(msg.indexOf("com.aexp.brms.runtime.deploy.impl.StaticDeployArtifactBinder") != -1) {
			return true;
		}

		return false;

	}

	private static void reportActualBinding(Set<URL> staticDeployArtifactBinderPathSet) {
		if(!isAmbiguousStaticDeployArtifactBinderPathSet(staticDeployArtifactBinderPathSet)) {
			logger.info("Actual binding is of type [" + StaticDeployArtifactBinder.getSingleton().getDeployArtifactFactoryClassStr() + "]");
		}
	}

	private static void reportMultipleBindings(Set<URL> staticDeployArtifactBinderPathSet) {

		if(isAmbiguousStaticDeployArtifactBinderPathSet(staticDeployArtifactBinderPathSet)) {
			logger.warn("Class path contains multiple Deploy Artifact bindings.");
			Iterator<URL> iterator = staticDeployArtifactBinderPathSet.iterator();
			while(iterator.hasNext()) {
				URL path = iterator.next();
				logger.warn("Found binding in [" + path + "]");
			}
			logger.warn("Only one deploy artifact binding can be on the classpath");
		}

	}

	private static boolean isAmbiguousStaticDeployArtifactBinderPathSet(Set<URL> staticDeployArtifactBinderPathSet) {
		return staticDeployArtifactBinderPathSet.size() > 1;
	}

	private static String STATIC_DEPLOY_ARTIFACT_BINDER_PATH = "com/aexp/brms/runtime/deploy/impl/StaticDeployArtifactBinder.class";

	private static Set<URL> findPossibleStaticDeployArtifactPathSet() {

		Set<URL> staticDeployArtifactBinderPathSet = new LinkedHashSet<URL>();

		try {
			ClassLoader resourceDeployArtifactClassLoader = DeployArtifactFactory.class.getClassLoader();
			Enumeration<URL> paths;
			if(null == resourceDeployArtifactClassLoader) {
				paths = ClassLoader.getSystemResources(STATIC_DEPLOY_ARTIFACT_BINDER_PATH);
			} else {
				paths = resourceDeployArtifactClassLoader.getResources(STATIC_DEPLOY_ARTIFACT_BINDER_PATH);
			}
			while(paths.hasMoreElements()) {
				URL path = (URL) paths.nextElement();
				staticDeployArtifactBinderPathSet.add(path);
			}
		} catch(IOException ioe) {
			logger.error("Error getting resources from path", ioe);
		}

		return staticDeployArtifactBinderPathSet;

	}

	public static DeployArtifact getDeployArtifact() {
		IDeployArtifactFactory iDeployArtifact = getIDeployArtifactFactory();
		return iDeployArtifact.getDeployArtifact();
	}

	public static IDeployArtifactFactory getIDeployArtifactFactory() {

		if(INITIALIZATION_STATE == UNINITIALIZED) {
			INITIALIZATION_STATE = ONGOING_INITIALIZATION;
			performInitialization();
		}

		switch(INITIALIZATION_STATE) {
		case SUCCESSFUL_INITIALIZATION:
			return StaticDeployArtifactBinder.getSingleton().getDeployArtifactFactory();
		case FALLBACK_INITIALIZATION:
			throw new IllegalStateException("Failed to get NOP fallback factory");
		case FAILED_INITIALIZATION:
			throw new IllegalStateException("Failed to initialize deploy artifact factory");
		case ONGOING_INITIALIZATION:
			throw new IllegalStateException("Failed to get temporary deploy artifact factory");
		}
		throw new IllegalStateException("Unreachable code");

	}

}
