package fr.theskinter.mcdreams.guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.GUIInteractEvent;
import fr.theskinter.mcdreams.objects.Joueur;
import fr.theskinter.mcdreams.utils.JoueurTempStates;
import fr.theskinter.mcdreams.utils.Skins;
import fr.theskinter.mcdreams.utils.Skins.SkinsENUM;
import fr.theskinter.mcdreams.utils.Skulls.SkullUtils;
import fr.theskinter.mcdreams.utils.creators.GUICreator;
import fr.theskinter.mcdreams.utils.creators.ItemCreator;
import fr.theskinter.mcdreams.utils.joueurs.JoueurManager;
import lombok.Getter;

public class GUI_McDreams {
	//✔✖
	@Getter public final McDreams_Main_Menu mainMenu;
	@Getter public final McDreams_Server_Settings_Menu serverMenu;
	@Getter public final McDreams_Players_Settings_Menu playersMenu;
	@Getter public final McDreams_Players_Infos_Menu playerInfoMenu;
	@Getter public final McDreams_Money_Menu playerMoneyMenu;
	
	@Getter public final Map<UUID,Integer> playerListPageBackup = new HashMap<>();
	@Getter public final Map<UUID,UUID> playerSelectedPlayerBackup = new HashMap<>();
	
	public GUI_McDreams(McDreams plugin) {
		this.mainMenu = new McDreams_Main_Menu(); plugin.getServer().getPluginManager().registerEvents(mainMenu, plugin);
		this.serverMenu = new McDreams_Server_Settings_Menu(); plugin.getServer().getPluginManager().registerEvents(serverMenu, plugin);
		this.playersMenu = new McDreams_Players_Settings_Menu(); plugin.getServer().getPluginManager().registerEvents(playersMenu, plugin);
		this.playerInfoMenu = new McDreams_Players_Infos_Menu(); plugin.getServer().getPluginManager().registerEvents(playerInfoMenu, plugin);
		this.playerMoneyMenu = new McDreams_Money_Menu(); plugin.getServer().getPluginManager().registerEvents(playerMoneyMenu, plugin);
	}
	
	public class McDreams_Main_Menu extends GUICreator {

		private ItemStack emptyBtn(String name) {
			ItemCreator creator = new ItemCreator()
					.setName("§c§l"+name)
					.setMaterial(Material.STAINED_GLASS_PANE)
					.setByte((byte)14);
			return creator.build();
		}
		
		private ItemStack settingsBtn() {
			ItemCreator creator = new ItemCreator()
					.setMaterial(Material.COMMAND)
					.setName("§6§lSERVEUR");
			return creator.build();
		}
		
		private ItemStack npcBtn() {
			Integer random = new Random().nextInt(SkinsENUM.values().length);
			SkinsENUM skin = SkinsENUM.values()[random];
			ItemStack item = SkullUtils.getCustomSkullFromTexture(skin.getTEXTURE());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§a§l✔CITIZENS✔");
			item.setItemMeta(meta);
			return item;
		}
		
		private ItemStack playersBtn() {
			ItemCreator creator = new ItemCreator();
			creator.setName("§6§lJOUEURS");
			creator.setMaterial(Material.SKULL_ITEM);
			creator.setByte((byte)3);
			return creator.build();
		}
		
		private ItemStack regionsBtn() {
			ItemCreator creator = new ItemCreator();
			creator.setName("§3§lREGIONS");
			creator.setMaterial(Material.STRUCTURE_VOID);
			return creator.build();
		}
		
		public McDreams_Main_Menu() {
			setName("§3§lMc§6§lDreams");
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
		}
		
