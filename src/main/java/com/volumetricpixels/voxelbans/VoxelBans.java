package com.volumetricpixels.voxelbans;

import java.io.File;

import com.volumetricpixels.voxelbans.shared.connection.BanSynchronizer;
import com.volumetricpixels.voxelbans.shared.connection.DataRetriever;
import com.volumetricpixels.voxelbans.shared.perapi.GlobalBanStorer;
import com.volumetricpixels.voxelbans.shared.perapi.VBConfig;
import com.volumetricpixels.voxelbans.shared.perapi.VBLocalBans;
import com.volumetricpixels.voxelbans.shared.perapi.VBMutes;
import com.volumetricpixels.voxelbans.shared.util.API;

/**
 * Although this is a PerAPI thing, it does not go in the perapi package
 * because it is the main class for the plugin and any plugins using
 * VoxelBans' API should interact with this class, not VoxelBansSpout
 * or other. We keep it here so it makes sense when other plugins import
 * VoxelBans
 * @author DziNeIT
 */
public interface VoxelBans {
    
    public BanSynchronizer getBanSynchronizer();
    
    public String getServerKey();
    
    public VBLocalBans getLocalBanHandler();
    
    public VBMutes getMuteHandler();
    
    public GlobalBanStorer getGlobalBanStorer();
    
    public VBConfig getVBConfig();
    
    public DataRetriever getMainDataRetriever();
    
    public API getInUseAPI();
    
    public File getDataFolder();
    
}
