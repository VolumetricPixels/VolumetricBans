package com.volumetricpixels.bans.interfaces;

import java.util.List;

import com.volumetricpixels.bans.common.connection.BanSynchronizer;

public interface GlobalBanStorer {
    public void addToTempList(Ban b);

    public void remove(BanSynchronizer bs, Ban b) throws IllegalAccessException;

    public List<Ban> getBansToSubmit();
}
