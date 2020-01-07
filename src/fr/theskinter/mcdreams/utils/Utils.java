package fr.theskinter.mcdreams.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.objects.Items;
import fr.theskinter.mcdreams.objects.parc.Attraction;

@SuppressWarnings("deprecation")
public class Utils {

	public static void broadcast(String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
		System.out.println(ChatColor.stripColor(message));
	}
	
	public static void drawLine(Player player, Location point1, Location point2, double space) {
	    World world = point1.getWorld();
	    if (!(point2.getWorld().equals(world))) return;
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double length = 0;
	    for (; length < distance; p1.add(vector)) {
	        player.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1);
	        length += space;
	    }
	}
	
	public static List<String> loopPlayers(String name) {
		ArrayList<String> returned = new ArrayList<>();
		if (!name.equals("")) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getName().toLowerCase().startsWith(name.toLowerCase())) {
					returned.add(player.getName());
				}
			}
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				returned.add(player.getName());
			}
		}
		return returned;
	}
	
	public static List<String> mentionLoopPlayers(String name) {
		ArrayList<String> returned = new ArrayList<>();
		if (!name.equals("")) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getName().toLowerCase().startsWith(name.toLowerCase())) {
					returned.add("@"+player.getName());
				}
			}
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				returned.add("@"+player.getName());
			}
		}
		return returned;
	}
	
	public static Property getPlayerTexture(Player player) {
		CraftPlayer craftPlayer = ((CraftPlayer)player);
		GameProfile profile = craftPlayer.getProfile();
    	return profile.getProperties().get("textures").iterator().next();
	}
	
	public static UUID fromString(String uuid) {
		return UUID.fromString(
			    uuid
			    .replaceFirst( 
			        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5" 
			    )
			);
	}
		
	public static void updatePlayerPVP(Player player) {
		/* 16 : 1.8 | 4 : 1.9+ */
		AttributeInstance attr = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		if (attr.getBaseValue() != 16) {
			attr.setBaseValue(16);
			player.saveData();
		}	
	}
	
	public static boolean pasteSchematic(Player player,Location from,Location to,BlockFace face,Attraction attra) {
		WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		File schematic = new File(McDreams.instance.getDataFolder(),"portal.schem");
		EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(from.getWorld()),1000);
		if (schematic.exists()) {
			try {
				com.sk89q.worldedit.Vector origin = new com.sk89q.worldedit.Vector(from.getBlockX(), from.getBlockY(), from.getBlockZ());
				CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
				clipboard.rotate2D(getAngleBasedOnBlockFace(face));
				com.sk89q.worldedit.Vector maxPoint = (new com.sk89q.worldedit.Vector(origin).add(new com.sk89q.worldedit.Vector(clipboard.getWidth()-1, clipboard.getHeight()-2, clipboard.getLength()-1))).add(getOffset(face));
				com.sk89q.worldedit.Vector minPoint = new com.sk89q.worldedit.Vector(origin).add(getOffset(face));
				if (!isRegionObstructed(minPoint, maxPoint,from.getWorld())) {
					clipboard.place(session, minPoint, false);
					setBedrockToPortal(minPoint, maxPoint,from.getWorld(),attra.getWarpLocation());
					TitleManager.sendTitle(player, " ","§a§lPortail placé avec succès !",5);
					return true;
				} else {
					TitleManager.sendTitle(player, " ","§c§lVeuillez trouver une zone libre !",5);
					return false;
				}
			} catch (MaxChangedBlocksException | DataException | IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			player.sendMessage("§c§lLe portail est introuvable, préviens un modérateur de toute urgence !");
			return false;
		}
	}
	
	public static BlockFace getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 45) { return BlockFace.WEST; }
        else if (45 <= rotation && rotation < 135) { return BlockFace.NORTH; }
        else if (135 <= rotation && rotation < 225) { return BlockFace.EAST; }
        else if (225 <= rotation && rotation < 315) { return BlockFace.SOUTH; }
        else if (315 <= rotation && rotation < 360) {return BlockFace.WEST; } 
        else { return null; }
    }
	
	public static boolean isRegionObstructed(com.sk89q.worldedit.Vector one,com.sk89q.worldedit.Vector two,World world) {
		for (int i = one.getBlockX(); i <= two.getBlockX();i++) {
			for (int j = one.getBlockY(); j <= two.getBlockY(); j++) {
				for (int k = one.getBlockZ(); k <= two.getBlockZ();k++) {
					Block block = world.getBlockAt(i,j,k);
					if (block.getType() == Material.AIR) {
						if (!world.getNearbyEntities(block.getLocation().clone().add(0.5D, 0.5D, 0.5D), 0.5, 0.5, 0.5).isEmpty()) {
							return true;
						}
						continue;
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean setBedrockToPortal(com.sk89q.worldedit.Vector one,com.sk89q.worldedit.Vector two,World world,Location toLocation) {
		for (int i = one.getBlockX(); i <= two.getBlockX();i++) {
			for (int j = one.getBlockY(); j <= two.getBlockY(); j++) {
				for (int k = one.getBlockZ(); k <= two.getBlockZ();k++) {
					Block block = world.getBlockAt(i,j,k);
					if (block.getType() == Material.BEDROCK) {
						Items.setGateway(block.getLocation(), toLocation);
						continue;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean setPortalToBedrock(com.sk89q.worldedit.Vector one,com.sk89q.worldedit.Vector two,World world) {
		for (int i = one.getBlockX(); i <= two.getBlockX();i++) {
			for (int j = one.getBlockY(); j <= two.getBlockY(); j++) {
				for (int k = one.getBlockZ(); k <= two.getBlockZ();k++) {
					Block block = world.getBlockAt(i,j,k);
					if (block.getType() == Material.END_GATEWAY) {
						block.setType(Material.BEDROCK);
						continue;
					}
				}
			}
		}
		return false;
	}
	
	public static Integer getAngleBasedOnBlockFace(BlockFace face) {
		if (face == BlockFace.NORTH) {
			return 0;
		} else if (face == BlockFace.EAST) {
			return 90;
		} else if (face == BlockFace.SOUTH) {
			return 180;
		} else if (face == BlockFace.WEST) {
			return 270;
		}
		return 0;
	}
	
	public static com.sk89q.worldedit.Vector getOffset(BlockFace face) {
		if (face == BlockFace.NORTH) { return new com.sk89q.worldedit.Vector(-3, 0, -1); }
		else if (face == BlockFace.SOUTH) { return new com.sk89q.worldedit.Vector(-3, 0, -1); }
		else if (face == BlockFace.WEST) { return new com.sk89q.worldedit.Vector(-1, 0, -3); }
		else if (face == BlockFace.EAST) { return new com.sk89q.worldedit.Vector(-1, 0, -3); }
		else { return new com.sk89q.worldedit.Vector(); }
	}
	
}
