package me.vasil7112.CombinationLock.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.vasil7112.CombinationLock.CombinationLock;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LocksConf {
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private CombinationLock plugin;
	
	public LocksConf(CombinationLock plugin){
	  this.plugin = plugin;
	}

	public FileConfiguration getCustomConfig() {
	    if (customConfig == null) {
	        reloadCustomConfig();
	    }
	    return customConfig;
	}
	
	public void reloadCustomConfig() {
	    if (customConfigFile == null) {
	    	customConfigFile = new File(plugin.getDataFolder(), "Locks.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    InputStream defConfigStream = plugin.getResource("Locks.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	    setupConfig();
	    saveCustomConfig();
	}
	    
    public void saveCustomConfig() {
	    if (customConfig == null || customConfigFile == null) {
	    	return;
	    }
	    try {
	        getCustomConfig().save(customConfigFile);
	    } catch (IOException ex) {
	        plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
    }
    
    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), "Locks.yml");
        }
        if (!customConfigFile.exists()) {            
        	plugin.saveResource("Locks.yml", false);
        }
    }
    
    private void setupConfig(){
    	
    }
}
