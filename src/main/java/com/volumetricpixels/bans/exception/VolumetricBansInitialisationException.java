package com.volumetricpixels.bans.exception;

/**
 * Thrown when there is an error in initialisation of the plugin
 */
public class VolumetricBansInitialisationException extends Exception {
	private static final long serialVersionUID = 2436847205272294250L;

	public VolumetricBansInitialisationException() {
		super();
	}

	public VolumetricBansInitialisationException(final String arg0,
			final Throwable arg1) {
		super(arg0, arg1);
	}

	public VolumetricBansInitialisationException(final String arg0) {
		super(arg0);
	}

	public VolumetricBansInitialisationException(final Throwable arg0) {
		super(arg0);
	}
}
