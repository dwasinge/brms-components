package org.jboss.brms.api;

import org.jboss.brms.exception.BrmsException;

public interface RuleGeneratorService {

	String generateKjarFromTemplates() throws BrmsException;

}
