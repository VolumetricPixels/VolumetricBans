package com.volumetricpixels.bans.crossapi.perapi;

import java.util.List;

import com.volumetricpixels.bans.crossapi.connection.BanSynchronizer;

public interface GlobalBanStorer {
    public void addToTempList(Ban b);

    public void remove(BanSynchronizer bs, Ban b) throws IllegalAccessException;

    public List<Ban> getBansToSubmit();
}
