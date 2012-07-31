package com.volumetricpixels.bans;

import java.io.File;

import com.volumetricpixels.bans.shared.VBPunishments;
import com.volumetricpixels.bans.shared.connection.BanSynchronizer;
import com.volumetricpixels.bans.shared.connection.DataRetriever;
import com.volumetricpixels.bans.shared.perapi.GlobalBanStorer;
import com.volumetricpixels.bans.shared.perapi.VBConfig;
import com.volumetricpixels.bans.shared.perapi.VBLocalBans;
import com.volumetricpixels.bans.shared.perapi.VBMutes;
import com.volumetricpixels.bans.shared.util.API;

/**
 * Although this is a PerAPI thing, it does not go in the perapi package
 * because it is the main class for the plugin and any plugins using
 * VolumetricBans' API should interact with this class, not VolumetricBansSpout
 * or other. We keep it here so it makes sense when other plugins import
 * VolumetricBans
 * @author DziNeIT
 */
public interface VolumetricBans {
    
    public BanSynchronizer getBanSynchronizer();
    
    public String getServerKey();
    
    public VBLocalBans getLocalBanHandler();
    
    public VBMutes getMuteHandler();
    
    public GlobalBanStorer getGlobalBanStorer();
    
    public VBConfig getVBConfig();
    
    public DataRetriever getMainDataRetriever();
    
    public API getInUseAPI();
    
    public File getDataFolder();
    
    public VBPunishments getPunishmentHandler();
    
}
