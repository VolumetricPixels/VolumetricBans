package com.volumetricpixels.voxelbans.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import com.volumetricpixels.voxelbans.VoxelBans;
import com.volumetricpixels.voxelbans.punishments.Ban;

public class VBBanFile {
    
    private final VoxelBans plugin;
    private boolean initialized = false;
    private File dataFolder;
    
    // Sections: 0 = Player Banned, 1 = Reason, 2 = Admin, 3 = Global (true/false), 4 = Time (only if temp)
    private final List<Ban> bans = new ArrayList<Ban>();
    private File banFile;
    private YamlConfiguration conf;
    
    private final List<String> exceptions = new ArrayList<String>();
    private final List<String> ignoredReasons = new ArrayList<String>();
    private YamlConfiguration exceptionYaml;
    private File exceptionFile;
    
    public VBBanFile(VoxelBans pl) {
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
        
        try {
            conf.load();
            exceptionYaml.load();
        } catch (ConfigurationException e) {}
        
        Map<String, ConfigurationNode> map = conf.getChildren();
        Iterator<Entry<String, ConfigurationNode>> iterator = map.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Entry<String, ConfigurationNode> entry = iterator.next();
            String[] sections = entry.getKey().split(":");
            Ban b = null;
            if (Long.parseLong(sections[4]) != -1 && sections[4] != null) {
                b = new Ban(sections[0], sections[1], sections[2], Long.parseLong(sections[4]));
            } else if (Boolean.parseBoolean(sections[3]) == true) {
                b = new Ban(sections[0], sections[1], sections[2], Boolean.parseBoolean(sections[3]));
            } else {
                b = new Ban(sections[0], sections[1], sections[2], false);
            }
            bans.add(b);
        }
        
        exceptions.addAll(exceptionYaml.getNode("Ignored-Players").getStringList());
        ignoredReasons.addAll(exceptionYaml.getNode("Ignored-Reasons").getStringList());
        
        initialized = true;
    }
    
    public boolean isBanned(String name) {
        for (Ban b : bans) {
            if (b.getPlayer().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public String getBanReason(String name) {
        for (Ban b : bans) {
            if (b.getPlayer().equalsIgnoreCase(name)) {
                return b.getReason();
            }
        }
        return null;
    }
    
    public String getAdmin(String banned) {
        for (Ban b : bans) {
            if (b.getPlayer().equalsIgnoreCase(banned)) {
                return b.getAdmin();
            }
        }
        return null;
    }
    
    public List<Ban> getBans() {
        return bans;
    }
    
    public void banPlayer(String name, String reason, String admin, long time) {
        Ban ban = new Ban(name, reason, admin, time);
        bans.add(ban);
        updateConfig(false);
    }
    
    public void banPlayer(String name, String reason, String admin, boolean global) {
        Ban ban = new Ban(name, reason, admin, global);
        bans.add(ban);
        updateConfig(false);
    }
    
    public void banPlayer(String name, String admin) {
        banPlayer(name, "Banned!", admin, false);
    }
    
    public void banPlayer(String name) {
        banPlayer(name, "CONSOLE");
    }
    
    public boolean unbanPlayer(String name) {
        for (Ban b : bans) {
            if (b.getPlayer().equalsIgnoreCase(name)) {
                bans.remove(b);
                updateConfig(true);
                return true;
            }
        }
        return false;
    }
    
    private void updateConfig(boolean unban) {
        if (unban) {
            List<String> toBeInConf = new ArrayList<String>();
            for (Ban b : bans) {
                String value = "";
                value = b.getPlayer() + ":" + b.getReason() + ":" + b.getAdmin() + ":" + String.valueOf(b.isGlobal()) + ":" + b.getTime();
                toBeInConf.add(value);
            }
            conf.getNode("Bans").setValue(toBeInConf);
        } else {
            List<String> inConf = conf.getNode("Bans").getStringList();
            for (Ban b : bans) {
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
