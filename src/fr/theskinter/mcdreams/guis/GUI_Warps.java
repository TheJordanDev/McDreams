package fr.theskinter.mcdreams.guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.theskinter.mcdreams.events.GUIInteractEvent;
import fr.theskinter.mcdreams.objects.Items;
import fr.theskinter.mcdreams.objects.parc.Attraction;
import fr.theskinter.mcdreams.objects.parc.Land;
import fr.theskinter.mcdreams.objects.parc.ParcManager;
import fr.theskinter.mcdreams.utils.creators.GUICreator;
import fr.theskinter.mcdreams.utils.creators.ItemCreator;

import lombok.Getter;

public class GUI_Warps {

	@Getter public static GUI_Warps instance;
	
	@Getter private Warps_Land_Select_GUI warpLandSelectGUI;
	@Getter private Warps_Land_Attraction_Select_GUI warpLandAttractionSelectGUI;

	@Getter private Map<UUID,Integer> playerLandPageBackup = new HashMap<UUID,Integer>();
	@Getter private Map<UUID,String> playerSelectedLand = new HashMap<UUID,String>();
	@Getter private Map<UUID,Integer> playerAttractionPageBackup = new HashMap<UUID,Integer>();
	
	public GUI_Warps(JavaPlugin plugin) {
		instance = this;
		this.warpLandSelectGUI = new Warps_Land_Select_GUI(); plugin.getServer().getPluginManager().registerEvents(warpLandSelectGUI, plugin);
		this.warpLandAttractionSelectGUI = new Warps_Land_Attraction_Select_GUI(); plugin.getServer().getPluginManager().registerEvents(warpLandAttractionSelectGUI, plugin);
	}
	
	public class Warps_Land_Select_GUI extends GUICreator {

		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		public ItemStack landItem(Land land) {
			ItemCreator creator = new ItemCreator().setName("§3§l"+land.getName());
			if (land.hasAttractionWichCanBeWarped()) { creator.setMaterial(Material.EYE_OF_ENDER); }
			else { creator.setMaterial(Material.ENDER_PEARL); }
			creator.setLore(Arrays.asList("§6§lAttractions §7§l: §9§l"+land.getAttractions().size()));
			return creator.build();
		}
		
		public Inventory open(UUID uuid,Integer page) {
			setName("§9§lWarps §7§l| §3§lLand Selecteur");
			setBackGround(new ItemCreator().setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).setName(" ").build());
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			List<Land> lands = new ArrayList<>();
			Iterator<? extends Land> it_lands = ParcManager.instance.getLands().iterator();
			it_lands.forEachRemaining(lands::add);
			Collections.sort(lands,Land.sorter);
			getPlayerLandPageBackup().put(uuid, page);
				if (!lands.isEmpty()) {
					if (page<1) {
						open(uuid,1);
					} else if (page == 1) {
						int i = 0; 
						for (int slot=9;slot<getSlots().size()-9;slot++) { 
							if (lands.size()<=i) {break;}
							setSlot(slot, landItem(lands.get(i)));
							i++;
						}
						if (lands.size()>i) { 
							setSlot(50, next_btn);
						}
					} else if (page > 1){
						int i = ((getLine()-18)*page)-27;
						for (int slot=9;slot<getSlots().size()-9;slot++) {
							if (lands.size()<=i) { break;}
							setSlot(slot, landItem(lands.get(i)));	
							i++;
						}
						setSlot(48, prev_btn);
						if (lands.size()>i) { 
							setSlot(50, next_btn);
						}
					}
				} else {
					setSlot(22, new ItemCreator().setName("§7§lAucun Land !!").setMaterial(Material.PAPER).build());
				}
				return build();
			}
		
