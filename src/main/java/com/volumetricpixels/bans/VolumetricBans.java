package com.volumetricpixels.bans;

import java.io.File;

import com.volumetricpixels.bans.api.VolumetricBansAPI;
import com.volumetricpixels.bans.common.VBPunishments;
import com.volumetricpixels.bans.common.connection.BanSynchronizer;
import com.volumetricpixels.bans.common.connection.DataRetriever;
import com.volumetricpixels.bans.common.connection.PlayerDataRetriever;
import com.volumetricpixels.bans.common.util.API;
import com.volumetricpixels.bans.interfaces.GlobalBanStorer;
import com.volumetricpixels.bans.interfaces.VBConfig;
import com.volumetricpixels.bans.interfaces.VBLocalBans;
import com.volumetricpixels.bans.interfaces.VBMutes;

/**
 * Although this is a PerAPI thing, it does not go in the perapi package because
 * it is the main class for the plugin and any plugins using VolumetricBans' API
 * should interact with this class, not VolumetricBansSpout or other. We keep it
 * here so it makes sense when other plugins import VolumetricBans
 * 
 * @author DziNeIT
 */
public interface VolumetricBans {
    VolumetricBansAPI getAPI();

    BanSynchronizer getBanSynchronizer();

    String getServerKey();

    VBLocalBans getLocalBanHandler();

    VBMutes getMuteHandler();

    GlobalBanStorer getGlobalBanStorer();

    VBConfig getVBConfig();

    DataRetriever getMainDataRetriever();

    API getInUseAPI();

    File getDataFolder();

    VBPunishments getPunishmentHandler();

    PlayerDataRetriever getPlayerDataRetriever();
}
