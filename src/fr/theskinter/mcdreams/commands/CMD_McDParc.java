package fr.theskinter.mcdreams.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.theskinter.mcdreams.objects.parc.Land;
import fr.theskinter.mcdreams.objects.parc.ParcManager;
import fr.theskinter.mcdreams.utils.TitleManager;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class CMD_McDParc implements CommandExecutor, TabCompleter {

	public enum Perms {
		execute("mcdreams.command.mcparc");
		@Getter private String perm;
		private Perms(String perm) { this.perm = perm; }
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission(Perms.execute.perm)) return false;
			int x=0;
			for (String arg : args) {
				args[x] = ChatColor.stripColor(arg);
				x++;
			}
			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("lands")) {
					if (args[1].equalsIgnoreCase("create")) {
						if (!ParcManager.instance.hasLand(args[2])) {
							player.sendMessage("§a§lLand §3§l"+args[2]+"§a§l créé.");
							ParcManager.instance.registerLand(args[2]);
						} else {
							player.sendMessage("§c§lLand §3§l"+args[2]+"§c§l existe déjà");
						}
					}
				}
			} else if (args.length == 4) {
				if (args[0].equalsIgnoreCase("attractions")) {
					if (args[1].equalsIgnoreCase("create")) {
						if (ParcManager.instance.hasLand(args[2])) {
							Land land = ParcManager.instance.getLandByName(args[2]);
							if (!land.hasAttraction(args[3])) {
								land.registerAttraction(args[3]);
								player.sendMessage("§a§lAttraction §3§l"+args[3]+"§a§l enregistrée dans le land §3§l"+args[2]);
							} else {
								player.sendMessage("§c§lAttraction §3§l"+args[3]+"§c§l existe déjà dans le land §3§l"+args[2]);
							}
						}
					} else if (args[1].equalsIgnoreCase("defineWarpLocation")) {
						if (ParcManager.instance.hasLand(args[2])) {
							Land land = ParcManager.instance.getLandByName(args[2]);
							if (land.hasAttraction(args[3])) {
								Location newWarpLoc = player.getLocation().add(0,1,0).getBlock().getLocation().add(0.5,0,0.5);
								land.getAttractionByName(args[3]).setWarpLocation(newWarpLoc);
								player.sendMessage("§a§lLa position de warp de l'attraction §3§l"+args[3]+"§a§l du land §3§l"+args[2]+"§a§l est définie en :");
								player.sendRawMessage("§6§lX §7§l: §9§l"+newWarpLoc.getX()+"§r\n"
										+ "§6§lY §7§l: §9§l"+newWarpLoc.getY()+"§r\n"
										+ "§6§lZ §7§l: §9§l"+newWarpLoc.getZ()+"§r\n"
										+ "§6§lMonde §7§l: §9§l"+newWarpLoc.getWorld().getName());
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return getCompleters(sender,args);
	}
	
	private List<String> getCompleters(CommandSender sender,String[] args) {
		ArrayList<String> returned = new ArrayList<>();
		List<String> args0 = Arrays.asList("lands","attractions");
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
			if (args[0].equalsIgnoreCase("lands")) {
				List<String> args1 = Arrays.asList("create","list");
				if (args[1].equals("")) {
					returned.addAll(args1);
				} else {
					for (String arg1 : args1) {
						if (arg1.toLowerCase().startsWith(args[1].toLowerCase())) {
							returned.add(arg1);
						}
					}
				}
			} else if (args[0].equalsIgnoreCase("attractions")) {
				List<String> args1 = Arrays.asList("create");
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
		} else if (args.length == 3) {
			 if (args[0].equalsIgnoreCase("attractions")) {
				 if (args[1].equalsIgnoreCase("create")) {
					 if (ParcManager.instance.getLands().isEmpty()) {
						 if (sender instanceof Player) {
							 TitleManager.sendTitle((Player)sender, "§4§lINFO", "§c§lIl n'existe aucun lands", 5);
						 }
					 } else {
						 List<String> args2 = new ArrayList<>();
						 for (Land land : ParcManager.instance.getLands()) { args2.add(land.getName()); }
						 if (args[2].equals("")) {
							 returned.addAll(args2);
						 } else {
							 for (String arg2 : args2) {
								 if (arg2.toLowerCase().startsWith(args[2].toLowerCase())) {
									 returned.add(arg2);
								 }
							 }
						 }
					 }
				 }
			 }
		}
		return returned;
	}

	/*
	 * 
	 */
	
}
