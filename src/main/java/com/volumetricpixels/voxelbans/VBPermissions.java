package com.volumetricpixels.voxelbans;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.player.Player;

public class VBPermissions {
    
    public static final VBPermissions perms = new VBPermissions((VoxelBans) Spout.getEngine().getPluginManager().getPlugin("VoxelBans"));
    
    private VoxelBans plugin;
    
    private VBPermissions(VoxelBans plugin) {
        this.plugin = plugin;
    }
    
    private final List<String> canGlobalBan = new ArrayList<String>();
    private final List<String> canLocalBan = new ArrayList<String>();
    private final List<String> canViewBans = new ArrayList<String>();
    private final List<String> canTempBan = new ArrayList<String>();
    private final List<String> canUnban = new ArrayList<String>();
    private final List<String> canMute = new ArrayList<String>();
    private final List<String> canKick = new ArrayList<String>();
    private final List<String> admins = new ArrayList<String>();
    
    public boolean canGlobalBan(String player) {
        return canGlobalBan.contains(player);
    }
    
    public boolean canLocalBan(String player) {
        return canLocalBan.contains(player);
    }
    
    public boolean canTempBan(String player) {
        return canTempBan.contains(player);
    }
    
    public boolean canKick(String player) {
        return canKick.contains(player);
    }
    
    public boolean canMute(String player) {
        return canMute.contains(player);
    }
    
    public boolean canUnban(String player) {
        return canUnban.contains(player);
    }

    public boolean isAdmin(String player) {
        return admins.contains(player);
    }

    public boolean canViewBans(String player) {
        return canViewBans.contains(player);
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
        if (p.hasPermission("voxelbans.mute") && !canMute.contains(p.getName())) {
            canMute.add(p.getName());
        }
        if (p.hasPermission("voxelbans.admin") && !admins.contains(p.getName())) {
            admins.add(p.getName());
        }
        if (p.hasPermission("voxelbans.bans.view") && !canViewBans.contains(p.getName())) {
            canViewBans.add(p.getName());
        }
    }
    
    void update() {
        for (Player p : plugin.getEngine().getOnlinePlayers()) {
            update(p);
        }
    }
    
}
