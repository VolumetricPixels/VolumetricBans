package com.volumetricpixels.bans.shared;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.volumetricpixels.bans.VBUtils;
import com.volumetricpixels.bans.VolumetricBans;
import com.volumetricpixels.bans.shared.connection.BanSynchronizer;
import com.volumetricpixels.bans.shared.perapi.Ban;
import com.volumetricpixels.bans.shared.perapi.GlobalBanStorer;
import com.volumetricpixels.bans.shared.util.ConfigConverter;

public class VBGlobalBanStorer implements GlobalBanStorer {
    
    private ConfigConverter yc;
    
    public VBGlobalBanStorer(VolumetricBans vb) {
        this.yc = new ConfigConverter(new File(vb.getDataFolder(), "doNotTouch" + File.separator + "globalBansTemp.yml"));
    }
    
    public void addToTempList(Ban b) {
        List<String> currentTemps = new ArrayList<String>();
        currentTemps.addAll(yc.getStringList("notSynchronizedGlobals"));
        String ban = b.getPlayer() + ":" + b.getReason() + ":" + b.getAdmin();
        currentTemps.add(ban);
        yc.setValue("notSynchronizedGlobals", currentTemps);
    }
    
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
    
    public List<Ban> getBansToSubmit() {
        List<Ban> result = new ArrayList<Ban>();
        List<String> inConf = yc.getStringList("notSynchronizedGlobals");
        for (String s : inConf) {
            String[] banSplit = s.split(":");
            Ban b = VBUtils.newBan(banSplit[0], banSplit[1], banSplit[2], true);
            result.add(b);
        }
        return result;
    }
    
}
