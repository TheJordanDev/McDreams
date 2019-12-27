package fr.theskinter.mcdreams.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.TabCompleteEvent;

import fr.theskinter.mcdreams.events.JoueurDamageEvent;
import fr.theskinter.mcdreams.events.JoueurFoodChange;
import fr.theskinter.mcdreams.events.RegionEnterEvent;
import fr.theskinter.mcdreams.events.RegionLeaveEvent;
import fr.theskinter.mcdreams.objects.Joueur;
import fr.theskinter.mcdreams.utils.Utils;

public class JoueurEventListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
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
	
}
