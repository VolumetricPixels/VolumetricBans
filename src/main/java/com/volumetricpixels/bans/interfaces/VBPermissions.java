package com.volumetricpixels.bans.interfaces;

public interface VBPermissions {

    public boolean canLocalBan(String player);

    public boolean canGlobalBan(String player);

    public boolean canTempBan(String player);

    public boolean isAdmin(String player);

    public boolean canViewBans(String player);

    public boolean canUnban(String player);

    public boolean canMute(String player);

    public boolean canKick(String player);

}
