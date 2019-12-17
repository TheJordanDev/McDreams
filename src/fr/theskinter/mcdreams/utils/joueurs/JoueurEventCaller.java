package fr.theskinter.mcdreams.utils.joueurs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.JoueurDamageEvent;
import fr.theskinter.mcdreams.events.JoueurFoodChange;
import net.citizensnpcs.api.CitizensAPI;

public class JoueurEventCaller implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (McDreams.isEnabled("Citizens")) {
				if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) return;
			}
			Bukkit.getServer().getPluginManager().callEvent(new JoueurDamageEvent(event));
		}
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getFoodLevel()<((Player)event.getEntity()).getFoodLevel()) {
				Bukkit.getServer().getPluginManager().callEvent(new JoueurFoodChange(event));
			}
		}
	}	
}
