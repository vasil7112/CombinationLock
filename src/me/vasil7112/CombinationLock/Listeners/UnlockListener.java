package me.vasil7112.CombinationLock.Listeners;

import me.vasil7112.CombinationLock.CombinationLock;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.Packet;
import net.minecraft.server.v1_7_R1.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_7_R1.PacketPlayOutUpdateSign;
import net.minecraft.server.v1_7_R1.TileEntitySign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class UnlockListener implements Listener{
	
	private CombinationLock plugin;
	public UnlockListener(CombinationLock plugin){
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getState() instanceof Sign){
				Sign sign = (Sign)e.getClickedBlock().getState();
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(e.getPlayer().hasPermission("CombinationLock.op.unlock") || e.getPlayer().isOp()){
						if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
							sign.setLine(0, "[CombLock]");
							sign.setLine(1, "UNLOCKED");
							sign.setLine(2, "999");
							sign.setLine(3, "");
							sign.update();
							return;
						}else if(sign.getLine(1).equalsIgnoreCase("UNLOCKED")){
							sign.setLine(0, "[CombLock]");
							sign.setLine(1, "LOCKED");
							sign.setLine(2, "999");
							sign.setLine(3, "");
							sign.update();
							return;
						}
					}else if(e.getPlayer().hasPermission("CombinationLock.unlock")){
						if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
							/** Tries to unlock the lock **/
							sign.setLine(0, "");
							sign.setLine(1, "↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
							sign.setLine(2, "Please enter");
							sign.setLine(3, "your password");
							sendPacket(e.getPlayer(), getSignChange(e.getClickedBlock(), new String[] { sign.getLine(0), sign.getLine(1), sign.getLine(2), sign.getLine(3) }));
							sendSignEditor(e.getPlayer(), e.getClickedBlock());
							return;
						}else if(sign.getLine(1).equalsIgnoreCase("UNLOCKED")){
							/** Locks the lock **/
							sign.setLine(0, "[CombLock]");
							sign.setLine(1, "LOCKED");
							sign.setLine(2, "999");
							sign.setLine(3, "");
							sign.update();
							return;
						}
					}
				}
			}else if(e.getClickedBlock().getState() instanceof Chest || e.getClickedBlock().getState() instanceof DoubleChest){
				/** Grands access only to the unlocked chests. **/
				if(isDoubleChestLocked(e.getClickedBlock())){
					e.getPlayer().sendMessage(plugin.getMessage("LOCK-ISLOCKED").replaceAll("%locked_item%", e.getClickedBlock().getType().toString().toLowerCase()));
					e.setCancelled(true);
					return;
				}else{
					if(isLocked(e.getClickedBlock())){
						e.getPlayer().sendMessage(plugin.getMessage("LOCK-ISLOCKED").replaceAll("%locked_item%", e.getClickedBlock().getType().toString().toLowerCase()));
						e.setCancelled(true);
						return;
					}
				}
			}else if( e.getClickedBlock().getState() instanceof Furnace){
				/** Grands access only to unlocked items. **/
				if(isLocked(e.getClickedBlock())){
					e.getPlayer().sendMessage(plugin.getMessage("LOCK-ISLOCKED").replaceAll("%locked_item%", e.getClickedBlock().getType().toString().toLowerCase()));
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	private Boolean isLocked(Block block){
		Sign sign;
		org.bukkit.material.Sign sign2;
		if (block.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN){
			sign = (Sign) block.getRelative(BlockFace.NORTH).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.NORTH).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.SOUTH){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
						return true;
					}
				}
			}
		}
		if(block.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN){
			sign = (Sign) block.getRelative(BlockFace.EAST).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.EAST).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.WEST){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
						return true;
					}
				}
			}
		}
		if(block.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN){
			sign = (Sign) block.getRelative(BlockFace.SOUTH).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.SOUTH).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.NORTH){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
						return true;
					}
				}
			}
		}
		if(block.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN){
			sign = (Sign) block.getRelative(BlockFace.WEST).getState();
			sign2 = (org.bukkit.material.Sign) block.getRelative(BlockFace.WEST).getState().getData();
			if(sign2.getAttachedFace() == BlockFace.EAST){
				if(sign.getLine(0).equalsIgnoreCase("[CombLock]")){
					if(sign.getLine(1).equalsIgnoreCase("LOCKED")){
						return true;
					}
				}
			}
		}
		return false;
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
	
	private void sendSignEditor(Player p, Block sign){
	    TileEntitySign tile = (TileEntitySign)((CraftWorld)sign.getWorld()).getHandle().getTileEntity(sign.getX(), sign.getY(), sign.getZ());
	    tile.isEditable = true;
	    tile.a(getNMS(p));
	    sendPacket(p, new PacketPlayOutOpenSignEditor(sign.getX(), sign.getY(), sign.getZ()));
	}

	private PacketPlayOutUpdateSign getSignChange(Block sign, String[] lines) {
	    return new PacketPlayOutUpdateSign(sign.getX(), sign.getY(), sign.getZ(), lines);
	}

	private EntityPlayer getNMS(Player p) {
	    return ((CraftPlayer)p).getHandle();
	}

	private void sendPacket(Player p, Packet a) {
	    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(a);
	}
}
