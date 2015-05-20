package org.jboss.brms.runtime.resource.pull.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jboss.brms.commons.exception.BrmsException;
import org.jboss.brms.runtime.resource.pull.PullResource;

public class EmbeddedPullResource implements PullResource {

	@Override
	public List<String> pullResources(List<String> resourceNameList) throws BrmsException {

		List<String> resourceStringList = new ArrayList<String>();

		for(String resourceName : resourceNameList) {
			String resourceContent = retrieveTemplateFromClasspath(resourceName);
			if(null != resourceContent) {
				resourceStringList.add(resourceContent);
			}
		}

		return resourceStringList;

	}

	/**
	 * This method is used to retrieve the template contents as a string from 
	 * the given name in the classpath.
	 * @param templateName
	 * @return
	 * @throws BrmsException 
	 * @throws IOException
	 */
	public String retrieveTemplateFromClasspath(String templateName) throws BrmsException {

		// Get the InputStream for the template name
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(templateName);

		// Exit if template not found
		if(null == is) {
			return null;
		}

		// Read the file and return the contents
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String line;

		try {
			while ((line = reader.readLine()) != null) {
				out.append(line);
				out.append(newLine);
			}
		} catch (IOException e) {
			throw new BrmsException(e);
		}

		return out.toString();

	}

}
