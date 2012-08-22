package com.volumetricpixels.bans.bukkit.files;

import java.util.Collection;

import com.volumetricpixels.bans.bukkit.punishments.VBBukkitPunishTimers;
import com.volumetricpixels.bans.shared.perapi.Ban;
import com.volumetricpixels.bans.shared.perapi.VBLocalBans;

public class VBBukkitLocalBans implements VBLocalBans {
    
    public final VBBukkitPunishTimers pt = new VBBukkitPunishTimers();

    @Override
    public boolean isBanned(String name) {
        return false;
    }

    @Override
    public String getBanReason(String name) {
        return null;
    }

    @Override
    public Collection<? extends Ban> getBans() {
        return null;
    }

    @Override
    public String getAdmin(String bannedName) {
        return null;
    }

    @Override
    public void banPlayer(String name, String reason, String admin, long time) {
        
    }

    @Override
    public void banPlayer(String name, String reason, String admin) {
        
    }

    @Override
    public void banPlayer(String name, String admin) {
        
    }

    @Override
    public void banPlayer(String name) {
        
    }

    @Override
    public boolean unbanPlayer(String name) {
        return false;
    }
    
}
