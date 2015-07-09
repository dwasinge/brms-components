package org.jboss.brms.common.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.jboss.brms.common.FileResource;
import org.jboss.brms.exception.BrmsException;
import org.kie.api.builder.helper.FluentKieModuleDeploymentHelper;
import org.kie.api.builder.helper.KieModuleDeploymentHelper;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieSessionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KieResourceUtils {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KieResourceUtils.class);

	/**
	 * This method is used to generate resources for a KJAR and deploy the
	 * generated KJAR to the Maven repository defined in the local settings.xml
	 * definition.
	 * 
	 * @param resourceList
	 * @param inputMap
	 * @param kjarGroupId
	 * @param kjarArtifactId
	 * @return
	 * @throws BrmsException
	 */
	public static String generateResourcesForKjarAndDeployToMaven(
			List<FileResource> resourceList, Map<String, Object> inputMap,
			String kjarGroupId, String kjarArtifactId, List<String> dependencies, List<Class<?>> classes) throws BrmsException {

		// Create temporary directory for the generated resources
		Path path = IoUtils.createTempDirectory();

		// Generate Resources and write to temporary directory
		for (FileResource resource : resourceList) {
			if (resource.getName().endsWith(".drl")) {
				String updatedContent = MvelUtils.generateResourceFromTemplate(
						resource.getContent(), inputMap);
				resource.setContent(updatedContent);
			}
			Path resourcePath = path.resolve(resource.getName());
			try {
				Files.write(resourcePath, resource.getContent().getBytes());
			} catch (IOException e) {
				throw new BrmsException(
						"Failed to write KJAR resource to temp directory.", e);
			}
		}

		// Generate Kjar and deploy it to Maven
		String version = createKjarAndDeployToMaven(path, kjarGroupId, kjarArtifactId, dependencies, classes);

		// Delete temporary directory
		try {
//			IoUtils.deleteTemporaryKieArtifacts(kjarArtifactId, version);
			IoUtils.deleteDirectory(path);
		} catch (IOException e) {
			LOGGER.warn("Failed to remove temp KJAR directory '"
					+ path.toString() + "' - " + e.getMessage());
		}

		// Return version created for Kjar
		return version;

	}

	/**
	 * Uses the {@link FluentKieModuleDeploymentHelper} to create a KJAR given
	 * the resources in the resourcePath. The created KJAR is then deployed to
	 * the Maven repository.
	 * 
	 * @param resourcePath
	 * @param kjarGroupId
	 * @param kjarArtifactId
	 * @return
	 */
	private static String createKjarAndDeployToMaven(Path resourcePath,
			String kjarGroupId, String kjarArtifactId, List<String> dependencies, List<Class<?>> classes) {

		/*
		 * NOTE: The KIE CI code currently requires a / at the end of the path
		 * to add all files into the KJAR. It doesn't use the default os file
		 * separator so / is required.
		 */
		String tmpKjarDir = resourcePath.toString();
		if (!tmpKjarDir.endsWith("/") || !tmpKjarDir.endsWith("\\")) {
			tmpKjarDir = tmpKjarDir + "/";
		}
		LOGGER.debug(tmpKjarDir);

		// Create KJAR and deploy to maven
		String time = Long.toString(System.currentTimeMillis());
		FluentKieModuleDeploymentHelper helper = KieModuleDeploymentHelper
				.newFluentInstance();
		createDefaultKieBase(helper);
		helper.addResourceFilePath(tmpKjarDir)
			.setGroupId(kjarGroupId)
			.setArtifactId(kjarArtifactId)
			.setVersion(time);

		// Add Maven dependencies
		for(String dependency : dependencies) {
			helper.addDependencies(dependency);
		}

		// Add Classes
		for(Class<?> classForKjar : classes) {
			helper.addClass(classForKjar);
		}

		helper.createKieJarAndDeployToMaven();

		return time;

	}

	/**
	 * Creates default KIE sessions for the KJAR.
	 * 
	 * @param helper
	 */
	private static void createDefaultKieBase(
			FluentKieModuleDeploymentHelper helper) {
		KieBaseModel kieBaseModel = helper.getKieModuleModel()
				.newKieBaseModel("defaultKieBase").addPackage("*")
				.setDefault(true);
		kieBaseModel.newKieSessionModel("defaultKieSession").setDefault(true);
		kieBaseModel.newKieSessionModel("defaultStatelessKieSession")
				.setType(KieSessionModel.KieSessionType.STATELESS)
				.setDefault(true);
	}

}
