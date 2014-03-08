package me.vasil7112.CombinationLock.Listeners;

import java.util.Random;
import me.vasil7112.CombinationLock.CombinationLock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sign;

public class LockPlaceListener implements Listener{

	private CombinationLock plugin;
	
	public LockPlaceListener(CombinationLock plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
    public void onSignPlaceEvent(SignChangeEvent e) {
        String[] line = e.getLines();
        Player player = e.getPlayer();
        /** Checks if the sign already exists and the user entered a password. **/
        if(plugin.lConfig.getCustomConfig().get(e.getBlock().getLocation().getBlockX()+"_"+e.getBlock().getLocation().getBlockY()+"_"+e.getBlock().getLocation().getBlockZ()) != null){
        	if(line[0].equalsIgnoreCase(plugin.lConfig.getCustomConfig().getString(e.getBlock().getLocation().getBlockX()+"_"+e.getBlock().getLocation().getBlockY()+"_"+e.getBlock().getLocation().getBlockZ()+".Password"))){
        		e.setLine(0, "[CombLock]");
            	e.setLine(1, "UNLOCKED");
            	e.setLine(2, "999");
            	e.setLine(3, "");
            	e.getBlock().getState().update();
            	plugin.getMessage("LOCK-UNLOCKED");
        	}else{
        		e.setLine(0, "[CombLock]");
            	e.setLine(1, "LOCKED");
            	e.setLine(2, "999");
            	e.setLine(3, "");
            	e.getBlock().getState().update();
            	player.sendMessage(plugin.getMessage("LOCK-WRONG-PASSWORD"));
        	}
        	return;
        }
        
        /** Try to place a lock **/
        if(line[0].equalsIgnoreCase("[CombLock]")) {
        	Sign sign = (Sign) e.getBlock().getState().getData();
        	Block attached = e.getBlock().getRelative(sign.getAttachedFace());
        
        	/** Checks for permissions **/
        	if(!player.hasPermission("CombinationLock.place")){
        		player.sendMessage(plugin.getMessage("LOCK-NOT-ENOUGH-PERMS-TO-PLACE"));
        		e.getBlock().setType(Material.AIR);
        		player.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SIGN, 1));
        		e.setCancelled(true);
        		return;
        	}
        	
        	/** Checks if a lock can be placed on that block **/
        	if(attached.getType() == Material.CHEST || attached.getType()== Material.FURNACE /** || **/){}else{
        		player.sendMessage(plugin.getMessage("LOCK-UNPLACEABLE"));
        		player.sendMessage(attached.getType().toString());
        		e.getBlock().setType(Material.AIR);
        		player.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SIGN, 1));
        		return;
        	}
        	
        	/** Check if the block is already locked and cancels the event **/
        	if(attached.getType() == Material.CHEST){
        		if(isDoubleChestLocked(attached)){
        			e.getBlock().setType(Material.AIR);
        			player.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SIGN, 1));
        			return;
        		}else{
        			if(isLocked(attached, false)){
        				e.getBlock().setType(Material.AIR);
        				player.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SIGN, 1));
        				return;
        			}
        		}
        	}else{
        		if(isLocked(attached, false)){
    				e.getBlock().setType(Material.AIR);
    				player.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SIGN, 1));
    				return;
    			}
        	}
        	
        	
        	/** Place the lock successfully **/
            if(line[1] == null || line[1].equalsIgnoreCase("")){
            	String password = generatePassword();
            	e.setLine(0, "[CombLock]");
            	e.setLine(1, "LOCKED");
            	e.setLine(2, "999");
            	e.setLine(3, "");
                e.getBlock().getState().update();
                player.sendMessage(plugin.getMessage("LOCK-CREATED").replaceAll("%password%", password));
                saveLocationToConfig(e.getBlock().getLocation(), player, password);
                return;
            }else{
            	String password = e.getLine(1);
            	if(!isNumeric(password, player)){
            		player.sendMessage("Password must be numeric only!");
            		e.getBlock().setType(Material.AIR);
            		player.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SIGN, 1));
            	}else{
            		e.setLine(0, "[CombLock]");
                	e.setLine(1, "LOCKED");
                	e.setLine(2, "999");
                	e.setLine(3, "");
                    e.getBlock().getState().update();
                    player.sendMessage(plugin.getMessage("LOCK-CREATED").replaceAll("%password%", password));
                    saveLocationToConfig(e.getBlock().getLocation(), player, password);
            	}
            	return;
            }
        }
    }
	
	private String generatePassword(){
		String Password;
		Random rand = new Random();
		int randNum = rand.nextInt(1000);
		if (randNum <10){
			Password = "00" + String.valueOf(randNum);
		}else if (randNum <100){
			Password = "0" + String.valueOf(randNum) ;
		}else{
			Password = String.valueOf(randNum);
		}
		return Password;
	}
	
	@SuppressWarnings("unused")
	private Boolean isNumeric(String number, Player player){
		try{
			Integer number1;
			number1 = Integer.valueOf(number) + 0;
			return true;
		} catch (NumberFormatException nfe) {     
		}
		return false;
	}
	
	private void saveLocationToConfig(Location location, Player owner, String password){
		plugin.lConfig.getCustomConfig().set(location.getBlockX()+"_"+location.getBlockY()+"_"+location.getBlockZ()+".Owner", String.valueOf(owner.getName()));
		plugin.lConfig.getCustomConfig().set(location.getBlockX()+"_"+location.getBlockY()+"_"+location.getBlockZ()+".Password", String.valueOf(password));
		plugin.lConfig.getCustomConfig().set(location.getBlockX()+"_"+location.getBlockY()+"_"+location.getBlockZ()+".Locked", Boolean.valueOf(true));
		plugin.lConfig.saveCustomConfig();
	}
	
	private Boolean isDoubleChestLocked(Block block){
		BlockFace[] aside = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		for(BlockFace bf : aside)
		{
		    if(block.getRelative(bf, 1).getType() == Material.CHEST)
		    {
		    	if(isLocked(block.getRelative(bf, 1), false) || isLocked(block, false)){
		    		return true;
		    	}
		    }
		}
		return false;
	}
	
	private Boolean isLocked(Block block, Boolean checkLocked){
		org.bukkit.block.Sign sign;
		org.bukkit.material.Sign sign2;
		if (block.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN){
			sign = (org.bukkit.block.Sign) block.getRelative(BlockFace.NORTH).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.NORTH).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.SOUTH){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(checkLocked){
						if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
							return true;
						}
					}else{
						return true;
					}
				}
			}
		}
		if(block.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN){
			sign = (org.bukkit.block.Sign) block.getRelative(BlockFace.EAST).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.EAST).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.WEST){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(checkLocked){
						if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
							return true;
						}
					}else{
						return true;
					}
				}
			}
		}
		if(block.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN){
			sign = (org.bukkit.block.Sign) block.getRelative(BlockFace.SOUTH).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.SOUTH).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.NORTH){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(checkLocked){
						if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
							return true;
						}
					}else{
						return true;
					}
				}
			}
		}
		if(block.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN){
			sign = (org.bukkit.block.Sign) block.getRelative(BlockFace.WEST).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.WEST).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.EAST){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(checkLocked){
						if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
							return true;
						}
					}else{
						return true;
					}
				}
			}
		}
		return false;
	}
}
