package fr.theskinter.mcdreams.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.events.JoueurDamageEvent;
import fr.theskinter.mcdreams.events.JoueurFoodChange;
import fr.theskinter.mcdreams.events.RegionEnterEvent;
import fr.theskinter.mcdreams.events.RegionLeaveEvent;
import fr.theskinter.mcdreams.objects.Joueur;
import fr.theskinter.mcdreams.objects.Portal;
import fr.theskinter.mcdreams.objects.parc.Attraction;
import fr.theskinter.mcdreams.objects.parc.Land;
import fr.theskinter.mcdreams.objects.parc.ParcManager;
import fr.theskinter.mcdreams.utils.JoueurTempStates;
import fr.theskinter.mcdreams.utils.Utils;

public class JoueurEventListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!Joueur.doesJoueurExist(event.getPlayer())) {
			new Joueur(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerDamage(JoueurDamageEvent event) {
		if (event.getJoueur().getPlayerIfOnline() != null) {
			if (event.getJoueur().getPlayerIfOnline().isInsideVehicle()) {
				event.setCancelled(true);
			}
		}
		if (event.getJoueur().isGod()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerFoodChange(JoueurFoodChange event) {
		if (event.getJoueur().isSaturation()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onTabComplete(TabCompleteEvent event) {
		if (event.getSender() instanceof Player) {
			event.setCancelled(true);
			Player player = (Player) event.getSender();
			String[] args = event.getBuffer().split(" ");
			if (args[args.length-1].startsWith("@")) {
				event.setCancelled(false);
				String name = args[args.length-1].substring(1);
				List<String> players = Utils.mentionLoopPlayers(name);
				event.setCompletions(players);
				return;
			}
			if (player.hasPermission("mcdreams.tabcomplete")) {
				event.setCancelled(false);
				return;
			}
			args[0] = args[0].replaceFirst("/", "");
			player.sendTitle("§c§lNOPE", " ", 10, 20, 10);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onChatMention(AsyncPlayerChatEvent event) {
		if (event.getMessage().equals("inv")) { event.getPlayer().openInventory(Joueur.getJoueur(event.getPlayer()).getBackpack()); }
		if (Joueur.getJoueur(event.getPlayer()).isChatMuted()) { event.setCancelled(true); return; }
		StringBuilder message = new StringBuilder();
		String[] words = event.getMessage().split(" ");
		for (int i = 0;i<words.length;i++) {
			String word = words[i];
			if (word.startsWith("@")) {
				String pName = word.substring(1);
				Player mentionned = Bukkit.getPlayerExact(pName);
				if (mentionned != null) {
					Joueur joueur = Joueur.getJoueur(mentionned);
					if (joueur.isChatNotified()) {
						words[i] = "§6§l"+word+"§r";
						mentionned.playSound(mentionned.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,SoundCategory.VOICE, 1F,1F);
					}
				}
			}
			if (event.getPlayer().hasPermission("mcdreams.chat.color")) { message.append(ChatColor.translateAlternateColorCodes('&', words[i]+" "));
			} else {message.append(words[i]+" "); }
		}
		event.setMessage(message.toString());
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if (event.getPlayer() == null) return;
		Player player = event.getPlayer();
		Joueur joueur = Joueur.getJoueur(event.getPlayer()); 
		if (!joueur.isPlace_break()) {
			event.setCancelled(true);
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, SoundCategory.VOICE, 1, 1);
			player.sendTitle(" ", "§c§lTu ne peut faire ça ici !!", 10, 20, 10);
			return;
		}
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockPlaceEvent event) {
		if (event.getPlayer() == null) return;
		Player player = event.getPlayer();
		Joueur joueur = Joueur.getJoueur(event.getPlayer()); 
		if (!joueur.isPlace_break()) {
			event.setCancelled(true);
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, SoundCategory.VOICE, 1, 1);
			player.sendTitle(" ", "§c§lTu ne peut faire ça ici !!", 10, 20, 10);
			return;
		}
	}
	
	@EventHandler
	public void onRegionEnter(RegionEnterEvent event) {
		event.getPlayer().sendMessage("§9§lRegion Entrée : §3§l"+event.getRegion().getName());
	}
	
	@EventHandler
	public void onRegionExit(RegionLeaveEvent event) {
		event.getPlayer().sendMessage("§9§lRegion Quitée : §3§l"+event.getRegion().getName());
	}
	
	@EventHandler
	public void onItemSwitchHandEvent(PlayerSwapHandItemsEvent event) {
		event.setCancelled(true);
		event.getPlayer().updateInventory();
	}
	
	@EventHandler
	public void onItemPickupEvent(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		Joueur joueur = Joueur.getJoueur(player);
		if (joueur.isUse_fake_inv()) {
			if (joueur.getBackpack().firstEmpty() != -1) {
				event.setCancelled(true);
				joueur.getBackpack().addItem(event.getItem().getItemStack());
				event.getItem().remove();
			}
		}
	}
	
	@EventHandler
	public void onItemClickBlockEvent(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getPlayer().getInventory().getItemInOffHand().getType() == Material.MONSTER_EGG) {
			if (event.getHand() != EquipmentSlot.OFF_HAND) return;
			SpawnEggMeta meta = (SpawnEggMeta) event.getPlayer().getInventory().getItemInOffHand().getItemMeta();
			if (meta.getSpawnedType() == EntityType.SHULKER) {
				if (meta.getDisplayName().startsWith("§6§lGénérateur de porail pour §9§l")) {
					event.setCancelled(true);
					String attraName = meta.getDisplayName().replace("§6§lGénérateur de porail pour §9§l", "");
					String landName = ChatColor.stripColor(meta.getLore().get(1));
					Land land = ParcManager.instance.getLandByName(landName); if (land == null) return;
					Attraction attraction = land.getAttractionByName(attraName); if (attraction == null) return;
					BlockFace faceReference = Utils.getCardinalDirection(event.getPlayer()); if (faceReference == null) return;
					Location loc;
					if (event.getClickedBlock().getType().isSolid()) { loc = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlock().getLocation(); } 
					else { loc = event.getClickedBlock().getLocation(); }
					if (event.getBlockFace() == BlockFace.UP) {
						if (Portal.create(event.getPlayer(), loc, faceReference, attraction)) {
							event.getPlayer().getInventory().getItemInOffHand().setAmount(event.getPlayer().getInventory().getItemInOffHand().getAmount()-1);
						}
					}
				}
			}
		} else if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.MONSTER_EGG) {
			SpawnEggMeta meta = (SpawnEggMeta) event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
			if (meta.getSpawnedType() == EntityType.SHULKER) {
				if (meta.getDisplayName().startsWith("§6§lGénérateur de porail pour §9§l")) {
					event.setCancelled(true);
				}
			}
		}
	}
 	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Joueur joueur = Joueur.getJoueur(player);
		if (joueur.is(JoueurTempStates.LOOKING_INTO_BACKPACK_FROM_ADMIN)) {
			joueur.setState(JoueurTempStates.LOOKING_INTO_BACKPACK_FROM_ADMIN,false);
			if (McDreams.instance.getMcDreamsGUI().getPlayerSelectedPlayerBackup().containsKey(player.getUniqueId())) {
				Bukkit.getScheduler().runTaskLater(McDreams.instance, new BukkitRunnable() {
					@Override public void run() {
						UUID target = McDreams.instance.getMcDreamsGUI().getPlayerSelectedPlayerBackup().get(player.getUniqueId());
						player.openInventory(McDreams.instance.getMcDreamsGUI().getPlayerInfoMenu().build(player.getUniqueId(), target));
					}}, 1);
			}
		}
	}

}
