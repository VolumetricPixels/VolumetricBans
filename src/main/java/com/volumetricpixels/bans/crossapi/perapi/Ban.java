package com.volumetricpixels.bans.crossapi.perapi;

public interface Ban {
    public String getPlayer();

    public String getReason();

    public String getAdmin();

    public boolean isGlobal();

    public boolean isTemporary();

    public long getTime();
}
