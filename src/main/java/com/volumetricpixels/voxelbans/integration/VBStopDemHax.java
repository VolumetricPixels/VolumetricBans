package com.volumetricpixels.voxelbans.integration;

import org.spout.api.Spout;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Order;
import org.spout.api.plugin.Plugin;

import com.volumetricpixels.voxelbans.VoxelBans;

import en.dzine.stopdemhax.checks.CheckEvent;

public class VBStopDemHax implements VBPluginIntegration {
    
    private VoxelBans vb;
    
    public VBStopDemHax() {
        this.vb = (VoxelBans) Spout.getEngine().getPluginManager().getPlugin("VoxelBans");
    }

    @Override
    public boolean integrationEnabled() {
        Plugin p = Spout.getEngine().getPluginManager().getPlugin("StopDemHax");
        if (p != null) {
            return p.isEnabled() && vb.getConfig().getNode("Integration.StopDemHax.Enable").getBoolean();
        }
        return false;
    }
    
    @EventHandler(order = Order.LATEST)
    public void onCheckEvent(CheckEvent e) {
        if (e.getVL() > vb.getConfig().getNode("Integration.StopDemHax.MaxVlBeforeBan").getDouble()) {
            vb.punishments.localBanPlayer(e.getPlayer().getName(), "Hacking (VL: " + e.getVL() + ") - Local Ban!", "VB Integration For StopDemHax");
        }
    }
    
}
