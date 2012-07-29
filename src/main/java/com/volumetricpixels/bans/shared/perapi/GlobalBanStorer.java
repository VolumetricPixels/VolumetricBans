package com.volumetricpixels.bans.shared.perapi;

import java.util.List;

import com.volumetricpixels.bans.shared.connection.BanSynchronizer;

public interface GlobalBanStorer {
    
    public void addToTempList(Ban b);
    
    public void remove(BanSynchronizer bs, Ban b) throws IllegalAccessException;
    
    public List<Ban> getBansToSubmit();
    
}
