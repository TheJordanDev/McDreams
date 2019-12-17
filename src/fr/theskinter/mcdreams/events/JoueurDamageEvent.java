package fr.theskinter.mcdreams.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.theskinter.mcdreams.utils.joueurs.Joueur;
import lombok.Getter;

public class JoueurDamageEvent extends Event implements Cancellable {

	private EntityDamageEvent event;
	@Getter private Joueur joueur;
	@Getter private DamageCause cause;
	private static HandlerList handlers = new HandlerList();
	
	public JoueurDamageEvent(EntityDamageEvent event) {
		this.joueur = Joueur.getJoueur((Player) event.getEntity());
		this.cause = event.getCause();
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
