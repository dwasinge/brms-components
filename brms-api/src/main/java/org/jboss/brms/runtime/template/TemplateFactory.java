package org.jboss.brms.runtime.template;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jboss.brms.runtime.template.impl.StaticTemplateBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateFactory {

	private static Logger logger = LoggerFactory.getLogger(TemplateFactory.class);

	private TemplateFactory() {}

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

		String requested = StaticTemplateBinder.REQUESTED_API_VERSION;

		try {
			boolean match = false;
			for(String versionPrefix : API_COMPATIBILITY_LIST) {
				if(requested.startsWith(versionPrefix)) {
					match = true;
				}
			}
	
			if(!match) {
				logger.warn("The requested version " + requested + " by your template binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
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
			Set<URL> staticTemplateBinderPathSet = findPossibleStaticTemplatePathSet();
			reportMultipleBindings(staticTemplateBinderPathSet);
			StaticTemplateBinder.getSingleton();
			INITIALIZATION_STATE = SUCCESSFUL_INITIALIZATION;
			reportActualBinding(staticTemplateBinderPathSet);
		} catch (NoClassDefFoundError ncde) {
			String msg = ncde.getMessage();
			if(messageContainsComAexpBrmsTemplateImplStaticResourceBinder(msg)) {
				INITIALIZATION_STATE = FALLBACK_INITIALIZATION;
				logger.error("Failed to load class \"com.aexp.brms.template.impl.StaticTemplateBinder\".");
				logger.error("Defaulting to no-operation template implementation");
			} else {
				failedBinding(ncde);
				throw ncde;
			}
		} catch (java.lang.NoSuchMethodError nsme) {
			String msg = nsme.getMessage();
			if(msg != null && msg.indexOf("com.aexp.brms.template.StaticTemplateBinder.getSingleton()") != -1) {
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
		logger.error("Failed to instantiate TemplateFactory");
		t.printStackTrace();
	}

	private static boolean messageContainsComAexpBrmsTemplateImplStaticResourceBinder(String msg) {

		if(null == msg) {
			return false;
		} else if(msg.indexOf("com/aexp/brms/template/impl/StaticTemplateBinder") != -1 ) {
			return true;
		} else if(msg.indexOf("com.aexp.brms.template.impl.StaticTemplateBinder") != -1) {
			return true;
		}

		return false;

	}

	private static void reportActualBinding(Set<URL> staticTemplateBinderPathSet) {
		if(!isAmbiguousStaticTemplateBinderPathSet(staticTemplateBinderPathSet)) {
			logger.info("Actual binding is of type [" + StaticTemplateBinder.getSingleton().getTemplateFactoryClassStr() + "]");
		}
	}

	private static void reportMultipleBindings(Set<URL> staticTemplateBinderPathSet) {

		if(isAmbiguousStaticTemplateBinderPathSet(staticTemplateBinderPathSet)) {
			logger.warn("Class path contains multiple Template bindings.");
			Iterator<URL> iterator = staticTemplateBinderPathSet.iterator();
			while(iterator.hasNext()) {
				URL path = iterator.next();
				logger.warn("Found binding in [" + path + "]");
			}
			logger.warn("Only one template binding can be on the classpath");
		}

	}

	private static boolean isAmbiguousStaticTemplateBinderPathSet(Set<URL> staticTemplateBinderPathSet) {
		return staticTemplateBinderPathSet.size() > 1;
	}

	private static String STATIC_TEMPLATE_BINDER_PATH = "com/aexp/brms/template/impl/StaticTemplateBinder.class";

	private static Set<URL> findPossibleStaticTemplatePathSet() {

		Set<URL> staticTemplateBinderPathSet = new LinkedHashSet<URL>();

		try {
			ClassLoader resourceTemplateClassLoader = TemplateFactory.class.getClassLoader();
			Enumeration<URL> paths;
			if(null == resourceTemplateClassLoader) {
				paths = ClassLoader.getSystemResources(STATIC_TEMPLATE_BINDER_PATH);
			} else {
				paths = resourceTemplateClassLoader.getResources(STATIC_TEMPLATE_BINDER_PATH);
			}
			while(paths.hasMoreElements()) {
				URL path = (URL) paths.nextElement();
				staticTemplateBinderPathSet.add(path);
			}
		} catch(IOException ioe) {
			logger.error("Error getting resources from path", ioe);
		}

		return staticTemplateBinderPathSet;

	}

	public static Template getTemplate() {
		ITemplateFactory iTemplateFactory = getITemplateFactory();
		return iTemplateFactory.getTemplate();
	}

	public static ITemplateFactory getITemplateFactory() {

		if(INITIALIZATION_STATE == UNINITIALIZED) {
			INITIALIZATION_STATE = ONGOING_INITIALIZATION;
			performInitialization();
		}

		switch(INITIALIZATION_STATE) {
		case SUCCESSFUL_INITIALIZATION:
			return StaticTemplateBinder.getSingleton().getTemplateFactory();
		case FALLBACK_INITIALIZATION:
			throw new IllegalStateException("Failed to get NOP fallback factory");
		case FAILED_INITIALIZATION:
			throw new IllegalStateException("Failed to initialize template factory");
		case ONGOING_INITIALIZATION:
			throw new IllegalStateException("Failed to get temporary template factory");
		}
		throw new IllegalStateException("Unreachable code");

	}

}
