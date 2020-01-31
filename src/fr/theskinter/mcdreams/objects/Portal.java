package fr.theskinter.mcdreams.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.objects.parc.Attraction;
import fr.theskinter.mcdreams.utils.TitleManager;
import fr.theskinter.mcdreams.utils.Utils;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("deprecation")
public class Portal extends Cuboid {

	@Getter @Setter private int timer = 30;
	@Getter @Setter private Location toLocation;
	@Getter @Setter private boolean enabled;
	@Getter @Setter private List<UUID> entityTimerID = new ArrayList<UUID>(); 
	
	public Portal(Location point1,Location point2,Location toLocation) {
		super(point1, point2);
		this.toLocation = toLocation;
	}
	
	public static boolean create(Player creator,Location from,BlockFace face,Attraction attraction) {
		WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		File schematic = new File(McDreams.instance.getDataFolder(),"portal.schem");
		EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(from.getWorld()),1000);
		if (schematic.exists()) {
			try {
				com.sk89q.worldedit.Vector origin = new com.sk89q.worldedit.Vector(from.getBlockX(), from.getBlockY(), from.getBlockZ());
				CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
				clipboard.rotate2D(Utils.getAngleBasedOnBlockFace(face));
				Vector offset = Utils.getOffset(face);
				com.sk89q.worldedit.Vector maxPoint = (new com.sk89q.worldedit.Vector(origin).add(new com.sk89q.worldedit.Vector(clipboard.getWidth()-1, clipboard.getHeight()-2, clipboard.getLength()-1))).add(offset);
				com.sk89q.worldedit.Vector minPoint = new com.sk89q.worldedit.Vector(origin).add(offset);
				if (!Utils.isRegionObstructed(minPoint, maxPoint,from.getWorld())) {
					TitleManager.sendTitle(creator, " ","§a§lPortail placé avec succès !",5);
					Location minLoc = new Location(from.getWorld(), minPoint.getBlockX(), minPoint.getY(), minPoint.getBlockZ());
					Location maxLoc = new Location(from.getWorld(), maxPoint.getBlockX(), maxPoint.getY(), maxPoint.getBlockZ());
					clipboard.place(session, minPoint, false);
					Portal portal = new Portal(
							minLoc, 
							maxLoc,
							attraction.getWarpLocation());
					List<UUID> stands = new ArrayList<UUID>();
					if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
						ArmorStand stand1 = (ArmorStand) from.getWorld().spawnEntity(minLoc.clone().add(-offset.getX()+0.5,-offset.getY()+3.5,-offset.getZ()+1.5), EntityType.ARMOR_STAND);
						stand1.setVisible(false); stand1.setCustomNameVisible(true); stand1.setInvulnerable(true); stand1.setGravity(false); stand1.setSmall(true);
						ArmorStand stand2 = (ArmorStand) from.getWorld().spawnEntity(minLoc.clone().add(-offset.getX()+0.5,-offset.getY()+3.5,-offset.getZ()-0.5), EntityType.ARMOR_STAND);
						stand2.setVisible(false); stand2.setCustomNameVisible(true); stand2.setInvulnerable(true); stand2.setGravity(false); stand2.setSmall(true);
						stands.add(stand1.getUniqueId()); stands.add(stand2.getUniqueId());
					} else if (face == BlockFace.EAST || face == BlockFace.WEST) {
						ArmorStand stand1 = (ArmorStand) from.getWorld().spawnEntity(minLoc.clone().add(-offset.getX()+1.5,-offset.getY()+3.5,-offset.getZ()+0.5), EntityType.ARMOR_STAND);
						stand1.setVisible(false); stand1.setCustomNameVisible(true); stand1.setInvulnerable(true); stand1.setGravity(false); stand1.setSmall(true);
						ArmorStand stand2 = (ArmorStand) from.getWorld().spawnEntity(minLoc.clone().add(-offset.getX()-0.5,-offset.getY()+3.5,-offset.getZ()+0.5), EntityType.ARMOR_STAND);
						stand2.setVisible(false); stand2.setCustomNameVisible(true); stand2.setInvulnerable(true); stand2.setGravity(false); stand2.setSmall(true);
						stands.add(stand1.getUniqueId()); stands.add(stand2.getUniqueId());
					}
					portal.setEntityTimerID(stands);
					portal.enable();
					attraction.getPortals().add(portal);
					return true;
				} else {
					TitleManager.sendTitle(creator, " ","§c§lVeuillez trouver une zone libre !",5);
					return false;
				}
			} catch (MaxChangedBlocksException | DataException | IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			creator.sendMessage("§c§lLe portail est introuvable, préviens un modérateur de toute urgence !");
			return false;
		}
	}
	
	public void disable() {
		for (int i = getXMin(); i <= getXMax();i++) {
			for (int j = getYMin(); j <= getYMax(); j++) {
				for (int k = getZMin(); k <= getZMax();k++) {
					Block block = world.getBlockAt(i,j,k);
					if (block.getType() == Material.END_GATEWAY) {
						block.setType(Material.BEDROCK);
						continue;
					}
				}
			}
		}
		enabled = false;
	}
	
	public void enable() {
		for (int i = getXMin(); i <= getXMax();i++) {
			for (int j = getYMin(); j <= getYMax(); j++) {
				for (int k = getZMin(); k <= getZMax();k++) {
					Block block = world.getBlockAt(i,j,k);
					if (block.getType() == Material.BEDROCK) {
						Items.setGateway(block.getLocation(), getToLocation());
						continue;
					}
				}
			}
		}
		enabled = true;
	}
	
	public void clear() {
		Block d = corners()[0];
		Block u= corners()[7];
		for (int x = d.getX(); x <= u.getX();x++) {
			for (int y = d.getY(); y<=u.getY(); y++) {
				for (int z = d.getZ(); z<=u.getZ(); z++) {
					getWorld().getBlockAt(x, y, z).setType(Material.AIR);
					((CraftChunk)getWorld().getBlockAt(x, y, z).getChunk()).getHandle().initLighting();
					continue;
				}
			}
		}
		for (UUID entityUUID : entityTimerID) {
			Bukkit.getEntity(entityUUID).remove();
		}
	}
	
	public void updateLocation(Location newLoc) {
		setToLocation(newLoc);
		disable();
		enable();
	}
	
}