		@EventHandler
		public void onGUIInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				ItemStack item = event.getEvent().getCurrentItem();
				if (item == null) return;
				UUID uuid = event.getEvent().getWhoClicked().getUniqueId();
				List<Land> lands = new ArrayList<>();
				Iterator<? extends Land> it_lands = ParcManager.instance.getLands().iterator();
				it_lands.forEachRemaining(lands::add);
				Collections.sort(lands,Land.sorter);
				if (item.isSimilar(exit())) {
					event.getEvent().getWhoClicked().closeInventory();
				} else if (item.getType() == Material.EYE_OF_ENDER) {
					getPlayerSelectedLand().put(uuid, lands.get(getLandIndex(uuid, event.getEvent().getRawSlot())).getName());
					event.getEvent().getWhoClicked().openInventory(getWarpLandAttractionSelectGUI().open(uuid, 1));
				}
			}
		}
		
		public Integer getLandIndex(UUID uuid,Integer slot) {
			if (!getPlayerLandPageBackup().containsKey(uuid)) return 0;
			Integer page = getPlayerLandPageBackup().get(uuid);
			if (page==1) {
				int i = 0; 
				for (int slota=9;slota<getSlots().size()-9;slota++) { 
					if (slota == slot) {
						return i;
					}
					i++;
				}
			} else {
				int i = ((getLine()-18)*page)-27;
				for (int slota=9;slota<getSlots().size()-9;slota++) {
					if (slota == slot) {
						return i;
					}
					i++;
				}
			}
			return 0;
		}
		
	}
	
	public class Warps_Land_Attraction_Select_GUI extends GUICreator {
		
		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		public ItemStack attractionItem(Attraction attraction) {
			ItemCreator creator = new ItemCreator().setName("§3§l"+attraction.getName());
			if (attraction.canBeWarpedAt()) { creator.setMaterial(Material.EYE_OF_ENDER); }
			else { creator.setMaterial(Material.ENDER_PEARL); }
			return creator.build();
		}
		
		public Inventory open(UUID uuid,Integer page) {
			setName("§9§lWarps §7§l| §3§lAttraction Selecteur");
			setBackGround(new ItemCreator().setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).setName(" ").build());
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			if (!getPlayerSelectedLand().containsKey(uuid)) return super.build();
			if (!ParcManager.instance.hasLand(getPlayerSelectedLand().get(uuid))) return super.build();
			Land land = ParcManager.instance.getLandByName(getPlayerSelectedLand().get(uuid));
			List<Attraction> attraction = new ArrayList<>();
			Iterator<? extends Attraction> it_attraction = land.getAttractions().iterator();
			it_attraction.forEachRemaining(attraction::add);
			Collections.sort(attraction,Attraction.sorter);
			getPlayerAttractionPageBackup().put(uuid, page);
				if (!attraction.isEmpty()) {
					if (page<1) {
						open(uuid,1);
					} else if (page == 1) {
						int i = 0; 
						for (int slot=9;slot<getSlots().size()-9;slot++) { 
							if (attraction.size()<=i) {break;}
							setSlot(slot, attractionItem(attraction.get(i)));
							i++;
						}
						if (attraction.size()>i) { 
							setSlot(50, next_btn);
						}
					} else if (page > 1){
						int i = ((getLine()-18)*page)-27;
						for (int slot=9;slot<getSlots().size()-9;slot++) {
							if (attraction.size()<=i) { break;}
							setSlot(slot, attractionItem(attraction.get(i)));	
							i++;
						}
						setSlot(48, prev_btn);
						if (attraction.size()>i) { 
							setSlot(50, next_btn);
						}
					}
				} else {
					setSlot(22, new ItemCreator().setName("§7§lAucun Joueur !!").setMaterial(Material.PAPER).build());
				}
				return build();
			}
		
		@EventHandler
		public void onGUIInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				Player clicker = (Player) event.getEvent().getWhoClicked();
				if (getPlayerSelectedLand().containsKey(clicker.getUniqueId())) {
					List<Attraction> attractions = new ArrayList<>();
					Iterator<? extends Attraction> it_attraction = ParcManager.instance.getLandByName(getPlayerSelectedLand().get(clicker.getUniqueId())).getAttractions().iterator();
					it_attraction.forEachRemaining(attractions::add);
					Collections.sort(attractions,Attraction.sorter);
					ItemStack item = event.getEvent().getCurrentItem();
					if (item == null) return;
					if (item.isSimilar(exit())) {
						event.getEvent().getWhoClicked().closeInventory();
					} else if (item.getType() == Material.EYE_OF_ENDER) {
						Attraction attraction = attractions.get(getAttractionIndex(clicker.getUniqueId(), event.getEvent().getRawSlot()));
						if (attraction.canBeWarpedAt()) {
							Land selLand = ParcManager.instance.getLandByName(getPlayerSelectedLand().get(clicker.getUniqueId()));
							clicker.getInventory().setItemInOffHand(Items.warpCreator(selLand,attraction));
							clicker.closeInventory();
						}
					}
				}
			}
		}
		
		public Integer getAttractionIndex(UUID uuid,Integer slot) {
			if (!getPlayerAttractionPageBackup().containsKey(uuid)) return 0;
			Integer page = getPlayerAttractionPageBackup().get(uuid);
			if (page==1) {
				int i = 0; 
				for (int slota=9;slota<getSlots().size()-9;slota++) { 
					if (slota == slot) { return i; }
					i++;
				}
			} else {
				int i = ((getLine()-18)*page)-27;
				for (int slota=9;slota<getSlots().size()-9;slota++) {
					if (slota == slot) { return i; }
					i++;
				}
			}
			return 0;
		}
		
	}
	
}
