package com.volumetricpixels.bans.crossapi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.crossapi.connection.BanSynchronizer;
import com.volumetricpixels.bans.crossapi.perapi.Ban;
import com.volumetricpixels.bans.crossapi.perapi.GlobalBanStorer;
import com.volumetricpixels.bans.crossapi.util.ConfigConverter;
import com.volumetricpixels.bans.crossapi.util.GeneralUtil;

public class VBGlobalBanStorer implements GlobalBanStorer {
    private ConfigConverter yc;

    public VBGlobalBanStorer(VolumetricBans vb) {
        this.yc = new ConfigConverter(new File(vb.getDataFolder(), "doNotTouch" + File.separator + "globalBansTemp.yml"));
    }

    @Override
    public void addToTempList(Ban b) {
        List<String> currentTemps = new ArrayList<String>();
        currentTemps.addAll(yc.getStringList("notSynchronizedGlobals"));
        String ban = b.getPlayer() + ":" + b.getReason() + ":" + b.getAdmin();
        currentTemps.add(ban);
        yc.setValue("notSynchronizedGlobals", currentTemps);
    }

    @Override
    public void remove(BanSynchronizer bs, Ban b) throws IllegalAccessException {
        if (bs == null) {
            throw new IllegalAccessException("Something tried to remove a ban from the GlobalTempBanSaver with a null param!");
        }

        List<String> currentTemps = new ArrayList<String>();

        currentTemps.addAll(yc.getStringList("notSynchronizedGlobals"));
        String ban = b.getPlayer() + ":" + b.getReason() + ":" + b.getAdmin();
        currentTemps.remove(ban);
        yc.setValue("notSynchronizedGlobals", currentTemps);
    }

    @Override
    public List<Ban> getBansToSubmit() {
        List<Ban> result = new ArrayList<Ban>();
        List<String> inConf = yc.getStringList("notSynchronizedGlobals");

        for (String s : inConf) {
            String[] banSplit = s.split(":");
            Ban b = GeneralUtil.newBan(banSplit[0], banSplit[1], banSplit[2], true);
            result.add(b);
        }

        return result;
    }
}
