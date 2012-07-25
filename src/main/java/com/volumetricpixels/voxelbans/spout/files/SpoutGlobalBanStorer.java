package com.volumetricpixels.voxelbans.spout.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.VBUtils;
import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.shared.connection.BanSynchronizer;
import com.volumetricpixels.voxelbans.shared.perapi.Ban;
import com.volumetricpixels.voxelbans.shared.perapi.GlobalBanStorer;

public class SpoutGlobalBanStorer implements GlobalBanStorer {
    
    private YamlConfiguration yc;
    
    public SpoutGlobalBanStorer(VoxelBans vb) {
        this.yc = new YamlConfiguration(new File(vb.getDataFolder(), "doNotTouch" + File.separator + "globalBansTemp.yml"));
    }
    
    public void addToTempList(Ban b) {
        List<String> currentTemps = new ArrayList<String>();
        currentTemps.addAll(yc.getNode("notSynchronizedGlobals").getStringList());
        String ban = b.getPlayer() + ":" + b.getReason() + ":" + b.getAdmin();
        currentTemps.add(ban);
        yc.getNode("notSynchronizedGlobals").setValue(currentTemps);
    }
    
    public void remove(BanSynchronizer bs, Ban b) throws IllegalAccessException {
        if (bs == null) {
            throw new IllegalAccessException("Something tried to remove a ban from the GlobalTempBanSaver with a null param!");
        }
        List<String> currentTemps = new ArrayList<String>();
        currentTemps.addAll(yc.getNode("notSynchronizedGlobals").getStringList());
        String ban = b.getPlayer() + ":" + b.getReason() + ":" + b.getAdmin();
        currentTemps.remove(ban);
        yc.getNode("notSynchronizedGlobals").setValue(currentTemps);
    }
    
    public List<Ban> getBansToSubmit() {
        List<Ban> result = new ArrayList<Ban>();
        List<String> inConf = yc.getNode("notSynchronizedGlobals").getStringList();
        for (String s : inConf) {
            String[] banSplit = s.split(":");
            Ban b = VBUtils.newBan(banSplit[0], banSplit[1], banSplit[2], true);
            result.add(b);
        }
        return result;
    }
    
}
