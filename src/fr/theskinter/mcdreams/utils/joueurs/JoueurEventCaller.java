package fr.theskinter.mcdreams.utils.joueurs;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.JoueurDamageEvent;
import fr.theskinter.mcdreams.events.JoueurFoodChange;
import fr.theskinter.mcdreams.events.RegionEnterEvent;
import fr.theskinter.mcdreams.events.RegionLeaveEvent;
import fr.theskinter.mcdreams.objects.RegionManager;
import fr.theskinter.mcdreams.objects.RegionManager.Region;
import net.citizensnpcs.api.CitizensAPI;

public class JoueurEventCaller implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (McDreams.isEnabled("Citizens")) {
				if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) return;
			}
			McDreams.instance.getServer().getPluginManager().callEvent(new JoueurDamageEvent(event));
		}
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getFoodLevel()<((Player)event.getEntity()).getFoodLevel()) {
				McDreams.instance.getServer().getPluginManager().callEvent(new JoueurFoodChange(event));
			}
		}
	}	
	
	@EventHandler
	public void onRegionInteract(PlayerMoveEvent event) {
		Location from = event.getFrom().getBlock().getLocation();
		Location to = event.getTo().getBlock().getLocation();
		if (RegionManager.instance.getRegions().isEmpty()) return;
		for (Region region : RegionManager.instance.getRegions()) {
			if (region.isIn(from) && !region.isIn(to)) {
				McDreams.instance.getServer().getPluginManager().callEvent(new RegionLeaveEvent(event, region)); } 
			else if (region.isIn(to) && !region.isIn(from)) {
				McDreams.instance.getServer().getPluginManager().callEvent(new RegionEnterEvent(event, region)); }
		}
	}
	
}
