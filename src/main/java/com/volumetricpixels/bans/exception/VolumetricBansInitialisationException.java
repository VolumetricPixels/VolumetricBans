package com.volumetricpixels.bans.exception;

/** Thrown when there is an error in initialisation of the plugin */
public class VolumetricBansInitialisationException extends Exception {
    private static final long serialVersionUID = 322558845832186170L;

    private boolean isFatal;

    public VolumetricBansInitialisationException(boolean arg0) {
        super();
        isFatal = arg0;
    }

    public VolumetricBansInitialisationException(String arg0, Throwable arg1, boolean arg2) {
        super(arg0, arg1);
        isFatal = arg2;
    }

    public VolumetricBansInitialisationException(String arg0, boolean arg1) {
        super(arg0);
        isFatal = arg1;
    }

    public VolumetricBansInitialisationException(Throwable arg0, boolean arg1) {
        super(arg0);
        isFatal = arg1;
    }

    public boolean isFatal() {
        return isFatal;
    }
}
