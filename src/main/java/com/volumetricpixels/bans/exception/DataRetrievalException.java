package com.volumetricpixels.bans.exception;

/**
 * Thrown when there is an error retrieving data from the servers
 */
public class DataRetrievalException extends Exception {
    private static final long serialVersionUID = -8492190312727224578L;

    public DataRetrievalException() {
        super();
    }

    public DataRetrievalException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public DataRetrievalException(final String arg0) {
        super(arg0);
    }

    public DataRetrievalException(final Throwable arg0) {
        super(arg0);
    }
}
