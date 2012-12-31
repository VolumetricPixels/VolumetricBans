package com.volumetricpixels.bans.spout;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.entity.Player;

import com.volumetricpixels.bans.interfaces.VBPermissions;

/**
 * Easy way to deal with Permissions
 * Stores people with permissions in memory so no often perm checking
 * @author DziNeIT
 */
public class VBSpoutPermissions implements VBPermissions {

    public static final VBSpoutPermissions perms = new VBSpoutPermissions((VolumetricBansSpout) Spout.getEngine().getPluginManager().getPlugin("VolumetricBans"));

    private VolumetricBansSpout plugin;

    private VBSpoutPermissions(VolumetricBansSpout plugin) {
        this.plugin = plugin;
    }

    private final List<String> canGlobalBan = new ArrayList<String>();
    private final List<String> canLocalBan = new ArrayList<String>();
    private final List<String> canViewBans = new ArrayList<String>();
    private final List<String> canTempBan = new ArrayList<String>();
    private final List<String> canUnmute = new ArrayList<String>();
    private final List<String> canUnban = new ArrayList<String>();
    private final List<String> canMute = new ArrayList<String>();
    private final List<String> canKick = new ArrayList<String>();
    private final List<String> admins = new ArrayList<String>();

    @Override
    public boolean canGlobalBan(String player) {
        return canGlobalBan.contains(player);
    }

    @Override
    public boolean canLocalBan(String player) {
        return canLocalBan.contains(player);
    }

    @Override
    public boolean canTempBan(String player) {
        return canTempBan.contains(player);
    }

    @Override
    public boolean canKick(String player) {
        return canKick.contains(player);
    }

    @Override
    public boolean canMute(String player) {
        return canMute.contains(player);
    }

    @Override
    public boolean canUnban(String player) {
        return canUnban.contains(player);
    }

    @Override
    public boolean isAdmin(String player) {
        return admins.contains(player);
    }

    @Override
    public boolean canViewBans(String player) {
        return canViewBans.contains(player);
    }

    public boolean canUnmute(String name) {
        return canUnmute.contains(name);
    }

    public void update(Player p) {
        if (p.hasPermission("voxelbans.bans.global") && !canGlobalBan.contains(p.getName())) {
            canGlobalBan.add(p.getName());
        }
        if (p.hasPermission("voxelbans.bans.local") && !canLocalBan.contains(p.getName())) {
            canLocalBan.add(p.getName());
        }
        if (p.hasPermission("voxelbans.bans.temp") && !canTempBan.contains(p.getName())) {
            canTempBan.add(p.getName());
        }
        if (p.hasPermission("voxelbans.bans.unban") && !canUnban.contains(p.getName())) {
            canUnban.add(p.getName());
        }
        if (p.hasPermission("voxelbans.kick") && !canKick.contains(p.getName())) {
            canKick.add(p.getName());
        }
        if (p.hasPermission("voxelbans.mutes.mute") && !canMute.contains(p.getName())) {
            canMute.add(p.getName());
        }
        if (p.hasPermission("voxelbans.admin") && !admins.contains(p.getName())) {
            admins.add(p.getName());
        }
        if (p.hasPermission("voxelbans.bans.view") && !canViewBans.contains(p.getName())) {
            canViewBans.add(p.getName());
        }
        if (p.hasPermission("voxelbans.mutes.unmute") && !canViewBans.contains(p.getName())) {
            canUnmute.add(p.getName());
        }
    }

    void update() {
        // Updates all players
        for (Player p : ((Server) plugin.getEngine()).getOnlinePlayers()) {
            update(p);
        }
    }

}
