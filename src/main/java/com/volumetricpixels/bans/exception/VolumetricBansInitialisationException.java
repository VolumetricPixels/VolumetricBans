package com.volumetricpixels.bans.exception;

/** Thrown when there is an error in initialisation of the plugin */
public class VolumetricBansInitialisationException extends Exception {
    private static final long serialVersionUID = 322558845832186170L;

    private final boolean isFatal;

    public VolumetricBansInitialisationException(final boolean arg0) {
        super();
        isFatal = arg0;
    }

    public VolumetricBansInitialisationException(final String arg0, final Throwable arg1, final boolean arg2) {
        super(arg0, arg1);
        isFatal = arg2;
    }

    public VolumetricBansInitialisationException(final String arg0, final boolean arg1) {
        super(arg0);
        isFatal = arg1;
    }

    public VolumetricBansInitialisationException(final Throwable arg0, final boolean arg1) {
        super(arg0);
        isFatal = arg1;
    }

    public boolean isFatal() {
        return isFatal;
    }
}
