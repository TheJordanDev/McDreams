package fr.theskinter.mcdreams.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.guis.GUI_Warps;

public class CMD_McDWarp implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.openInventory(GUI_Warps.instance.getWarpLandSelectGUI().open(player.getUniqueId(), 1));
			} else {
				if (!sender.hasPermission(McDreams.Perms.acces_staff_menu.p)) return false;
				if (!player.hasPermission(McDreams.Perms.execute_cmd_mcwarp.p)) return false;
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
		List<String> args0 = new ArrayList<String>();
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
		}
		return returned;
	}

}
