package fr.theskinter.mcdreams.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class Autopia {

	@Getter public Map<UUID,List<Integer>> showedPointsID = new HashMap<>();
	@Getter public List<Integer> carts = new ArrayList<Integer>();
	@Getter public List<AutopiaStepPoint> stepPoints = new ArrayList<AutopiaStepPoint>();

	public Autopia() {}
	
	public void togglePointsViewToPlayer(Player... players) {
		for (Player player : players) {
			if (getShowedPointsID().containsKey(player.getUniqueId())) {
				hidePointsToPlayer(player);
			} else {
				showPointsToPlayer(player);
			}
		}
	}
	
	public void showPointsToPlayer(Player... players) {
		for (Player player : players) {
			if (!getShowedPointsID().containsKey(player.getUniqueId())) {
				getShowedPointsID().put(player.getUniqueId(),new ArrayList<Integer>());
				PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
				for (AutopiaStepPoint point : getStepPoints()) {
					Location pointLoc = point.getLocation();
					EntityArmorStand stand = new EntityArmorStand(((CraftWorld) point.getLocation().getWorld()).getHandle());
					stand.setArms(false);
					stand.setBasePlate(true);
					stand.setLocation(pointLoc.getX()+0.5D, pointLoc.getY(), pointLoc.getZ()+0.5D, 0, 0);
					stand.setFlag(6, true);//GLOW
					stand.setFlag(5, true);//INVI
					PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(stand);
					connection.sendPacket(spawnPacket);
					getShowedPointsID().get(player.getUniqueId()).add(stand.getId());
				}
			}
		}
	}
	
	public void hidePointsToPlayer(Player... players) {
		for (Player player : players) {
			if (getShowedPointsID().containsKey(player.getUniqueId())) {
				PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
				for (Integer id : getShowedPointsID().get(player.getUniqueId())) {
					PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(id);
					connection.sendPacket(destroyPacket);
				}
				getShowedPointsID().remove(player.getUniqueId());
			}
		}
	}
	
	public void addPoint(AutopiaStepPoint point) { getStepPoints().add(point); }
	public void addNewPoint(Location location,StepPointOrientation orientation) { getStepPoints().add(new AutopiaStepPoint(location, orientation)); }
	public AutopiaStepPoint createStepPoint(Location location,StepPointOrientation orientation) { AutopiaStepPoint point = new AutopiaStepPoint(location,orientation); return point; }
	
	public class AutopiaStepPoint {
		@Getter public Location location; @Getter public StepPointOrientation orientation;
		public AutopiaStepPoint(Location location,StepPointOrientation orientation) {
			this.location = location;
			this.orientation = orientation;
		}
	}
	public enum StepPointOrientation { X,Z; }
}
