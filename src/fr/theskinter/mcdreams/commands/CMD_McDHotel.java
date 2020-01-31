package fr.theskinter.mcdreams.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.objects.parc.Hotel;
import fr.theskinter.mcdreams.objects.parc.Parc;

public class CMD_McDHotel implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission(McDreams.Perms.execute_cmd_mcdreams.p)) return false;
		if (sender instanceof Player) {
			if (args.length == 0) {
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("remove")) {
			        String name = args[1];
			        name.replaceAll("_", " ");
					if (Parc.instance.hasHotel(name)) {
						Parc.instance.getHotels().remove(Parc.instance.getHotelByName(name));
						sender.sendMessage("§c§lHotel §6§l"+name+"§c§l supprimer !");
					}
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("create")) {
				    try {
				        int max_room = Integer.parseInt(args[2]);
				        String name = args[1];
				        name.replaceAll("_", " ");
				        if (Parc.instance.hasHotel(name)) {
				        	sender.sendMessage("§c§lHotel §6§l"+name+"§c§l éxiste déjà !");
				        } else {
				        	Parc.instance.registerHotel(name, max_room);
				        	sender.sendMessage("§a§lHotel §6§l"+name+"§a§l créé !");
				        	
				        }
				    } catch (NumberFormatException nfe) {
				    	sender.sendMessage("§c§lVeuillez spécifier un nombre de chambre correct !");
				        return false;
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
		List<String> args0 = Arrays.asList("create","remove");
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
			if (args[0].equalsIgnoreCase("remove")) {
				for (Hotel hotel : Parc.instance.getHotels()) {
					returned.add(hotel.getName().replaceAll(" ", "_"));
				}
			}
		}
		return returned;
	}

}
