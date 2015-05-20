package org.jboss.brms.runtime.template.spi;

import org.jboss.brms.runtime.template.ITemplateFactory;

public interface TemplateFactoryBinder {

	public ITemplateFactory getTemplateFactory();

	public String getTemplateFactoryClassStr();

}
