package fr.theskinter.mcdreams.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.theskinter.mcdreams.objects.managers.RegionManager.Region;
import lombok.Getter;

public class RegionLeaveEvent extends Event implements Cancellable{

	@Getter private Player player;
	@Getter private Region region;
	@Getter private PlayerMoveEvent event;
	private static HandlerList handlers = new HandlerList();
	
	public RegionLeaveEvent(PlayerMoveEvent event,Region regions) {
		this.player = event.getPlayer();
		this.region = regions;
		this.event = event;
	}

	@Override
	public boolean isCancelled() {
		return event.isCancelled();
	}

	@Override
	public void setCancelled(boolean arg0) {
		event.setCancelled(arg0);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
