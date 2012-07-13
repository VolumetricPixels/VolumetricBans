package com.volumetricpixels.voxelbans.integration;

import org.spout.api.event.Listener;

public interface VBPluginIntegration extends Listener {
    
    public boolean integrationEnabled();
    
}