		public Inventory build() {
			if (McDreams.isEnabled("Citizens")) {
				setSlot(1, npcBtn());
			} else {
				setSlot(1, emptyBtn("✖CITIZENS✖"));
			}
			setSlot(2, settingsBtn());
			setSlot(3, playersBtn());
			setSlot(4, regionsBtn());
			return super.build();
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				Player player = (Player)event.getEvent().getWhoClicked();
				ItemStack item = event.getEvent().getCurrentItem();
				if (item.isSimilar(npcBtn())) {
					player.openInventory(McDreams.instance.getCitizensGUI().getList().open(player.getUniqueId(), 1));
				} else if (item.isSimilar(settingsBtn())) {
					player.openInventory(serverMenu.build());
				} else if (item.isSimilar(playersBtn())) {
					player.openInventory(playersMenu.open(player.getUniqueId(), 1));
				} else if (item.isSimilar(regionsBtn())) {
					player.openInventory(McDreams.instance.getRegionGUI().getRegionListMenu().open(player.getUniqueId(), 1));
				}
			}
		}
		
	}
	
	public class McDreams_Server_Settings_Menu extends GUICreator {
		
		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		private ItemStack maintenanceBtn() {
			ItemCreator creator = new ItemCreator();
			creator.setName("§6§lMAINTENANCE");
			creator.setMaterial(Material.INK_SACK);
			if (McDreams.instance.isMaintenance()) {
				creator.setByte((byte)10);
				creator.setLore(Arrays.asList("§a§lON"));
			} else {
				creator.setByte((byte)1);
				creator.setLore(Arrays.asList("§c§lOFF"));
			}
			return creator.build();
		}
		
		private ItemStack whitelistBtn() {
			ItemCreator creator = new ItemCreator();
			creator.setName("§f§lWHITELIST");
			creator.setMaterial(Material.PAPER);
			return creator.build();
		}
		
		public McDreams_Server_Settings_Menu() {
			setName("§3§lMc§6§lDreams §7§l- §6§lServeur");
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
		}
		
		public Inventory build() {
			setSlot(0, exit());
			setSlot(1, maintenanceBtn());
			setSlot(2, whitelistBtn());
			return super.build();
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				Player player = (Player)event.getEvent().getWhoClicked();
				ItemStack item = event.getEvent().getCurrentItem();
				if (item.isSimilar(maintenanceBtn())) {
					McDreams.instance.setMaintenance(!McDreams.instance.isMaintenance());
					player.openInventory(build());
				} else if (item.isSimilar(exit())) {
					player.openInventory(getMainMenu().build());
				}
			}
		}
		
	}

	public class McDreams_Players_Settings_Menu extends GUICreator {
		
		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		@Override
		public Inventory build() {
			setName("§9§lJOUEUR LISTE");
			return super.build();
		}
			
		public Inventory open(UUID uuid,Integer page) {
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			List<Joueur> joueurs = new ArrayList<>();
			Iterator<? extends Joueur> it_joueurs = JoueurManager.instance.getJoueurs().iterator();
			it_joueurs.forEachRemaining(joueurs::add);
			Collections.sort(joueurs,JoueurManager.instance.getSorter());
			getPlayerListPageBackup().put(uuid, page);
				if (!joueurs.isEmpty()) {
					if (page<1) {
						open(uuid,1);
					} else if (page == 1) {
						int i = 0; 
						for (int slot=9;slot<getSlots().size()-9;slot++) { 
							if (joueurs.size()<=i) {break;}
							Joueur target = joueurs.get(i);
							if (target.getOfflinePlayerIfHasPlayed().isOnline() && target.getPlayerIfOnline() != null) {
								setSlot(slot, SkullUtils.getPlayerSkull(target.getPlayerIfOnline()));
							} else {
								setSlot(slot, SkullUtils.getSkullOfOfflinePlayer(target.getOfflinePlayerIfHasPlayed()));
							}
							i++;
						}
						if (joueurs.size()>i) { 
							setSlot(50, next_btn);
						}
					} else if (page > 1){
						int i = ((getLine()-18)*page)-27;
						for (int slot=9;slot<getSlots().size()-9;slot++) {
							if (joueurs.size()<=i) { break;}
							Joueur target = joueurs.get(i);
							if (target.getOfflinePlayerIfHasPlayed().isOnline()) {
								setSlot(slot, SkullUtils.getPlayerSkull(joueurs.get(i).getPlayerIfOnline()));
							} else {
								setSlot(slot, SkullUtils.getSkullOfOfflinePlayer(target.getOfflinePlayerIfHasPlayed()));
							}
							i++;
						}
						setSlot(48, prev_btn);
						if (joueurs.size()>i) { 
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
						List<Joueur> joueurs = new ArrayList<>();
						Iterator<? extends Joueur> it_joueurs = JoueurManager.instance.getJoueurs().iterator();
						it_joueurs.forEachRemaining(joueurs::add);
						Collections.sort(joueurs,JoueurManager.instance.getSorter());
						UUID uuid = event.getEvent().getWhoClicked().getUniqueId();
						Integer page = getPlayerListPageBackup().get(event.getEvent().getWhoClicked().getUniqueId());
						if (event.getEvent().getCurrentItem().isSimilar(next_btn)) {
							event.getEvent().getWhoClicked().openInventory(open(uuid, page+1));
						} else if (event.getEvent().getCurrentItem().isSimilar(prev_btn)) {
							event.getEvent().getWhoClicked().openInventory(open(uuid, page-1));
						} else if (event.getEvent().getCurrentItem().getType() == Material.SKULL_ITEM) {
							getPlayerSelectedPlayerBackup().put(uuid, joueurs.get(getPlayerIndex(uuid, event.getEvent().getRawSlot())).getUuid());
							//Bukkit.broadcastMessage(event.getEvent().getRawSlot() + " " + page + " " + getNPCIndex(uuid, event.getEvent().getRawSlot()));
							//NPC npcdd= npcs.get(getNPCIndex(uuid, event.getEvent().getRawSlot()));
							//Bukkit.broadcastMessage(npcdd.getName() + " " + npcdd.getId());
							event.getEvent().getWhoClicked().openInventory(getPlayerInfoMenu().build(uuid,joueurs.get(getPlayerIndex(uuid, event.getEvent().getRawSlot())).getUuid()));
						} else if (event.getEvent().getCurrentItem().isSimilar(exit())) {
							event.getEvent().getWhoClicked().openInventory(mainMenu.build());
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

	public class McDreams_Players_Infos_Menu extends GUICreator {

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		private ItemStack saturationBtn(Joueur joueur) {
			ItemCreator creator = new ItemCreator();
			if (joueur.isSaturation()) {
				creator.setName("§a§l✔SATURATION✔");	
			} else {
				creator.setName("§c§l✖SATURATION✖");
			}
			creator.setMaterial(Material.GRILLED_PORK);
			return creator.build(); 	
		}
		
		private ItemStack useFakeInventoryBtn(Joueur joueur) {
			ItemCreator creator = new ItemCreator();
			if (joueur.isUse_fake_inv()) {
				creator.setName("§a§l✔FAKE INVENTAIRE✔");	
			} else {
				creator.setName("§c§l✖FAKE INVENTAIRE✖");
			}
			creator.setLore(Arrays.asList("§6§lCLIQUE DROIT §3§lpour ouvrir"));
			creator.setMaterial(Material.ENDER_CHEST);
			return creator.build(); 	
		}
		
		private ItemStack godBtn(Joueur joueur) {
			ItemCreator creator = new ItemCreator();
			if (joueur.isGod()) {
				creator.setName("§a§l✔GOD✔");	
			} else {
				creator.setName("§c§l✖GOD✖");
			}
			creator.setMaterial(Material.TOTEM);
			return creator.build(); 	
		}

		private ItemStack muteBtn(Joueur joueur) {
			ItemCreator creator = new ItemCreator();
			if (joueur.isChatMuted()) {
				creator.setName("§a§l✔MUTE✔");	
			} else {
				creator.setName("§c§l✖MUTE✖");
			}
			creator.setMaterial(Material.BOOK);
			return creator.build(); 	
		}
		
		private ItemStack flyBtn(Joueur joueur) {
			ItemCreator creator = new ItemCreator();
			OfflinePlayer player = joueur.getOfflinePlayerIfHasPlayed();
			if (player.isOnline()) {
				if (player.getPlayer().getAllowFlight()) {
					creator.setName("§a§l✔FLY✔");	
				} else {
					creator.setName("§c§l✖FLY✖");
				}
				creator.setMaterial(Material.FEATHER);
			} else {
				creator.setName("§c§lERREUR");
				creator.setMaterial(Material.STAINED_GLASS_PANE);
				creator.setByte((byte)14);
			}
			return creator.build(); 	
		}
		
		private ItemStack breakPlaceBtn(Joueur joueur) {
			ItemCreator creator = new ItemCreator();
			if (joueur.isPlace_break()) {
				creator.setName("§a§l✔CASSER/POSER✔");	
			} else {
				creator.setName("§c§l✖CASSER/POSER✖");
			}
			creator.setMaterial(Material.GRASS);
			return creator.build(); 	
		}

		private ItemStack moneyBtn() {
			ItemCreator creator = new ItemCreator();
			creator.setName("§e§lARGENT");
			creator.setMaterial(Material.GOLD_NUGGET);
			return creator.build();
		}
		
		private ItemStack reloadBtn() {
			ItemCreator creator = new ItemCreator();
			creator.setName("§b§lRELOAD");
			creator.setMaterial(Material.NETHER_STAR);
			return creator.build();
		}
		
		public McDreams_Players_Infos_Menu() {
		}
		
		public Inventory build(UUID userID,UUID targetID) {
			setMaxLine(3);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			getPlayerSelectedPlayerBackup().put(userID, targetID);
			Joueur target = Joueur.getJoueur(targetID);
			OfflinePlayer player = target.getOfflinePlayerIfHasPlayed();
			if (player != null) {
				setName("§9§lJOUEUR §7§l- §b§l"+player.getName());
			}
			setSlot(0, exit());
			setSlot(1, godBtn(target));
			setSlot(2, muteBtn(target));
			setSlot(3, flyBtn(target));
			setSlot(4, breakPlaceBtn(target));
			setSlot(5, useFakeInventoryBtn(target));
			setSlot(6, saturationBtn(target));
			setSlot(7, moneyBtn());
			setSlot(getLine()-1, reloadBtn());
			return super.build();
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				Player player = (Player)event.getEvent().getWhoClicked();
				Joueur joueur = Joueur.getJoueur(getPlayerSelectedPlayerBackup().get(player.getUniqueId()));
				player.openInventory(build(player.getUniqueId(), joueur.getUuid()));
				ItemStack item = event.getEvent().getCurrentItem();
				if (item.isSimilar(godBtn(joueur))) {
					joueur.setGod(!joueur.isGod());
					player.openInventory(build(player.getUniqueId(), joueur.getUuid()));
				} else if (item.isSimilar(muteBtn(joueur))) {
					joueur.setChatMuted(!joueur.isChatMuted());
					player.openInventory(build(player.getUniqueId(), joueur.getUuid()));
				} else if (item.isSimilar(useFakeInventoryBtn(joueur))) {
					if (event.getEvent().isLeftClick()) {
						joueur.setUse_fake_inv(!joueur.isUse_fake_inv());
						player.openInventory(build(player.getUniqueId(), joueur.getUuid()));
					} else if (event.getEvent().isRightClick()) {
						player.openInventory(joueur.getBackpack());
						Joueur.getJoueur(player.getUniqueId()).setState(JoueurTempStates.LOOKING_INTO_BACKPACK_FROM_ADMIN, true);
					}
				} else if (item.isSimilar(flyBtn(joueur))) {
					Player jPlayer = joueur.getPlayerIfOnline();
					if (jPlayer != null) {
						jPlayer.setAllowFlight(!jPlayer.getAllowFlight());
						player.openInventory(build(player.getUniqueId(), joueur.getUuid()));
					}
				} else if (item.isSimilar(breakPlaceBtn(joueur))) {
					joueur.setPlace_break(!joueur.isPlace_break());
					player.openInventory(build(player.getUniqueId(),joueur.getUuid()));
				} else if (item.isSimilar(moneyBtn())) {
					player.openInventory(playerMoneyMenu.build(player.getUniqueId(), joueur.getUuid()));
				} else if (item.isSimilar(saturationBtn(joueur))) {
					joueur.setSaturation(!joueur.isSaturation());
					player.openInventory(build(player.getUniqueId(),joueur.getUuid()));
				} else if (item.isSimilar(reloadBtn())) {
					joueur.reload();
				} else if (item.isSimilar(exit())) {
					player.openInventory(playersMenu.open(player.getUniqueId(), 1));
				}
			}
		}
		
	}
	
	public class McDreams_Money_Menu extends GUICreator {

		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		private ItemStack moneyInfo(Joueur joueur) {
			OfflinePlayer player = joueur.getOfflinePlayerIfHasPlayed();
			ItemStack item = new ItemCreator().setMaterial(Material.PAPER).setName("§6§l"+player.getName()+" §7§l: §e§l"+joueur.getMoney()).build();
			return item;
		}
		
		private ItemStack plusBTN() {
			ItemStack item = SkullUtils.getCustomSkullFromTexture(Skins.RandomSkin.fleche_droite.getTEXTURE());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§a§l+");
			meta.setLore(Arrays.asList("§e§l- CLICK : +1","§e§l- SHIFT CLICK : +5"));
			item.setItemMeta(meta);
			return item;
		}
		private ItemStack moinBTN() {
			ItemStack item = SkullUtils.getCustomSkullFromTexture(Skins.RandomSkin.fleche_gauche.getTEXTURE());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§c§l-");
			meta.setLore(Arrays.asList("§e§l- CLICK : -1","§e§l- SHIFT CLICK : -5"));
			item.setItemMeta(meta);
			return item;
		}
		
		public McDreams_Money_Menu() {
			setName("§3§lMc§6§lDreams §7§l- §e§lARGENT");
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
		}
		
		public Inventory build(UUID pUUID,UUID tUUID) {
			playerSelectedPlayerBackup.put(pUUID, tUUID);
			setSlot(0, exit());
			setSlot(2, moinBTN());
			setSlot(4, moneyInfo(Joueur.getJoueur(tUUID)));
			setSlot(6, plusBTN());
			return super.build();
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				Player player = (Player)event.getEvent().getWhoClicked();
				Joueur target = Joueur.getJoueur(playerSelectedPlayerBackup.get(player.getUniqueId()));
				ItemStack item = event.getEvent().getCurrentItem();
				boolean isShift = event.getEvent().isShiftClick();
				if (item.isSimilar(plusBTN())) {
					if (isShift) { target.setMoney(target.getMoney()+5); }
					else {target.setMoney(target.getMoney()+1);}
					player.openInventory(build(player.getUniqueId(), target.getUuid()));
				} else if (item.isSimilar(moinBTN())) {
					if (isShift) { if (!(target.getMoney()-5 < 0)) {target.setMoney(target.getMoney()-5); } else { target.setMoney(0); }}
					else { if (!(target.getMoney()-1 < 0)) {target.setMoney(target.getMoney()-1);  } else { target.setMoney(0); }}
					player.openInventory(build(player.getUniqueId(), target.getUuid()));
				} else if (item.isSimilar(exit())) {
					player.openInventory(getPlayerInfoMenu().build(player.getUniqueId(), target.getUuid()));
				}
			}
		}
		
	}
	
}
