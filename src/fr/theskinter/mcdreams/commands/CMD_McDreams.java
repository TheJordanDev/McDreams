package fr.theskinter.mcdreams.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.objects.Joueur;
import fr.theskinter.mcdreams.utils.Skins.SkinsENUM;
import fr.theskinter.mcdreams.utils.Utils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class CMD_McDreams implements CommandExecutor, TabCompleter{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("mcdreams.cmd")) return false;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("maintenance")) {
				McDreams.instance.setMaintenance(!McDreams.instance.isMaintenance());
				Boolean state = McDreams.instance.isMaintenance();
				if (state) {
					sender.sendMessage("§a§lMcDreams ouvert");
				} else {
					sender.sendMessage("§6§lMcDreams en maintenance");
				}
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("maintenance")) {
				Boolean state = McDreams.instance.isMaintenance();
				if (args[1].equalsIgnoreCase("on")) {
					if (state) return false;
					sender.sendMessage("§a§lMcDreams ouvert");
					McDreams.instance.setMaintenance(false);
				} else if (args[1].equalsIgnoreCase("off")) {
					if (!state) return false;
					sender.sendMessage("§6§lMcDreams en maintenance");
					McDreams.instance.setMaintenance(true);
				}
			}
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			UUID uuid = player.getUniqueId();
			if (args.length == 0) {
				player.openInventory(McDreams.instance.getMcDreamsGUI().getMainMenu().build());
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("god")) {
					Joueur joueur = Joueur.getJoueur(((Player) sender).getUniqueId());
					joueur.setGod(!joueur.isGod());
					if (joueur.isGod()) {
						player.sendMessage("§a§lGOD mode activé");
					} else {
						player.sendMessage("§c§lGOD mode desactivé");
					}
				} else if (args[0].equalsIgnoreCase("npc")) {
					if (!McDreams.isEnabled("Citizens")) { sender.sendMessage("§c§lCitizens n'est pas sur le serveur !"); return false;}
					player.openInventory(McDreams.instance.getCitizensGUI().getList().open(uuid,1));
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("god")) {
					if (Bukkit.getPlayer(args[1]) == null) return false; 
					Joueur joueur = Joueur.getJoueur(Bukkit.getPlayer(args[1]));
					joueur.setGod(!joueur.isGod());
					if (joueur.isGod()) {
						player.sendMessage("§a§lGOD mode activé pour "+joueur.getPlayerIfOnline().getName());
					} else {
						player.sendMessage("§c§lGOD mode desactivé pour "+joueur.getPlayerIfOnline().getName());
					}
				} else if (args[0].equalsIgnoreCase("npc")) {
					if (!McDreams.isEnabled("Citizens")) { sender.sendMessage("§c§lCitizens n'est pas sur le serveur !"); return false;}
					if (args[1].equalsIgnoreCase("s")) {
						NPC selected = CitizensAPI.getDefaultNPCSelector().getSelected(player);
						if (selected != null) {
							McDreams.instance.getCitizensGUI().getPlayerSelectedNPCID().put(uuid, selected.getId());
							player.openInventory(McDreams.instance.getCitizensGUI().getInfos().open(uuid));
						}
					}
				}
			}
		}
		return false;
	}
		
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return getCompleters(args);
	}
	
	private List<String> getCompleters(String[] args) {
		ArrayList<String> returned = new ArrayList<>();
		List<String> args0 = Arrays.asList("god","maintenance","npc");
		if (args.length == 1) {
			if (args[0].equals("")) {
				returned.addAll(args0);
			} else {
				for (String arg : args0) {
					if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
						returned.add(arg);
					}
				}
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("god")) {
				returned.addAll(Utils.loopPlayers(args[1]));
			} else if (args[0].equalsIgnoreCase("maintenance")) {
				List<String> args1 = Arrays.asList("on","off");
				if (args[1].equals("")) {
					returned.addAll(args1);
				} else {
					for (String arg1 : args1) {
						if (arg1.toLowerCase().startsWith(args[1].toLowerCase())) {
							returned.add(arg1);
						}
					}
				}
			}
		}
		return returned;
	}
	
	public static boolean setSkin(GameProfile profile, UUID uuid,SkinsENUM skin) {
		profile.getProperties().put("textures", new Property("textures", skin.getTEXTURE(), skin.getSIGNATURE()));
		return true;
	}
	
}
