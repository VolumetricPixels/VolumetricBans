package com.volumetricpixels.bans.exception;

/**
 * Thrown when there is an error retrieving data from the servers
 */
public class DataRetrievalException extends Exception {
	private static final long serialVersionUID = -8492190312727224578L;

	public DataRetrievalException() {
		super();
	}

	public DataRetrievalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DataRetrievalException(String arg0) {
		super(arg0);
	}

	public DataRetrievalException(Throwable arg0) {
		super(arg0);
	}
}
