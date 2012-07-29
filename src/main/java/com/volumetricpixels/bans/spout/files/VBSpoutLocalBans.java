package com.volumetricpixels.bans.spout.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.spout.api.Spout;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.bans.shared.perapi.VBLocalBans;
import com.volumetricpixels.bans.spout.VolumetricBansSpout;
import com.volumetricpixels.bans.spout.punishments.SpoutBan;
import com.volumetricpixels.bans.spout.punishments.VBSpoutPunishTimers;
import com.volumetricpixels.bans.spout.punishments.VBSpoutPunishTimers.VBSpoutBanTimer;

/**
 * Bans File only handles Local Bans
 * Globals handled elsewhere
 */
public class VBSpoutLocalBans implements VBLocalBans {
    
    private final VolumetricBansSpout plugin;
    public final VBSpoutPunishTimers vbspt = new VBSpoutPunishTimers();
    private boolean initialized = false;
    private File dataFolder;
    
    // Sections: 0 = Player Banned, 1 = Reason, 2 = Admin, 4 = Time (only if temp)
    private final List<SpoutBan> localbans = new ArrayList<SpoutBan>();
    private File banFile;
    private YamlConfiguration conf;
    
    private final List<String> exceptions = new ArrayList<String>();
    private final List<String> ignoredReasons = new ArrayList<String>();
    private YamlConfiguration exceptionYaml;
    private File exceptionFile;
    
    public VBSpoutLocalBans(VolumetricBansSpout pl) {
        this.plugin = pl;
    }
    
    // Called in onEnable; NOT when plugin instance created
    public void init() {
        if (initialized) {
            return;
        }
        
        this.dataFolder = plugin.getDataFolder();
        
        this.banFile = new File(dataFolder, "bans.yml");
        this.conf = new YamlConfiguration(banFile);
        
        this.exceptionFile = new File(dataFolder, "exceptions.yml");
        this.exceptionYaml = new YamlConfiguration(exceptionFile);
        
        for (String s : conf.getNode("Bans").getStringList()) {
            String[] sArray = s.split(":");
            if (sArray[4] != null) {
                VBSpoutBanTimer vbsbt = vbspt.new VBSpoutBanTimer(sArray[0], Long.parseLong(sArray[4]));
                int id = Spout.getScheduler().scheduleAsyncRepeatingTask(plugin, vbsbt, 0, 1000, TaskPriority.HIGHEST);
                vbsbt.setTaskId(id);
            }
        }
        
        try {
            conf.load();
            exceptionYaml.load();
        } catch (ConfigurationException e) {}
        
        Map<String, ConfigurationNode> map = conf.getChildren();
        Iterator<Entry<String, ConfigurationNode>> iterator = map.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Entry<String, ConfigurationNode> entry = iterator.next();
            String[] sections = entry.getKey().split(":");
            SpoutBan b = null;
            if (Long.parseLong(sections[4]) != -1 && sections[4] != null) {
                b = new SpoutBan(sections[0], sections[1], sections[2], Long.parseLong(sections[4]));
            } else if (Boolean.parseBoolean(sections[3]) == true) {
                b = new SpoutBan(sections[0], sections[1], sections[2], Boolean.parseBoolean(sections[3]));
            } else {
                b = new SpoutBan(sections[0], sections[1], sections[2], false);
            }
            localbans.add(b);
        }
        
        exceptions.addAll(exceptionYaml.getNode("Ignored-Players").getStringList());
        ignoredReasons.addAll(exceptionYaml.getNode("Ignored-Reasons").getStringList());
        
        initialized = true;
    }
    
    public boolean isBanned(String name) {
        for (SpoutBan b : localbans) {
            if (b.getPlayer().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public String getBanReason(String name) {
        for (SpoutBan b : localbans) {
            if (b.getPlayer().equalsIgnoreCase(name)) {
                return b.getReason();
            }
        }
        return null;
    }
    
    public String getAdmin(String banned) {
        for (SpoutBan b : localbans) {
            if (b.getPlayer().equalsIgnoreCase(banned)) {
                return b.getAdmin();
            }
        }
        return null;
    }
    
    public List<SpoutBan> getBans() {
        return localbans;
    }
    
    public void banPlayer(String name, String reason, String admin, long time) {
        SpoutBan spoutBan = new SpoutBan(name, reason, admin, time);
        localbans.add(spoutBan);
        VBSpoutBanTimer vbsbt = vbspt.new VBSpoutBanTimer(name, time);
        int id = Spout.getScheduler().scheduleAsyncRepeatingTask(plugin, vbsbt, 0, 1000, TaskPriority.HIGHEST);
        vbsbt.setTaskId(id);
        updateConfig(false);
    }
    
    public void banPlayer(String name, String reason, String admin) {
        SpoutBan spoutBan = new SpoutBan(name, reason, admin, false);
        localbans.add(spoutBan);
        updateConfig(false);
    }
    
    public void banPlayer(String name, String admin) {
        banPlayer(name, "Banned!", admin);
    }
    
    public void banPlayer(String name) {
        banPlayer(name, "CONSOLE");
    }
    
    public boolean unbanPlayer(String name) {
        if (!isBanned(name)) {
            return false;
        }
        for (SpoutBan b : localbans) {
            if (b.getPlayer().equalsIgnoreCase(name)) {
                localbans.remove(b);
                updateConfig(true);
                return true;
            }
        }
        return false;
    }
    
    private void updateConfig(boolean unban) {
        if (unban) {
            List<String> toBeInConf = new ArrayList<String>();
            for (SpoutBan b : localbans) {
                String value = "";
                value = b.getPlayer() + ":" + b.getReason() + ":" + b.getAdmin() + ":" + String.valueOf(b.isGlobal()) + ":" + b.getTime();
                toBeInConf.add(value);
            }
            conf.getNode("Bans").setValue(toBeInConf);
        } else {
            List<String> inConf = conf.getNode("Bans").getStringList();
            for (SpoutBan b : localbans) {
                boolean found = false;
                for (String s : inConf) {
                    if (b.getPlayer().equalsIgnoreCase(s.split(":")[0])) {
                        found = true;
                    }
                }
                if (!found) {
                    inConf.add(b.getPlayer() + ":" + b.getReason() + ":" + b.getAdmin() + ":" + String.valueOf(b.isGlobal()) + ":" + b.getTime());
                }
            }
            conf.getNode("Bans").setValue(inConf);
        }
    }
    
}
