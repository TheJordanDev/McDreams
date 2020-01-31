package fr.theskinter.mcdreams.guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.GUIInteractEvent;
import fr.theskinter.mcdreams.objects.managers.RegionManager;
import fr.theskinter.mcdreams.objects.managers.RegionManager.Region;
import fr.theskinter.mcdreams.utils.creators.GUICreator;
import fr.theskinter.mcdreams.utils.creators.ItemCreator;
import fr.theskinter.mcdreams.utils.filter.PlayerSorter;
import fr.theskinter.mcdreams.utils.filter.RegionSorter;
import lombok.Getter;

public class GUI_Regions {

	@Getter private Region_List_Menu regionListMenu;
	
	@Getter private Map<UUID,Integer> regionListPageIndex = new HashMap<>();
	@Getter private Map<UUID,UUID> lastRegionSelected = new HashMap<>();
	
	public GUI_Regions(JavaPlugin plugin) {
		this.regionListMenu = new Region_List_Menu(); plugin.getServer().getPluginManager().registerEvents(regionListMenu, plugin);
	}
	
	public class Region_List_Menu extends GUICreator {
		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		private ItemStack regionItem(Region region) {
			if (region == null) { return new ItemCreator().setName("§c§lERREUR").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)14).build(); }
			ItemCreator creator = new ItemCreator();
			creator.setName("§3§l"+region.getName());
			if (region.getZones().isEmpty()) {
				creator.setLore(Arrays.asList("§8§lID : §7§l"+region.getId().toString(),"§8§lCuboids : §c§lAucunes"));	
			} else {
				creator.setLore(Arrays.asList("§8§lID : §7§l"+region.getId().toString(),"§8§lCuboids : §7§l"+region.getZones().size()));
			}
			creator.setMaterial(Material.STRUCTURE_VOID);
			return creator.build();
		}
		
		@Override
		public Inventory build() {
			setName("§9§lRegion Liste");
			return super.build();
		}
			
		public Inventory open(UUID uuid,Integer page) {
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			List<Region> regions = new ArrayList<>();
			Iterator<? extends Region> it_players = RegionManager.instance.getRegions().iterator();
			it_players.forEachRemaining(regions::add);
			Collections.sort(regions,new RegionSorter());
			getRegionListPageIndex().put(uuid, page);
				if (!regions.isEmpty()) {
					if (page<1) {
						open(uuid,1);
					} else if (page == 1) {
						int i = 0; 
						for (int slot=9;slot<getSlots().size()-9;slot++) { 
							if (regions.size()<=i) {break;}
							setSlot(slot, regionItem(regions.get(i)));
							i++;
						}
						if (regions.size()>i) { 
							setSlot(50, next_btn);
						}
					} else if (page > 1){
						int i = ((getLine()-18)*page)-27;
						for (int slot=9;slot<getSlots().size()-9;slot++) {
							if (regions.size()<=i) { break;}
							setSlot(slot, regionItem(regions.get(i)));	
							i++;
						}
						setSlot(48, prev_btn);
						if (regions.size()>i) { 
							setSlot(50, next_btn);
						}
					}
				} else {
					setSlot(22, new ItemCreator().setName("§7§lAucune Regions !!").setMaterial(Material.PAPER).build());
				}
				return build();
			}
			
			@EventHandler
			public void onInteract(GUIInteractEvent event) {
				if (event.getGui_id().equals(getId())) {
					event.setCancelled(true);
					if (event.getEvent().getClick() == ClickType.LEFT) {
						List<Player> players = new ArrayList<Player>();
						Iterator<? extends Player> it_players = Bukkit.getOnlinePlayers().iterator();
						it_players.forEachRemaining(players::add);
						players.sort(new PlayerSorter());
						UUID uuid = event.getEvent().getWhoClicked().getUniqueId();
						Integer page = getRegionListPageIndex().get(event.getEvent().getWhoClicked().getUniqueId());
						if (event.getEvent().getCurrentItem().isSimilar(next_btn)) {
							event.getEvent().getWhoClicked().openInventory(open(uuid, page+1));
						} else if (event.getEvent().getCurrentItem().isSimilar(prev_btn)) {
							event.getEvent().getWhoClicked().openInventory(open(uuid, page-1));
						} else if (event.getEvent().getCurrentItem().getType() == Material.STRUCTURE_VOID) {
							getLastRegionSelected().put(uuid, RegionManager.instance.getRegions().get(getRegionIndex(uuid, event.getEvent().getRawSlot())).getId());
							//event.getEvent().getWhoClicked().openInventory(getPlayerInfoMenu().build(uuid,players.get(getPlayerIndex(uuid, event.getEvent().getRawSlot())).getUniqueId()));
						} else if (event.getEvent().getCurrentItem().isSimilar(exit())) {
							event.getEvent().getWhoClicked().openInventory(McDreams.instance.getMcDreamsGUI().mainMenu.build());
						}
					}
				}
			}
			
			public Integer getRegionIndex(UUID uuid,Integer slot) {
				Integer page = getRegionListPageIndex().get(uuid);
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
	
		public class Region_Info_Menu extends GUICreator {
			
			public ItemStack nameInfo(Region region) {
				return new ItemCreator().setName("Nom").setLore(Arrays.asList("§6§l"+region.getName())).setMaterial(Material.PAPER).build();
			}
			
			public ItemStack IDInfo(Region region) {
				return new ItemCreator().setName("ID").setLore(Arrays.asList("§6§l"+region.getId().toString())).setMaterial(Material.EMPTY_MAP).build();
			}
			
			public ItemStack zonesInfo(Region region) {
				return new ItemCreator().setName("Zones").setLore(Arrays.asList("§6§l"+region.getZones().size())).setMaterial(Material.HARD_CLAY).build();
			}
			
			
			public Inventory build(UUID viewer,Region region) {
				getLastRegionSelected().put(viewer, region.getId());
				setName("§3§lInfo §7§l: §4§l"+region.getName());
				setMaxLine(6);
				setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
				setSlot(0, nameInfo(region));
				setSlot(1, IDInfo(region));
				setSlot(2, zonesInfo(region));
				return super.build();
			}
			
			@EventHandler
			public void onInteract(GUIInteractEvent event) {
				if (event.getGui_id().equals(getId())) {
					Player player = (Player) event.getEvent().getWhoClicked();
					Region selectedRegion =	RegionManager.instance.getRegionByID(getLastRegionSelected().get(player.getUniqueId()));
					event.setCancelled(true);
					if (selectedRegion == null) return;
					if (event.getEvent().getClick() == ClickType.LEFT) {
						if (event.getEvent().getCurrentItem().isSimilar(nameInfo(selectedRegion))) {
							
						}
					}
				}
			}
			
		}

}
