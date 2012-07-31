package com.volumetricpixels.bans.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.bukkit.files.VBBukkitConfig;
import com.volumetricpixels.bans.shared.VBPunishments;
import com.volumetricpixels.bans.shared.connection.BanSynchronizer;
import com.volumetricpixels.bans.shared.connection.DataRetriever;
import com.volumetricpixels.bans.shared.perapi.GlobalBanStorer;
import com.volumetricpixels.bans.shared.perapi.VBLocalBans;
import com.volumetricpixels.bans.shared.perapi.VBMutes;
import com.volumetricpixels.bans.shared.util.API;

public class VolumetricBansBukkit extends JavaPlugin implements VolumetricBans {
    
    public final VBBukkitPermissions perms = VBBukkitPermissions.perms;
    public final String noPermsMessage = ChatColor.RED + "You don't have permission to do that!";

    @Override
    public BanSynchronizer getBanSynchronizer() {
        return null;
    }

    @Override
    public String getServerKey() {
        return null;
    }

    @Override
    public VBLocalBans getLocalBanHandler() {
        return null;
    }

    @Override
    public VBMutes getMuteHandler() {
        return null;
    }

    @Override
    public GlobalBanStorer getGlobalBanStorer() {
        return null;
    }

    @Override
    public VBBukkitConfig getVBConfig() {
        return null;
    }

    @Override
    public DataRetriever getMainDataRetriever() {
        return null;
    }

    @Override
    public API getInUseAPI() {
        return API.BUKKIT;
    }

    @Override
    public VBPunishments getPunishmentHandler() {
        return null;
    }
    
}
