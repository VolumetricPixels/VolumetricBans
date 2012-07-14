package com.volumetricpixels.voxelbans.interfaces;

import java.util.List;

import com.volumetricpixels.voxelbans.connection.BanSynchronizer;

public interface GlobalBanStorer {
    
    public void addToTempList(Ban b);
    
    public void remove(BanSynchronizer bs, Ban b) throws IllegalAccessException;
    
    public List<Ban> getBansToSubmit();
    
}
