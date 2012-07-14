package com.volumetricpixels.voxelbans.shared.perapi;

import java.util.List;

import com.volumetricpixels.voxelbans.shared.connection.BanSynchronizer;

public interface GlobalBanStorer {
    
    public void addToTempList(Ban b);
    
    public void remove(BanSynchronizer bs, Ban b) throws IllegalAccessException;
    
    public List<Ban> getBansToSubmit();
    
}
