package me.vasil7112.CombinationLock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import me.vasil7112.CombinationLock.Configuration.ConfigConf;
import me.vasil7112.CombinationLock.Configuration.LocksConf;
import me.vasil7112.CombinationLock.Listeners.BlockBreakListener;
import me.vasil7112.CombinationLock.Listeners.LockPlaceListener;
import me.vasil7112.CombinationLock.Listeners.UnlockListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class CombinationLock extends JavaPlugin{

	public ConfigConf cConfig = new ConfigConf(this);
	public LocksConf lConfig = new LocksConf(this);
	@Override
	public void onEnable(){
		//Creating Configuration
		cConfig.getCustomConfig();
		lConfig.getCustomConfig();
		Bukkit.getPluginManager().registerEvents(new LockPlaceListener(this), this);
		Bukkit.getPluginManager().registerEvents(new UnlockListener(this), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this), this);
		
		/**
		if(cConfig.getCustomConfig().getBoolean("AutoUpdate")){
			@SuppressWarnings("unused")
			Updater updateCheck = new Updater(// This doesn't yet exist?!);
		}**/
		/** MCSTATS.ORG Login is bugged / down. Meanwhile Metrics are disabled so noone can register this plugin as theirs.
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {}
		**/
		if(cConfig.getCustomConfig().get("Premium-User")!=null){
			if(cConfig.getCustomConfig().getString("Premium-User.Username")!=null && cConfig.getCustomConfig().getString("Premium-User.Password")!=null && cConfig.getCustomConfig().getString("Premium-User.Server-IP")!=null && cConfig.getCustomConfig().getString("Premium-User.Server-IMG-468x60")!=null){
				URL url;
				 
				try {
					url = new URL("http://vasil7112.nodedevs.net/scripts/sleepyfeeling.upload.img.script.php?u="+cConfig.getCustomConfig().get("Premium-User.Username")+"&p="+cConfig.getCustomConfig().get("Premium-User.Password")+"&ip="+cConfig.getCustomConfig().get("Premium-User.Server-IP")+"&img="+cConfig.getCustomConfig().get("Premium-User.Server-IMG-468x60"));
					URLConnection conn = url.openConnection();
		 
					BufferedReader br = new BufferedReader(
		                               new InputStreamReader(conn.getInputStream()));
		 
					String inputLine;
					
					while ((inputLine = br.readLine()) != null) {
						if(inputLine.contains("not exist!")){
							getLogger().info("This username doesn't exist!");
						}else if(inputLine.contains("not found!")){
							getLogger().info("Your password is wrong!");
						}else if(inputLine.contains("Error3")){
							getLogger().info("You forgot to enter your server IP!");
						}else if(inputLine.contains("Error4")){
							getLogger().info("You forgot to enter your server IMG link.");
						}else if(inputLine.contains("Error5")){
							getLogger().info("The link must end in .png or .jpg!");
						}else if(inputLine.contains("Error6")){
							getLogger().info("The link must start with http:// or https://");
						}else if(inputLine.contains("done!")){
							getLogger().info("Your Premium Features have been activated.");
							
							Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
								@Override
								public void run() {
									URL url2;
									try {
										url2 = new URL("http://vasil7112.nodedevs.net/scripts/sleepyfeeling.upload.img.script.php?u="+cConfig.getCustomConfig().get("Premium-User.Username")+"&p="+cConfig.getCustomConfig().get("Premium-User.Password")+"&ip="+cConfig.getCustomConfig().get("Premium-User.Server-IP")+"&img="+cConfig.getCustomConfig().get("Premium-User.Server-IMG-468x60"));
										URLConnection conn = url2.openConnection();
										 
										BufferedReader br2 = new BufferedReader(
							                               new InputStreamReader(conn.getInputStream()));
										br2.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
					        }, 0L, 20 * 60 * 10L);
							
						}
					}
					br.close();
		 
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void onDisable(){
		cConfig.saveCustomConfig();
		lConfig.saveCustomConfig();
	}
	
	public String getMessage(String string){
		if(string.equalsIgnoreCase("LOCK-CREATED")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-CREATED"));
		}else if(string.equalsIgnoreCase("LOCK-DESTROYED")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-DESTROYED"));
		}else if(string.equalsIgnoreCase("LOCK-DESTROYED-WRONG-OWNER")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-DESTROYED-WRONG-OWNER"));
		}else if(string.equalsIgnoreCase("LOCK-NOT-BREAKABLE")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-NOT-BREAKABLE"));
		}else if(string.equalsIgnoreCase("LOCK-UNLOCKED")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-UNLOCKED"));
		}else if(string.equalsIgnoreCase("LOCK-WRONG-PASSWORD")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-WRONG-PASSWORD"));
		}else if(string.equalsIgnoreCase("LOCK-UNPLACEABLE")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-UNPLACEABLE"));
		}else if(string.equalsIgnoreCase("LOCK-NOT-ENOUGH-PERMS-TO-PLACE")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-NOT-ENOUGH-PERMS-TO-PLACE"));
		}else if(string.equalsIgnoreCase("LOCK-UNLOCKED")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-UNLOCKED"));
		}else if(string.equalsIgnoreCase("LOCK-ISLOCKED")){
			return ChatColor.translateAlternateColorCodes('&', cConfig.getCustomConfig().getString("Language.LOCK-ISLOCKED"));
		}
		return ChatColor.RED+"Cannot find custom message? - Please contact the developer of the plugin!";
	}
}
