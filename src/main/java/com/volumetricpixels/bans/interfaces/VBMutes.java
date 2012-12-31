package com.volumetricpixels.bans.interfaces;

public interface VBMutes {

    public void mutePlayer(String player, long time);

    public void unmutePlayer(String player);

    public boolean isMuted(String player);

}
