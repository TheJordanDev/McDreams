package fr.theskinter.mcdreams.apis.citizens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.GUIInteractEvent;
import fr.theskinter.mcdreams.utils.Skins.SkinsENUM;
import fr.theskinter.mcdreams.utils.Skulls.SkullUtils;
import fr.theskinter.mcdreams.utils.creators.GUICreator;
import fr.theskinter.mcdreams.utils.creators.ItemCreator;
import fr.theskinter.mcdreams.utils.filter.NPCSorter;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.md_5.bungee.api.ChatColor;

public class CitizensAPI_GUI {
	
	@Getter private List_GUI list;
	@Getter private NPC_Info_GUI infos;
	@Getter private NPC_Skin_GUI skins;
	
	@Getter private Map<UUID,Integer> citizensGUIPageList = new HashMap<>();	
	@Getter private Map<UUID,Integer> citizensSkinPageList = new HashMap<>();
	@Getter private Map<UUID,Integer> playerSelectedNPCID = new HashMap<>();
	
	public CitizensAPI_GUI() {
		list = new List_GUI();
		infos = new NPC_Info_GUI();
		skins = new NPC_Skin_GUI();
	}
	
	public class List_GUI extends GUICreator {
		
		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();
		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		public List_GUI() {
			if (!McDreams.isEnabled("citizens")) { return;}
		}
		
		@Override
		public Inventory build() {
			setName("§9§lNPC MANAGER");
			return super.build();
		}
		
		public Inventory open(UUID uuid,Integer page) {
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			List<NPC> npcs = new ArrayList<NPC>();
			Iterator<NPC> it_npcs = CitizensAPI.getNPCRegistry().iterator();
			it_npcs.forEachRemaining(npcs::add);
			Collections.sort(npcs,new NPCSorter());
			getCitizensGUIPageList().put(uuid, page);
			if (!npcs.isEmpty()) {
				if (page<1) {
					open(uuid,1);
				} else if (page == 1) {
					int i = 0; 
					for (int slot=9;slot<getSlots().size()-9;slot++) { 
						if (npcs.size()<=i) {break;}
						setSlot(slot, SkullUtils.getNPCSkull(npcs.get(i)));	
						i++;
					}
					if (npcs.size()>i) { 
						setSlot(50, next_btn);
					}
				} else if (page > 1){
					int i = ((getLine()-18)*page)-27;
					for (int slot=9;slot<getSlots().size()-9;slot++) {
						if (npcs.size()<=i) { break;}
						setSlot(slot, SkullUtils.getNPCSkull(npcs.get(i)));	
						i++;
					}
					setSlot(48, prev_btn);
					if (npcs.size()>i) { 
						setSlot(50, next_btn);
					}
				}
			} else {
				setSlot(22, new ItemCreator().setName("§7§lAucun NPC !!").setMaterial(Material.PAPER).build());
			}
			return build();
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				if (event.getEvent().getClick() == ClickType.LEFT) {
					List<NPC> npcs = new ArrayList<NPC>();
					Iterator<NPC> it_npcs = CitizensAPI.getNPCRegistry().iterator();
					it_npcs.forEachRemaining(npcs::add);
					npcs.sort(new NPCSorter());
					UUID uuid = event.getEvent().getWhoClicked().getUniqueId();
					Integer page = getCitizensGUIPageList().get(event.getEvent().getWhoClicked().getUniqueId());
					if (event.getEvent().getCurrentItem().isSimilar(next_btn)) {
						event.getEvent().getWhoClicked().openInventory(getList().open(uuid, page+1));
					} else if (event.getEvent().getCurrentItem().isSimilar(prev_btn)) {
						event.getEvent().getWhoClicked().openInventory(getList().open(uuid, page-1));
					} else if (event.getEvent().getCurrentItem().getType() == Material.SKULL_ITEM) {
						getPlayerSelectedNPCID().put(uuid, npcs.get(getNPCIndex(uuid, event.getEvent().getRawSlot())).getId());
						event.getEvent().getWhoClicked().openInventory(getInfos().open(uuid));
					} else if (event.getEvent().getCurrentItem().isSimilar(exit())) {
						event.getEvent().getWhoClicked().openInventory(McDreams.instance.getMcDreamsGUI().mainMenu.build());
					}
				}
			}
		}
		
