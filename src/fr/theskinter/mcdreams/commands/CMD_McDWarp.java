package fr.theskinter.mcdreams.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.theskinter.mcdreams.guis.GUI_Warps;
import lombok.Getter;

public class CMD_McDWarp implements CommandExecutor, TabCompleter {

	public enum Perms {
		execute("mcdreams.command.mcwarp");
		@Getter private String perm;
		private Perms(String perm) { this.perm = perm; }
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.openInventory(GUI_Warps.instance.getWarpLandSelectGUI().open(player.getUniqueId(), 1));
			} else {
				if (!player.hasPermission(Perms.execute.perm)) return false;
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
