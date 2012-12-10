package com.volumetricpixels.bans.crossapi.perapi;

public interface VBMutes {

    public void mutePlayer(String player, long time);

    public void unmutePlayer(String player);

    public boolean isMuted(String player);

}
