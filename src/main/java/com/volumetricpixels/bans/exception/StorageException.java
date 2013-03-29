package com.volumetricpixels.bans.exception;

/**
 * Thrown when there is an error writing things to files / reading things from them
 */
public class StorageException extends Exception {
	private static final long serialVersionUID = -4957422348531758013L;

	public StorageException() {
		super();
	}

	public StorageException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public StorageException(String arg0) {
		super(arg0);
	}

	public StorageException(Throwable arg0) {
		super(arg0);
	}
}
