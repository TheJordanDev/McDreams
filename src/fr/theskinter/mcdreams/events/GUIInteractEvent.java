package fr.theskinter.mcdreams.events;

import java.util.UUID;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

import lombok.Getter;

public class GUIInteractEvent extends Event implements Cancellable {

	public boolean cancelled = false;
	
	private static final HandlerList handlers = new HandlerList();
	@Getter private InventoryClickEvent event;
	@Getter UUID gui_id;
	
	public GUIInteractEvent(InventoryClickEvent event,UUID guiID) {
		this.event = event;
		this.gui_id = guiID;
	}
	
	@Override
	public void setCancelled(boolean bol) {
		event.setCancelled(bol);
	}
	
	@Override
	public boolean isCancelled() {
		return event.isCancelled();
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
