package org.jboss.brms.embedded;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.brms.api.RuleGeneratorService;
import org.jboss.brms.common.FileResource;
import org.jboss.brms.common.util.IoUtils;
import org.jboss.brms.common.util.KieResourceUtils;
import org.jboss.brms.exception.BrmsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedBrmsRuleGenerator implements RuleGeneratorService {

	private static Logger LOGGER = LoggerFactory.getLogger(EmbeddedBrmsRuleGenerator.class);
	private String kjarGroupId;
	private String kjarArtifactId;
	private String templatePath;
	private String ruleflowPath;
	private String rulePath;
	private Map<String, Object> inputMap;
	private List<String> dependencies;
	private List<Class<?>> classes;

	public EmbeddedBrmsRuleGenerator(String kjarGroupId, String kjarArtifactId, String templatePath, String ruleflowPath, String rulePath, Map<String, Object> inputMap, List<String> dependencies, List<Class<?>> classes) {
		this.kjarArtifactId = kjarArtifactId;
		this.kjarGroupId = kjarGroupId;
		this.templatePath = templatePath;
		this.ruleflowPath = ruleflowPath;
		this.rulePath = rulePath;
		this.inputMap = inputMap;
		this.dependencies = dependencies;
		this.classes = classes;
	}

	@Override
	public String generateKjarFromTemplates() throws BrmsException {

		// Validate Required Values
		assertNotNull(kjarGroupId, "kjarGroupId");
		assertNotNull(kjarArtifactId, "kjarArtifactId");
		assertNotNull(templatePath, "templatePath");
		assertNotNull(ruleflowPath, "ruleflowPath");
		assertNotNull(rulePath, "rulePath");
		assertNotNull(inputMap, "inputMap");
		assertNotNull(dependencies, "dependencies");
		assertNotNull(classes, "classes");

		// Pull Templates and Ruleflows from path
		List<FileResource> resourceList = retrieveResourcesFromClassPath(templatePath);
		resourceList.addAll(retrieveResourcesFromClassPath(ruleflowPath));
		resourceList.addAll(retrieveResourcesFromClassPath(rulePath));

		// Create KJAR and deploy to Maven Repository
		return KieResourceUtils.generateResourcesForKjarAndDeployToMaven(resourceList, inputMap, kjarGroupId, kjarArtifactId, dependencies, classes);

	}

	/*
	 * 
	 * Get Templates From the Classpath
	 * 
	 */
	private List<FileResource> retrieveResourcesFromClassPath(String path) throws BrmsException {

		List<FileResource> fileResourceList = new ArrayList<FileResource>();

		try {
			fileResourceList.addAll(IoUtils.loadResource(path, true));
		} catch(Exception e) {
			// Cannot throw exception because this resource type may not be supplied
			LOGGER.info("Failed to retrieve resources from path '" + path + "'");
		}

		return fileResourceList;

	}


	private static void assertNotNull(Object obj, String name) {
		if (obj == null) {
			throw new IllegalArgumentException("Null " + name + " arguments are not accepted!");
		}
	}

}
