package org.jboss.brms.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.brms.api.RuleGeneratorService;
import org.jboss.brms.embedded.EmbeddedBrmsRuleGenerator;

public class RuleGeneratorServiceBuilder {

	private TemplateRepoType templateRepoType = TemplateRepoType.CLASSPATH;
	private String groupId = "org.jboss";
	private String artifactId = "test-kjar";
	private String templatePath = "templates";
	private String ruleflowPath = "ruleflow";
	private String rulePath = "rules";
	private Map<String, Object> inputMap = new HashMap<String, Object>();
	private List<String> dependencies = new ArrayList<String>();
	private List<Class<?>> classes = new ArrayList<Class<?>>();

	public RuleGeneratorService build() {
		if(templateRepoType.equals(TemplateRepoType.CLASSPATH)) {
			return new EmbeddedBrmsRuleGenerator(groupId, artifactId, templatePath, ruleflowPath, rulePath, inputMap, dependencies, classes);
		}
		return null;
	}

	public RuleGeneratorServiceBuilder templateRepoType(TemplateRepoType templateRepoType) {
		assertNotNull(templateRepoType, "templateRepoType");
		this.templateRepoType = templateRepoType;
		return this;
	}

	public RuleGeneratorServiceBuilder kjarGroupId(String groupId) {
		assertNotNull(groupId, "groupId");
		this.groupId = groupId;
		return this;
	}

	public RuleGeneratorServiceBuilder kjarArtifactId(String artifactId) {
		assertNotNull(artifactId, "artifactId");
		this.artifactId = artifactId;
		return this;
	}

	public RuleGeneratorServiceBuilder templatePath(String templatePath) {
		assertNotNull(templatePath, "templatePath");
		this.templatePath = templatePath;
		return this;
	}

	public RuleGeneratorServiceBuilder ruleflowPath(String ruleflowPath) {
		assertNotNull(ruleflowPath, "ruleflowPath");
		this.ruleflowPath = ruleflowPath;
		return this;
	}

	public RuleGeneratorServiceBuilder rulePath(String rulePath) {
		assertNotNull(rulePath, "rulePath");
		this.rulePath = rulePath;
		return this;
	}

	public RuleGeneratorServiceBuilder inputMap(Map<String, Object> inputMap) {
		assertNotNull(inputMap, "inputMap");
		this.inputMap = inputMap;
		return this;
	}

	public RuleGeneratorServiceBuilder dependencies(List<String> dependencies) {
		assertNotNull(dependencies, "dependencies");
		this.dependencies = dependencies;
		return this;
	}

	public RuleGeneratorServiceBuilder classes(List<Class<?>> classes) {
		assertNotNull(classes, "classes");
		this.classes = classes;
		return this;
	}

	private static void assertNotNull(Object obj, String name) {
		if (obj == null) {
			throw new IllegalArgumentException("Null " + name + " arguments are not accepted!");
		}
	}

}