		public Integer getNPCIndex(UUID uuid,Integer slot) {
			Integer page = getCitizensGUIPageList().get(uuid);
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
	
	public class NPC_Info_GUI extends GUICreator {
		
		private ItemStack rename_btn = new ItemCreator().setName("§6§lRENNOMER").setMaterial(Material.NAME_TAG).build();
		private ItemStack skin_btn = new ItemCreator().setName("§9§lSKIN").setMaterial(Material.ARMOR_STAND).build();
		private ItemStack tp_btn = new ItemCreator().setName("§3§lTELEPORTER A").setMaterial(Material.ENDER_PEARL).build();
		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		private ItemStack spawn_btn(boolean isSpawned) {
			ItemCreator stack = new ItemCreator();
			stack.setMaterial(Material.INK_SACK);
			if (isSpawned) { 
				stack.setName("§c§lDESPAWN");
				stack.setByte((byte)1);
			} else {
				stack.setName("§a§lRESPAWN");
				stack.setByte((byte)7);
			}
			return stack.build();
		}
		
		private ItemStack info_btn(NPC npc) {

			ItemCreator stack = new ItemCreator()
					.setMaterial(Material.PAPER)
					.setName("§7§lInfos")
					.setLore(Arrays.asList("§6§lNOM§7§l: "+npc.getName()+"§r"));
			if (npc.isSpawned()) {
				Location loc = npc.getStoredLocation();
				stack.setLore(Arrays.asList(
									"§6§lNOM§7§l: "+npc.getName()+"§r",
									"§6§lPOSITION§7§l: §e§lX§7§l: §a§l"+loc.getBlockX()+
													" §7§l| §e§lY§7§l: §a§l"+loc.getBlockY()+
													" §7§l| §e§lZ§7§l: §a§l"+loc.getBlockZ()+"§r",
									"§6§lMONDE§7§l: §a§l"+loc.getWorld().getName()+"§r"));
			}
			return stack.build();
		}
		
		public NPC_Info_GUI() {
			if (!McDreams.isEnabled("citizens")) { return;}
		}
		
		public Inventory build(UUID uuid) {
			
			if (!getPlayerSelectedNPCID().containsKey(uuid) || CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid)) == null) return super.build();
			NPC npc = CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid));
			setName("§9§lNPC MANAGER : §6§l"+npc.getName());
			return super.build();
		}
		
