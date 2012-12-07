package com.volumetricpixels.bans.shared.perapi;

import java.util.Collection;

public interface VBLocalBans {

    public boolean isBanned(String name);

    public String getBanReason(String name);

    public Collection<? extends Ban> getBans();

    public String getAdmin(String bannedName);

    public void banPlayer(String name, String reason, String admin, long time);

    public void banPlayer(String name, String reason, String admin);

    public void banPlayer(String name, String admin);

    public void banPlayer(String name);

    public boolean unbanPlayer(String name);

}
