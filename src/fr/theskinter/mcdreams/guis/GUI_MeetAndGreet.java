package fr.theskinter.mcdreams.guis;

import java.util.ArrayList;
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
import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.GUIInteractEvent;
import fr.theskinter.mcdreams.utils.Skulls.SkullUtils;
import fr.theskinter.mcdreams.utils.creators.GUICreator;
import fr.theskinter.mcdreams.utils.creators.ItemCreator;
import fr.theskinter.mcdreams.utils.filter.PlayerSorter;
import lombok.Getter;

public class GUI_MeetAndGreet {
	
	@Getter public final MeetAndGreet_Main_Menu mainMenu;
	@Getter public final MeetAndGreet_Player_List_Menu playersMenu;
	@Getter public final Map<UUID,Integer> playerListPageBackup = new HashMap<>();
	
	public GUI_MeetAndGreet(McDreams instance) {
		this.mainMenu = new MeetAndGreet_Main_Menu(); instance.getServer().getPluginManager().registerEvents(mainMenu, instance);
		this.playersMenu = new MeetAndGreet_Player_List_Menu(); instance.getServer().getPluginManager().registerEvents(playersMenu, instance);
	}
	
	public class MeetAndGreet_Main_Menu extends GUICreator {
		
		private ItemStack playersBtn() {
			ItemCreator creator = new ItemCreator();
			creator.setName("§6§lJOUEURS");
			creator.setMaterial(Material.SKULL_ITEM);
			creator.setByte((byte)3);
			return creator.build();
		}
		
		private ItemStack autographeBtn() {
			ItemCreator creator = new ItemCreator();
			creator.setName("§6§lAUTOGRAPHES");
			creator.setMaterial(Material.BOOK_AND_QUILL);
			return creator.build();
		}
		
		public MeetAndGreet_Main_Menu() {
			setName("§a§lMeetsAndGreets");
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
		}
		
		public Inventory build() {
			setSlot(1, playersBtn());
			setSlot(2, autographeBtn());
			return super.build();
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				Player player = (Player)event.getEvent().getWhoClicked();
				ItemStack item = event.getEvent().getCurrentItem();
				if (item.isSimilar(playersBtn())) {
					player.openInventory(playersMenu.open(player.getUniqueId(), 1));
				} else if (item.isSimilar(autographeBtn())) {
					player.openInventory(playersMenu.open(player.getUniqueId(), 1));
				}
			}
		}
	}
	
	public class MeetAndGreet_Player_List_Menu extends GUICreator {
		
		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();

		@Override
		public Inventory build() {
			setName("§9§lJOUEUR LISTE");
			return super.build();
		}
			
		public Inventory open(UUID uuid,Integer page) {
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			List<Player> players = new ArrayList<>();
			Iterator<? extends Player> it_players = Bukkit.getOnlinePlayers().iterator();
			it_players.forEachRemaining(players::add);
			Collections.sort(players,new PlayerSorter());
			getPlayerListPageBackup().put(uuid, page);
				if (!players.isEmpty()) {
					if (page<1) {
						open(uuid,1);
					} else if (page == 1) {
						int i = 0; 
						for (int slot=9;slot<getSlots().size()-9;slot++) { 
							if (players.size()<=i) {break;}
							setSlot(slot, SkullUtils.getPlayerSkull(players.get(i)));
							i++;
						}
						if (players.size()>i) { 
							setSlot(50, next_btn);
						}
					} else if (page > 1){
						int i = ((getLine()-18)*page)-27;
						for (int slot=9;slot<getSlots().size()-9;slot++) {
							if (players.size()<=i) { break;}
							setSlot(slot, SkullUtils.getPlayerSkull(players.get(i)));	
							i++;
						}
						setSlot(48, prev_btn);
						if (players.size()>i) { 
							setSlot(50, next_btn);
						}
					}
				} else {
					setSlot(22, new ItemCreator().setName("§7§lAucun Joueur !!").setMaterial(Material.PAPER).build());
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
						Integer page = getPlayerListPageBackup().get(event.getEvent().getWhoClicked().getUniqueId());
						if (event.getEvent().getCurrentItem().isSimilar(next_btn)) {
							event.getEvent().getWhoClicked().openInventory(open(uuid, page+1));
						} else if (event.getEvent().getCurrentItem().isSimilar(prev_btn)) {
							event.getEvent().getWhoClicked().openInventory(open(uuid, page-1));
						} else if (event.getEvent().getCurrentItem().getType() == Material.SKULL_ITEM) {
							event.getEvent().getWhoClicked().openInventory(McDreams.instance.getSkins().getSkinChanger_Menu().open(uuid,players.get(getPlayerIndex(uuid, event.getEvent().getRawSlot())).getUniqueId(),1));
						}
					}
				}
			}
			
			public Integer getPlayerIndex(UUID uuid,Integer slot) {
				Integer page = getPlayerListPageBackup().get(uuid);
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
	
}
