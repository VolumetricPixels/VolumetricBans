package com.volumetricpixels.bans.exception;

/** Thrown when there is an error loading or parsing data / files */
public class DataLoadException extends Exception {
    private static final long serialVersionUID = -7698740832606876416L;

    public DataLoadException() {
        super();
    }

    public DataLoadException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DataLoadException(String arg0) {
        super(arg0);
    }

    public DataLoadException(Throwable arg0) {
        super(arg0);
    }
}
