package org.jboss.brms.runtime.resource.pull;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jboss.brms.runtime.resource.pull.impl.StaticPullResourceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullResourceFactory {

	private static Logger logger = LoggerFactory.getLogger(PullResourceFactory.class);

	private PullResourceFactory() {}

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

		String requested = StaticPullResourceBinder.REQUESTED_API_VERSION;

		try {
			boolean match = false;
			for(String versionPrefix : API_COMPATIBILITY_LIST) {
				if(requested.startsWith(versionPrefix)) {
					match = true;
				}
			}
	
			if(!match) {
				logger.warn("The requested version " + requested + " by your pull binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
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
			Set<URL> staticPullResourceBinderPathSet = findPossibleStaticPullResourcePathSet();
			reportMultipleBindings(staticPullResourceBinderPathSet);
			StaticPullResourceBinder.getSingleton();
			INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
			reportActualBinding(staticPullResourceBinderPathSet);
		} catch (NoClassDefFoundError ncde) {
			String msg = ncde.getMessage();
			if(messageContainsComAexpBrmsResourcePullImplStaticResourceBinder(msg)) {
				INITIALIZATION_STATE = FALLBACK_INITIALIZATION;
				logger.error("Failed to load class \"com.aexp.brms.runtime.resource.pull.impl.StaticPullResourceBinder\".");
				logger.error("Defaulting to no-operation pull resource implementation");
			} else {
				failedBinding(ncde);
				throw ncde;
			}
		} catch (java.lang.NoSuchMethodError nsme) {
			String msg = nsme.getMessage();
			if(msg != null && msg.indexOf("com.aexp.brms.runtime.resource.pull.StaticPullResourceBinder.getSingleton()") != -1) {
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
		logger.error("Failed to instantiate PullResourceFactory");
		t.printStackTrace();
	}

	private static boolean messageContainsComAexpBrmsResourcePullImplStaticResourceBinder(String msg) {

		if(null == msg) {
			return false;
		} else if(msg.indexOf("com/aexp/brms/runtime/resource/pull/impl/StaticPullResourceBinder") != -1 ) {
			return true;
		} else if(msg.indexOf("com.aexp.brms.runtime.resource.pull.impl.StaticPullResourceBinder") != -1) {
			return true;
		}

		return false;

	}

	private static void reportActualBinding(Set<URL> staticPullResourceBinderPathSet) {
		if(!isAmbiguousStaticPullResourceBinderPathSet(staticPullResourceBinderPathSet)) {
			logger.info("Actual binding is of type [" + StaticPullResourceBinder.getSingleton().getPullResourceFactoryClassStr() + "]");
		}
	}

	private static void reportMultipleBindings(Set<URL> staticPullResourceBinderPathSet) {

		if(isAmbiguousStaticPullResourceBinderPathSet(staticPullResourceBinderPathSet)) {
			logger.warn("Class path contains multiple Pull Resource bindings.");
			Iterator<URL> iterator = staticPullResourceBinderPathSet.iterator();
			while(iterator.hasNext()) {
				URL path = iterator.next();
				logger.warn("Found binding in [" + path + "]");
			}
			logger.warn("Only one pull resource binding can be on the classpath");
		}

	}

	private static boolean isAmbiguousStaticPullResourceBinderPathSet(Set<URL> staticPullResourceBinderPathSet) {
		return staticPullResourceBinderPathSet.size() > 1;
	}

	private static String STATIC_PULL_RESOURCE_BINDER_PATH = "com/aexp/brms/runtime/resource/pull/impl/StaticPullResourceBinder.class";

	private static Set<URL> findPossibleStaticPullResourcePathSet() {

		Set<URL> staticPullResourceBinderPathSet = new LinkedHashSet<URL>();

		try {
			ClassLoader resourcePullResourceClassLoader = PullResourceFactory.class.getClassLoader();
			Enumeration<URL> paths;
			if(null == resourcePullResourceClassLoader) {
				paths = ClassLoader.getSystemResources(STATIC_PULL_RESOURCE_BINDER_PATH);
			} else {
				paths = resourcePullResourceClassLoader.getResources(STATIC_PULL_RESOURCE_BINDER_PATH);
			}
			while(paths.hasMoreElements()) {
				URL path = (URL) paths.nextElement();
				staticPullResourceBinderPathSet.add(path);
			}
		} catch(IOException ioe) {
			logger.error("Error getting resources from path", ioe);
		}

		return staticPullResourceBinderPathSet;

	}

	public static PullResource getPullResource() {
		IPullResourceFactory iPullResourceFactory = getIPullResourceFactory();
		return iPullResourceFactory.getPullResource();
	}

	public static IPullResourceFactory getIPullResourceFactory() {

		if(INITIALIZATION_STATE == UNINITIALIZED) {
			INITIALIZATION_STATE = ONGOING_INITIALIZATION;
			performInitialization();
		}

		switch(INITIALIZATION_STATE) {
		case SUCCESSFUL_INITIALIZATION:
			return StaticPullResourceBinder.getSingleton().getPullResourceFactory();
		case FALLBACK_INITIALIZATION:
			throw new IllegalStateException("Failed to get NOP fallback factory");
		case FAILED_INITIALIZATION:
			throw new IllegalStateException("Failed to initialize pull resource factory");
		case ONGOING_INITIALIZATION:
			throw new IllegalStateException("Failed to get temporary pull resource factory");
		}
		throw new IllegalStateException("Unreachable code");

	}

}