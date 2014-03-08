package me.vasil7112.CombinationLock.Listeners;

import java.util.Iterator;
import me.vasil7112.CombinationLock.CombinationLock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.block.Sign;

public class BlockBreakListener implements Listener{

	private CombinationLock plugin;
	
	public BlockBreakListener(CombinationLock plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Player player = e.getPlayer();
		
		if(e.getBlock().getState() instanceof Sign){
			/** Checks if it is the owner, and destroys the sign **/
			Sign sign = (Sign)e.getBlock().getState();
			if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
				if(plugin.lConfig.getCustomConfig().getString(e.getBlock().getLocation().getBlockX()+"_"+e.getBlock().getLocation().getBlockY()+"_"+e.getBlock().getLocation().getBlockZ()+".Owner").equals(player.getName()) || player.hasPermission("CombinationLock.op.destroy")){
					if(player.hasPermission("CombinationLock.destroy") || player.hasPermission("CombinationLock.op.destroy") || player.isOp()){
						plugin.lConfig.getCustomConfig().set(e.getBlock().getLocation().getBlockX()+"_"+e.getBlock().getLocation().getBlockY()+"_"+e.getBlock().getLocation().getBlockZ(), null);
						plugin.lConfig.saveCustomConfig();
						player.sendMessage(plugin.getMessage("LOCK-DESTROYED"));
					}
				}else{
					player.sendMessage(plugin.getMessage("LOCK-DESTROYED-WRONG-OWNER"));
					e.setCancelled(true);
				}
			}
		}else{
			/** Checks if the a lock exists, and cancels the break  **/
			if(e.getBlock().getState() instanceof Chest || e.getBlock().getState() instanceof DoubleChest || e.getBlock().getState() instanceof Furnace){
				if(isDoubleChestLocked(e.getBlock())){
					player.sendMessage(plugin.getMessage("LOCK-NOT-BREAKABLE").replaceAll("%locked_item%", e.getBlock().getType().toString().toLowerCase()));
					e.setCancelled(true);
				}else{
					if(isLocked(e.getBlock())){
						player.sendMessage(plugin.getMessage("LOCK-NOT-BREAKABLE").replaceAll("%locked_item%", e.getBlock().getType().toString().toLowerCase()));
						e.setCancelled(true);
					}
				}
			}
		}
	}

	/** Cancel the explosion of a Locked item.**/
	@EventHandler
	public void onExplosion(EntityExplodeEvent e) {
		 Iterator<Block> iter = e.blockList().iterator();
		 while (iter.hasNext()){
			 Block block = (Block)iter.next();
			 if(block.getType() == Material.CHEST){
				 if(isDoubleChestLocked(block)){
					 iter.remove();
				 }else{
	        		if(isLocked(block)){
	        			 iter.remove();	
	        		}
	        	}
			 }else{
				 if(isLocked(block)){
					 iter.remove();
			 		}else{
				 		if(block.getType() == Material.WALL_SIGN){
				 		Sign sign = (Sign) block.getState();
		    			if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
		    				iter.remove();
		    			}
				 	}
			 	}
			 }
		}
	}
	
	private Boolean isDoubleChestLocked(Block block){
		BlockFace[] aside = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		for(BlockFace bf : aside)
		{
		    if(block.getRelative(bf, 1).getType() == Material.CHEST)
		    {
		    	if(isLocked(block.getRelative(bf, 1)) || isLocked(block)){
		    		
		    		return true;
		    	}
		    }
		}
		return false;
	}
	
	private Boolean isLocked(Block block){
		Sign sign;
		org.bukkit.material.Sign sign2;
		if (block.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN){
			sign = (Sign) block.getRelative(BlockFace.NORTH).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.NORTH).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.SOUTH){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					return true;
				}
			}
		}
		if(block.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN){
			sign = (Sign) block.getRelative(BlockFace.EAST).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.EAST).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.WEST){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					return true;
				}
			}
		}
		if(block.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN){
			sign = (Sign) block.getRelative(BlockFace.SOUTH).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.SOUTH).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.NORTH){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					return true;
				}
			}
		}
		if(block.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN){
			sign = (Sign) block.getRelative(BlockFace.WEST).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.WEST).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.EAST){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					return true;
				}
			}
		}
		return false;
	}
}
