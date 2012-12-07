package com.volumetricpixels.bans.spout.event.punishment;

import com.volumetricpixels.bans.shared.perapi.Ban;

public class PlayerBannedEvent {

    private String player;
    private String reason;
    private String admin;
    private boolean global;
    private long time;

    public PlayerBannedEvent(Ban b) {
        this.player = b.getPlayer();
        this.reason = b.getReason();
        this.admin = b.getAdmin();
        this.global = b.isGlobal();
        this.time = b.getTime();
    }

    public String getPlayer() {
        return player;
    }

    public String getReason() {
        return reason;
    }

    public String getAdmin() {
        return admin;
    }

    public boolean isGlobal() {
        return global;
    }

    public boolean isTemporary() {
        return time != -1;
    }

    public long getTime() {
        return time;
    }

}
