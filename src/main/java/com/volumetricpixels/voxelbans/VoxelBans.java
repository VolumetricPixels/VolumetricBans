package com.volumetricpixels.voxelbans;

import com.volumetricpixels.voxelbans.shared.perapi.GlobalBanStorer;
import com.volumetricpixels.voxelbans.shared.perapi.VBLocalBans;

/**
 * Although this is a PerAPI thing, it does not go in the perapi package
 * because it is the main class for the plugin and any plugins using
 * VoxelBans' API should interact with this class, not VoxelBansSpout
 * or other. We keep it here so it makes sense when other plugins import
 * VoxelBans
 * @author DziNeIT
 */
public interface VoxelBans {
    
    public String getServerKey();
    
    public VBLocalBans bans();
    
    public GlobalBanStorer gbts();
    
}
