package fr.theskinter.mcdreams.utils.creators;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.GUIInteractEvent;
import lombok.Getter;

public class GUICreator implements Listener {

	@Getter
	private String name = "";
	@Getter
	private Integer line = 9;
	@Getter
	private Map<Integer,ItemStack> slots = new HashMap<Integer,ItemStack>();
	@Getter
	private UUID id = UUID.randomUUID();
	private Inventory inventory;
	public Inventory getInventory() { if (inventory == null) {build();} return inventory;}
	
	public GUICreator setName(String name) {
		this.name = name;
		return this;
	}
	
	public GUICreator setMaxLine(Integer slot) {
		this.line = slot*9;
		return this;
	}
	
	
	public GUICreator setSlot(Integer slot, ItemStack stack) {
		this.slots.put(slot, stack);
		return this;
	}
	
	public GUICreator setBackGround(ItemStack stack) {
		for (int i=0;i<line;i++) {
			slots.put(i, stack);
		}
		return this;
	}
	
	public Inventory build() {
		Inventory inv = Bukkit.createInventory(null, this.line, this.name);
		for (int i=0;i<this.line;i++) {
			if (slots.containsKey(i)) {
				inv.setItem(i, slots.get(i));
			}
		}
		inventory = inv;
		return inventory;
	}
	
	public void register(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onInteract(InventoryClickEvent event) {
		if (event.getSlotType() == SlotType.OUTSIDE) return;
		if (event.getClickedInventory().getType() != InventoryType.CHEST) return;
		if (event.getClickedInventory().getTitle() == null || !event.getClickedInventory().getTitle().equalsIgnoreCase(getName())) return;
		McDreams.instance.getServer().getPluginManager().callEvent(new GUIInteractEvent(event, id));
	}
	
}
