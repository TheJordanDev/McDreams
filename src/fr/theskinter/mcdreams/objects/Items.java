package fr.theskinter.mcdreams.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.EndGateway;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.util.Vector;

import fr.theskinter.mcdreams.objects.parc.Attraction;
import fr.theskinter.mcdreams.objects.parc.Land;

public class Items {

	public static ItemStack warpCreator(Land land,Attraction attraction) {
		ItemStack warpCreator = new ItemStack(Material.MONSTER_EGG);
		SpawnEggMeta meta = (SpawnEggMeta) warpCreator.getItemMeta();	
		meta.setDisplayName("§6§lGénérateur de porail pour §9§l"+attraction.getName());
		meta.setSpawnedType(EntityType.SHULKER);
		meta.setLore(Arrays.asList("§9§lClique Droit sur un bloc pour faire apparaître un portail.","§e§l"+land.getName()));
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		warpCreator.setItemMeta(meta);
		return warpCreator;
	}
	
	public static boolean isZoneObstructed(Location downLeft,Location upRight,BlockFace face) {
		if (face == BlockFace.NORTH) {
			for (int x = downLeft.getBlockX(); x<upRight.getBlockX();x++) {
				for (int y = downLeft.getBlockY();y<upRight.getBlockY();y++) {
					if (downLeft.getWorld().getBlockAt(x,y,downLeft.getBlockZ()).getType() != Material.AIR) {
						return true;
					}
				}
			}
		} else if (face == BlockFace.SOUTH) {
			for (int x = downLeft.getBlockX(); x>upRight.getBlockX();x--) {
				for (int y = downLeft.getBlockY();y<upRight.getBlockY();y++) {
					if (downLeft.getWorld().getBlockAt(x,y,downLeft.getBlockZ()).getType() != Material.AIR) {
						return true;
					}
				}
			}
		} else if (face == BlockFace.EAST ) {
			for (int z = downLeft.getBlockZ(); z<upRight.getBlockZ();z++) {
				for (int y = downLeft.getBlockY();y<upRight.getBlockY();y++) {
					if (downLeft.getWorld().getBlockAt(downLeft.getBlockX(),y,z).getType() != Material.AIR) {
						return true;
					}
				}
			}
		} else if (face == BlockFace.WEST) {
			for (int z = downLeft.getBlockZ(); z>upRight.getBlockZ();z--) {
				for (int y = downLeft.getBlockY();y<upRight.getBlockY();y++) {
					if (downLeft.getWorld().getBlockAt(downLeft.getBlockX(),y,z).getType() != Material.AIR) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static Vector getDownLeftOffset(BlockFace face) {
		if (face == BlockFace.NORTH) { return new Vector(-2, 0, 0); }
		else if (face == BlockFace.SOUTH) { return new Vector(2, 0, 0); }
		else if (face == BlockFace.WEST) { return new Vector(0, 0, 2); }
		else if (face == BlockFace.EAST) { return new Vector(0, 0, -2); }
		else { return new Vector(); }
	}
	
	public static Vector getUpRightOffset(BlockFace face) {
		if (face == BlockFace.NORTH) { return new Vector(3, 4, 0); }
		else if (face == BlockFace.SOUTH) { return new Vector(-3, 4, 0); }
		else if (face == BlockFace.WEST) { return new Vector(0, 4, -3); }
		else if (face == BlockFace.EAST) { return new Vector(0, 4, 3); }
		else { return new Vector(); }
	}
	
	public static List<Location> fillBlocks(Material blockMat,Location downLeft,Location upRight,BlockFace face) {
		List<Location> locs = new ArrayList<Location>();
		if (face == BlockFace.NORTH) {
			for (int x = downLeft.getBlockX(); x<upRight.getBlockX();x++) {
				for (int y = downLeft.getBlockY();y<upRight.getBlockY();y++) {
					downLeft.getWorld().getBlockAt(x,y,upRight.getBlockZ()).setType(blockMat);
					locs.add(new Location(upRight.getWorld(), x,y,upRight.getBlockZ()));
				}
			}
		} else if (face == BlockFace.SOUTH) {
			for (int x = downLeft.getBlockX(); x>upRight.getBlockX();x--) {
				for (int y = downLeft.getBlockY();y<upRight.getBlockY();y++) {
					downLeft.getWorld().getBlockAt(x,y,upRight.getBlockZ()).setType(blockMat);
					locs.add(new Location(upRight.getWorld(), x,y,upRight.getBlockZ()));
				}
			}
		} else if (face == BlockFace.EAST ) {
			for (int z = downLeft.getBlockZ(); z<upRight.getBlockZ();z++) {
				for (int y = downLeft.getBlockY();y<upRight.getBlockY();y++) {
					downLeft.getWorld().getBlockAt(upRight.getBlockX(),y,z).setType(blockMat);
					locs.add(new Location(upRight.getWorld(), upRight.getBlockX(),y,z));
				}
			}
		} else if (face == BlockFace.WEST) {
			for (int z = downLeft.getBlockZ(); z>upRight.getBlockZ();z--) {
				for (int y = downLeft.getBlockY();y<upRight.getBlockY();y++) {
					downLeft.getWorld().getBlockAt(upRight.getBlockX(),y,z).setType(blockMat);
					locs.add(new Location(upRight.getWorld(), upRight.getBlockX(),y,z));
				}
			}
		}
		return locs;
	}
	
	public static EndGateway setGateway(Location from,Location to) {
		World world = from.getWorld();
		from.getBlock().setType(Material.END_GATEWAY);
		EndGateway gateway = (EndGateway) world.getBlockAt(from).getState();
		gateway.setExactTeleport(true); gateway.setExitLocation(to); gateway.update();
		return gateway;
	}
	
}