//		new LineDraw_Scheduler(player, player.getLocation(), player.getLocation().add(0D, 5D, 0D)).runTaskTimer(McDreams.instance, 5L, 0L);
		
		public Inventory open(UUID uuid) {
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			if (!getPlayerSelectedNPCID().containsKey(uuid) || CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid)) == null) return super.build();
			NPC npc = CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid));
			setSlot(10, info_btn(npc));
			setSlot(11, rename_btn);
			setSlot(12, spawn_btn(npc.isSpawned()));
			setSlot(13, skin_btn);
			if (npc.isSpawned()) {
				setSlot(14, tp_btn);
			}
			return this.build(uuid);
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				if (!(event.getEvent().getWhoClicked() instanceof Player)) return;
				Player player = (Player) event.getEvent().getWhoClicked();
				UUID uuid = event.getEvent().getWhoClicked().getUniqueId();
				if (!getPlayerSelectedNPCID().containsKey(uuid) || CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid)) == null) return;
				NPC npc = CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid));
				getPlayerSelectedNPCID().put(uuid, npc.getId());
				ItemStack item = event.getEvent().getCurrentItem();
				if (item.isSimilar(rename_btn)) {
					ConversationFactory convFac = new ConversationFactory(McDreams.instance);
					convFac.withFirstPrompt(new NPC_INFO_RENAME_CONVERSATION(uuid));
					convFac.withLocalEcho(false);
					convFac.withEscapeSequence("cancel");
					player.beginConversation(convFac.buildConversation(player));
					player.closeInventory();
				} else if (item.isSimilar(spawn_btn(npc.isSpawned()))) {
					if (npc.isSpawned()) {
						npc.despawn();
					} else {
						npc.spawn(npc.getStoredLocation());
					}
					player.openInventory(open(uuid));
				} else if (item.isSimilar(skin_btn)) {
					player.openInventory(skins.open(uuid, 1));
				} else if (item.isSimilar(tp_btn)) {
					player.teleport(npc.getEntity().getLocation());
				} else if (item.isSimilar(exit())) {
					player.openInventory(list.open(uuid, 1));
				}
			}
		}
	}
	
	public class NPC_Skin_GUI extends GUICreator {
		
		private ItemStack next_btn = new ItemCreator().setName("§e§lSuivant").setMaterial(Material.GOLD_PLATE).build();
		private ItemStack prev_btn = new ItemCreator().setName("§6§lPrécédent").setMaterial(Material.IRON_PLATE).build();
		private ItemStack exit() { return new ItemCreator().setMaterial(Material.BARRIER).setName("§c§lSORTIE").build(); }
		
		private ItemStack skin_btn(SkinsENUM skin) {
			ItemStack creator = SkullUtils.getCustomSkullFromTexture(skin.getTEXTURE());
			ItemMeta meta = creator.getItemMeta();
			String name = skin.name();
			meta.setDisplayName("§e§l"+name.substring(0,1).toUpperCase()+name.substring(1));
			creator.setItemMeta(meta);
			return creator;
		}
		
		public NPC_Skin_GUI() {
			if (!McDreams.isEnabled("citizens")) { return;}
		}
		
		public Inventory build(UUID uuid) {
			return super.build();
		}
		
		public Inventory open(UUID uuid,Integer page) {
			setMaxLine(6);
			setBackGround(new ItemCreator().setName(" ").setMaterial(Material.STAINED_GLASS_PANE).setByte((byte)7).build());
			setSlot(0, exit());
			if (!getPlayerSelectedNPCID().containsKey(uuid) || CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid)) == null) return build(uuid);
			NPC npc = CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid));
			setName("§9§lSKIN MANAGER : §6§l"+npc.getName());
			List<SkinsENUM> npcs = new ArrayList<SkinsENUM>(Arrays.asList(SkinsENUM.values()));
			getCitizensSkinPageList().put(uuid, page);
			if (!npcs.isEmpty()) {
				if (page<1) {
					open(uuid,1);
				} else if (page == 1) {
					int i = 0; 
					for (int slot=9;slot<getSlots().size()-9;slot++) { 
						if (npcs.size()<=i) {break;}
						setSlot(slot, skin_btn(npcs.get(i)));	
						i++;
					}
					if (npcs.size()>i) { 
						setSlot(50, next_btn);
					}
				} else if (page > 1){
					int i = ((getLine()-18)*page)-27;
					for (int slot=9;slot<getSlots().size()-9;slot++) {
						if (npcs.size()<=i) { break;}
						setSlot(slot, skin_btn(npcs.get(i)));
						i++;
					}
					setSlot(48, prev_btn);
					if (npcs.size()>i) { 
						setSlot(50, next_btn);
					}
				}
			} else {
				setSlot(22, new ItemCreator().setName("§7§lAucun Skins !!").setMaterial(Material.PAPER).build());
			}
			return build();
		}
		
		@EventHandler
		public void onInteract(GUIInteractEvent event) {
			if (event.getGui_id().equals(getId())) {
				event.setCancelled(true);
				if (event.getEvent().getClick() == ClickType.LEFT) {
					UUID uuid = event.getEvent().getWhoClicked().getUniqueId();
					if (!getPlayerSelectedNPCID().containsKey(uuid) || CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid)) == null) return;
					NPC npc = CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid));
					Integer page = getCitizensGUIPageList().get(event.getEvent().getWhoClicked().getUniqueId());
					if (event.getEvent().getCurrentItem().isSimilar(next_btn)) {
						event.getEvent().getWhoClicked().openInventory(getList().open(uuid, page+1));
					} else if (event.getEvent().getCurrentItem().isSimilar(prev_btn)) {
						event.getEvent().getWhoClicked().openInventory(getList().open(uuid, page-1));
					} else if (event.getEvent().getCurrentItem().getType() == Material.SKULL_ITEM) {
						if (SkinsENUM.values().length >= getSkinIndex(uuid, event.getEvent().getRawSlot())) {
							SkinsENUM skin = SkinsENUM.values()[getSkinIndex(uuid, event.getEvent().getRawSlot())];
							if (npc.getEntity() instanceof SkinnableEntity) {
								SkinnableEntity npcE = (SkinnableEntity) npc.getEntity();
								npcE.setSkinPersistent(skin.name(), skin.getSIGNATURE(), skin.getTEXTURE());
							}
							//event.getEvent().getWhoClicked().openInventory(getInfos().open(uuid));
						}
					} else if (event.getEvent().getCurrentItem().isSimilar(exit())) {
						event.getEvent().getWhoClicked().openInventory(getInfos().open(uuid));
					}
				}
			}
		}
		
		public Integer getSkinIndex(UUID uuid,Integer slot) {
			Integer page = getCitizensSkinPageList().get(uuid);
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

	
	public class NPC_INFO_RENAME_CONVERSATION extends StringPrompt {
		
		private UUID uuid;
		
		public NPC_INFO_RENAME_CONVERSATION(UUID uuid) {
			this.uuid = uuid;
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String message) {
			if (!(context.getForWhom() instanceof Player)) return Prompt.END_OF_CONVERSATION;
			if (!getPlayerSelectedNPCID().containsKey(uuid) || CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid)) == null) { 
				context.getForWhom().sendRawMessage("§c§lVous n'avez pas séléctionner de NPC !!");
				return Prompt.END_OF_CONVERSATION;
			}
			Player player = (Player) context.getForWhom();
			NPC npc = CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid));
			String name = ChatColor.translateAlternateColorCodes('&',message.replaceAll(" ", "_"));
			context.getForWhom().sendRawMessage("§a§lLe nom du NPC est maintenant §6§l"+name);
			npc.setName(name);
			player.openInventory(getInfos().open(uuid));
			return Prompt.END_OF_CONVERSATION;
		}

		@Override
		public String getPromptText(ConversationContext context) {
			NPC npc = CitizensAPI.getNPCRegistry().getById(getPlayerSelectedNPCID().get(uuid));
			context.getForWhom().sendRawMessage("§9§lVeuillez entrer le nouveau nom du NPC : §6§l"+npc.getName());
			return "§b§lPour annuler l'action entrez \"cancel\"";
		}

	}

}
