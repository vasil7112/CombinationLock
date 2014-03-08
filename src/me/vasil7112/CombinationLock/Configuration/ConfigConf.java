package me.vasil7112.CombinationLock.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.vasil7112.CombinationLock.CombinationLock;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigConf {
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private CombinationLock plugin;
	
	public ConfigConf(CombinationLock plugin){
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
	    	customConfigFile = new File(plugin.getDataFolder(), "Config.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    InputStream defConfigStream = plugin.getResource("Config.yml");
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
            customConfigFile = new File(plugin.getDataFolder(), "Config.yml");
        }
        if (!customConfigFile.exists()) {            
        	plugin.saveResource("Config.yml", false);
        }
    }
    
    private void setupConfig(){
    	if(getCustomConfig().get("AutoUpdate")==null){
    		getCustomConfig().set("AutoUpdate", Boolean.valueOf(true));	
    	}
    	if(getCustomConfig().get("Premium-User")==null){
    		getCustomConfig().set("Premium-User.Username", String.valueOf(""));	
    		getCustomConfig().set("Premium-User.Password", String.valueOf(""));
    		getCustomConfig().set("Premium-User.Server-IP", String.valueOf(""));
    		getCustomConfig().set("Premium-User.Server-IMG-468x60", String.valueOf(""));
    	}
    	if(getCustomConfig().get("Language.LOCK-CREATED")==null){
    		getCustomConfig().set("Language.LOCK-CREATED", String.valueOf("&2The lock has been placed! Your password: %password%"));	
    	}
    	if(getCustomConfig().get("Language.LOCK-DESTROYED")==null){
    		getCustomConfig().set("Language.LOCK-DESTROYED", String.valueOf("&2The lock has been destroyed!"));	
    	}
    	if(getCustomConfig().get("Language.LOCK-DESTROYED-WRONG-OWNER")==null){
    		getCustomConfig().set("Language.LOCK-DESTROYED-WRONG-OWNER", String.valueOf("&4You cannot destroy this lock! You are not its owner."));	
    	}
    	if(getCustomConfig().get("Language.LOCK-NOT-BREAKABLE")==null){
    		getCustomConfig().set("Language.LOCK-NOT-BREAKABLE", String.valueOf("&4You cannot destroy this %locked_item%. It is locked!"));	
    	}
    	if(getCustomConfig().get("Language.LOCK-UNLOCKED")==null){
    		getCustomConfig().set("Language.LOCK-UNLOCKED", String.valueOf("&2The lock has been unlocked!"));	
    	}
    	if(getCustomConfig().get("Language.LOCK-WRONG-PASSWORD")==null){
    		getCustomConfig().set("Language.LOCK-WRONG-PASSWORD", String.valueOf("&4You entered a wrong password. The lock remains locked!"));	
    	}
    	if(getCustomConfig().get("Language.LOCK-UNPLACEABLE")==null){
    		getCustomConfig().set("Language.LOCK-UNPLACEABLE", String.valueOf("&4You cannot place a lock on that!"));	
    	}
    	if(getCustomConfig().get("Language.LOCK-NOT-ENOUGH-PERMS-TO-PLACE")==null){
    		getCustomConfig().set("Language.LOCK-NOT-ENOUGH-PERMS-TO-PLACE", String.valueOf("&4You do not have enough permissions to place a lock!"));	
    	}
    	if(getCustomConfig().get("Language.LOCK-ISLOCKED")==null){
    		getCustomConfig().set("Language.LOCK-ISLOCKED", String.valueOf("&4This %locked_item% is locked! You cannot open it."));	
    	}
    }
}
