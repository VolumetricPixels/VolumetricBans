package com.volumetricpixels.voxelbans.shared.perapi;

public interface VBPunishments {
    
    public void globalBanPlayer(String player, String reason, String admin);
    
    public void localBanPlayer(String player, String reason, String admin);
    
    public void tempBanPlayer(String player, String reason, String admin, long time);
    
    public void unbanPlayer(String player);
    
    public void kickPlayer(String player, Object... reason);
    
    public void mutePlayer(String player, long time);
    
    public void unmutePlayer(String player);
    
    public boolean isMuted(String player);
    
    public boolean isGlobalBanned(String player);
    
    public boolean isLocalBanned(String player);
    
}
