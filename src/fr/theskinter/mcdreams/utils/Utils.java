package fr.theskinter.mcdreams.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

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
		GameProfile profile = ((CraftPlayer)player).getProfile();
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
	
}
