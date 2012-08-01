package com.volumetricpixels.bans.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.bukkit.files.VBBukkitConfig;
import com.volumetricpixels.bans.shared.VBPunishments;
import com.volumetricpixels.bans.shared.connection.BanSynchronizer;
import com.volumetricpixels.bans.shared.connection.DataRetriever;
import com.volumetricpixels.bans.shared.connection.PlayerDataRetriever;
import com.volumetricpixels.bans.shared.perapi.GlobalBanStorer;
import com.volumetricpixels.bans.shared.perapi.VBLocalBans;
import com.volumetricpixels.bans.shared.perapi.VBMutes;
import com.volumetricpixels.bans.shared.util.API;

public class VolumetricBansBukkit extends JavaPlugin implements VolumetricBans {
    
    public final VBBukkitPermissions perms = VBBukkitPermissions.perms;
    public final String noPermsMessage = ChatColor.RED + "You don't have permission to do that!";
    
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new VBBukkitPlayerListener(this), this);
        
        CommandExecutor e = new VBBukkitCommandHandler();
        this.getCommand("ban").setExecutor(e);
        this.getCommand("gban").setExecutor(e);
        this.getCommand("tban").setExecutor(e);
        this.getCommand("vbans").setExecutor(e);
        this.getCommand("lookup").setExecutor(e);
        this.getCommand("banreason").setExecutor(e);
        this.getCommand("banlist").setExecutor(e);
        this.getCommand("kick").setExecutor(e);
        this.getCommand("mute").setExecutor(e);
        this.getCommand("unmute").setExecutor(e);
    }

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

    @Override
    public PlayerDataRetriever getPlayerDataRetriever() {
        return null;
    }
    
}
