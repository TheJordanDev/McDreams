package fr.theskinter.mcdreams.guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableMap;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.GUIInteractEvent;
import fr.theskinter.mcdreams.objects.parc.Attraction;
import fr.theskinter.mcdreams.objects.parc.Attraction.STATE;
import fr.theskinter.mcdreams.objects.parc.Land;
import fr.theskinter.mcdreams.objects.parc.Parc;
import fr.theskinter.mcdreams.utils.creators.GUICreator;
import fr.theskinter.mcdreams.utils.creators.ItemCreator;
import lombok.Getter;

public class GUI_Parc {

	public static GUI_Parc instance;
	
	@Getter private Map<UUID,Integer> playerLandPageBackup = new HashMap<UUID,Integer>();
	@Getter private Map<UUID,String> playerSelectedLand = new HashMap<UUID,String>();
	@Getter private Map<UUID,Integer> playerAttractionPageBackup = new HashMap<UUID,Integer>();
	@Getter private Map<UUID,Map<String,String>> playerSelectedAttraction = new HashMap<UUID,Map<String,String>>();
	
	@Getter private Parc_Land_Select_GUI parcLandSelectGUI;
	@Getter private Parc_Land_Attraction_Select_GUI parcLandAttraSelectGUI;
	@Getter private Parc_Land_Attraction_Info_GUI parcLandAttraInfoGUI;
	
	public GUI_Parc(JavaPlugin plugin) {
		instance = this;
		this.parcLandSelectGUI = new Parc_Land_Select_GUI(); plugin.getServer().getPluginManager().registerEvents(parcLandSelectGUI, plugin);
		this.parcLandAttraSelectGUI = new Parc_Land_Attraction_Select_GUI(); plugin.getServer().getPluginManager().registerEvents(parcLandAttraSelectGUI, plugin);
		this.parcLandAttraInfoGUI = new Parc_Land_Attraction_Info_GUI(); plugin.getServer().getPluginManager().registerEvents(parcLandAttraInfoGUI, plugin);
		
	}
	
	public class Parc_Land_Select_GUI extends GUICreator {

		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		public ItemStack landItem(Land land) {
			ItemCreator creator = new ItemCreator().setName("§3§l"+land.getName());
			creator.setMaterial(Material.FIREWORK_CHARGE);
			creator.hideFlags(true);
			creator.setLore(Arrays.asList(
					"§6§lAttractions §7§l: §9§l"+land.getAttractions().size(),
					"§6§lEtat : "+land.getState().getCouleur()+land.getState().toString()
			));
			return creator.build();
		}
		
		public Inventory open(UUID uuid,Integer page) {
			setName("§9§lPARC §7§l| §3§lLands");
			setBackGround(new ItemCreator().setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).setName(" ").build());
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			List<Land> lands = new ArrayList<>();
			Iterator<? extends Land> it_lands = Parc.instance.getLands().iterator();
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
				Iterator<? extends Land> it_lands = Parc.instance.getLands().iterator();
				it_lands.forEachRemaining(lands::add);
				Collections.sort(lands,Land.sorter);
				if (item.isSimilar(exit())) {
					event.getEvent().getWhoClicked().closeInventory();
				} else if (item.getType() == Material.FIREWORK_CHARGE) {
					getPlayerSelectedLand().put(uuid, lands.get(getLandIndex(uuid, event.getEvent().getRawSlot())).getName());
					event.getEvent().getWhoClicked().openInventory(getParcLandAttraSelectGUI().open(uuid, 1));
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
	
	public class Parc_Land_Attraction_Select_GUI extends GUICreator {
		
		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		public ItemStack attractionItem(Attraction attraction) {
			ItemCreator creator = new ItemCreator().setName("§3§l"+attraction.getName());
			creator.setLore(Arrays.asList(
					"§6§lEtat : "+attraction.getState().getCouleur()+attraction.getState().toString()
			));
			creator.setMaterial(Material.EMERALD);
			return creator.build();
		}
		
		public Inventory open(UUID uuid,Integer page) {
			setName("§9§lPARC §7§l| §3§lAttractions");
			setBackGround(new ItemCreator().setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).setName(" ").build());
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			if (!getPlayerSelectedLand().containsKey(uuid)) return super.build();
			if (!Parc.instance.hasLand(getPlayerSelectedLand().get(uuid))) return super.build();
			Land land = Parc.instance.getLandByName(getPlayerSelectedLand().get(uuid));
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
					if (!clicker.hasPermission(McDreams.Perms.acces_staff_menu.p)) return;
					if (!clicker.hasPermission(McDreams.Perms.edit_parc_infos.p)) return;
					List<Attraction> attractions = new ArrayList<>();
					Iterator<? extends Attraction> it_attraction = Parc.instance.getLandByName(getPlayerSelectedLand().get(clicker.getUniqueId())).getAttractions().iterator();
					it_attraction.forEachRemaining(attractions::add);
					Collections.sort(attractions,Attraction.sorter);
					ItemStack item = event.getEvent().getCurrentItem();
					if (item == null) return;
					if (item.isSimilar(exit())) {
						clicker.openInventory(getParcLandSelectGUI().open(clicker.getUniqueId(), 1));
					} else if (item.getType() == Material.EMERALD) {
						Attraction attraction = attractions.get(getAttractionIndex(clicker.getUniqueId(), event.getEvent().getRawSlot()));
						Land selLand = Parc.instance.getLandByName(getPlayerSelectedLand().get(clicker.getUniqueId()));
						getPlayerSelectedAttraction().put(clicker.getUniqueId(),ImmutableMap.of(selLand.getName(), attraction.getName()));
						clicker.openInventory(getParcLandAttraInfoGUI().build(clicker.getUniqueId()));
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
	
	public class Parc_Land_Attraction_Info_GUI extends GUICreator {

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		public ItemStack attraState(Attraction attraction) {
			ItemCreator creator = new ItemCreator();
			Attraction.STATE state = attraction.getState();
			creator.setMaterial(Material.INK_SACK);
			creator.setName(state.getCouleur()+state.toString());
			if (state == STATE.CLOSE) { creator.setByte((byte)1); } 
			else if (state == STATE.MAINTENANCE) { creator.setByte((byte)14); }
			else if (state == STATE.OPEN) { creator.setByte((byte)10); }
			return creator.build();
		}
		
		public Inventory build(UUID pUUID) {
			Entry<String, String> entry = getPlayerSelectedAttraction().get(pUUID).entrySet().iterator().next();
			Land land = Parc.instance.getLandByName(entry.getKey());
			Attraction attraction = land.getAttractionByName(entry.getValue());
			setName("§3§lPARC §7§l- §9§l"+attraction.getName());
			setMaxLine(3);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			setSlot(1, attraState(attraction));
			return super.build();
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				Player player = (Player)event.getEvent().getWhoClicked();
				Entry<String, String> entry = getPlayerSelectedAttraction().get(player.getUniqueId()).entrySet().iterator().next();
				Land land = Parc.instance.getLandByName(entry.getKey());
				Attraction attraction = land.getAttractionByName(entry.getValue());
				ItemStack item = event.getEvent().getCurrentItem();
				if (item.isSimilar(exit())) {
					player.openInventory(getParcLandAttraSelectGUI().open(player.getUniqueId(), 1));
					return;
				} else if (item.isSimilar(attraState(attraction))) {
					attraction.setState(attraction.getState().next());
					player.openInventory(build(player.getUniqueId()));
					return;
				}
			}
		}
		
	}
	
}
