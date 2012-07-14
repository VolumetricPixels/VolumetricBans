package com.volumetricpixels.voxelbans.spout.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.connection.BanSynchronizer;
import com.volumetricpixels.voxelbans.interfaces.Ban;
import com.volumetricpixels.voxelbans.interfaces.GlobalBanStorer;
import com.volumetricpixels.voxelbans.spout.VoxelBansSpout;
import com.volumetricpixels.voxelbans.spout.punishments.SpoutBan;

public class SpoutGlobalBanStorer implements GlobalBanStorer {
    
    private YamlConfiguration yc;
    
    public SpoutGlobalBanStorer(VoxelBansSpout vb) {
        this.yc = new YamlConfiguration(new File(vb.getDataFolder(), "doNotTouch" + File.separator + "globalBansTemp.yml"));
    }
    
    public void addToTempList(Ban b) {
        List<String> currentTemps = new ArrayList<String>();
        currentTemps.addAll(yc.getNode("notSynchronizedGlobals").getStringList());
        String ban = ((SpoutBan) b).getPlayer() + ":" + ((SpoutBan) b).getReason() + ":" + ((SpoutBan) b).getAdmin();
        currentTemps.add(ban);
        yc.getNode("notSynchronizedGlobals").setValue(currentTemps);
    }
    
    public void remove(BanSynchronizer bs, Ban b) throws IllegalAccessException {
        if (bs == null) {
            throw new IllegalAccessException("Something tried to remove a ban from the GlobalTempBanSaver with a null param!");
        }
        List<String> currentTemps = new ArrayList<String>();
        currentTemps.addAll(yc.getNode("notSynchronizedGlobals").getStringList());
        String ban = ((SpoutBan) b).getPlayer() + ":" + ((SpoutBan) b).getReason() + ":" + ((SpoutBan) b).getAdmin();
        currentTemps.remove(ban);
        yc.getNode("notSynchronizedGlobals").setValue(currentTemps);
    }
    
    public List<Ban> getBansToSubmit() {
        List<Ban> result = new ArrayList<Ban>();
        List<String> inConf = yc.getNode("notSynchronizedGlobals").getStringList();
        for (String s : inConf) {
            String[] banSplit = s.split(":");
            SpoutBan b = new SpoutBan(banSplit[0], banSplit[1], banSplit[2], true);
            result.add(b);
        }
        return result;
    }
    
}
