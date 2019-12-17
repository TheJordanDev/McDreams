package fr.theskinter.mcdreams.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.theskinter.mcdreams.utils.joueurs.Joueur;
import lombok.Getter;

public class JoueurFoodChange extends Event implements Cancellable {

	private FoodLevelChangeEvent event;
	@Getter private Joueur joueur;
	private static HandlerList handlers = new HandlerList();
	
	public JoueurFoodChange(FoodLevelChangeEvent event) {
		this.joueur = Joueur.getJoueur((Player) event.getEntity());
		this.event = event;
	}
	
	@Override
	public boolean isCancelled() {
		return event.isCancelled();
	}

	@Override
	public void setCancelled(boolean bool) {
		event.setCancelled(bool);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	
}
