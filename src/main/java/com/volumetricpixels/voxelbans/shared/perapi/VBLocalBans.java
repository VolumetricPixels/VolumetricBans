package com.volumetricpixels.voxelbans.shared.perapi;

import java.util.Collection;

public interface VBLocalBans {

    public boolean isBanned(String name);
    
    public String getBanReason(String name);

    public Collection<? extends Ban> getBans();
    
}
