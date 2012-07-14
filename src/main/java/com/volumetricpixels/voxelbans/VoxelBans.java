package com.volumetricpixels.voxelbans;

import com.volumetricpixels.voxelbans.interfaces.GlobalBanStorer;
import com.volumetricpixels.voxelbans.interfaces.VBLocalBans;

public interface VoxelBans {
    
    public String getServerKey();
    
    public VBLocalBans bans();
    
    public GlobalBanStorer gbts();
    
}
