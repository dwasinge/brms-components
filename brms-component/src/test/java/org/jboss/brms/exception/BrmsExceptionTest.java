package org.jboss.brms.exception;

import org.jboss.brms.exception.BrmsException;
import org.junit.Test;

public class BrmsExceptionTest {

	@Test(expected=BrmsException.class)
	public void setBrmsExceptionDefault() throws BrmsException {
		throw new BrmsException();
	}

	@Test(expected=BrmsException.class)
	public void setBrmsExceptionWithMessage() throws BrmsException {
		throw new BrmsException("message");
	}

	@Test(expected=BrmsException.class)
	public void setBrmsExceptionWithMessageAndThrowable() throws BrmsException {
		throw new BrmsException("message", new Exception());
	}

	@Test(expected=BrmsException.class)
	public void setBrmsExceptionDefaultWithThrowable() throws BrmsException {
		throw new BrmsException(new Exception());
	}

	@Test(expected=BrmsException.class)
	public void setBrmsExceptionWithAllOptions() throws BrmsException {
		throw new BrmsException("message", new Exception(), false, false);
	}

}
