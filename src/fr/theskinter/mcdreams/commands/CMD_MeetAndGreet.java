package fr.theskinter.mcdreams.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.theskinter.mcdreams.utils.TitleManager;

public class CMD_MeetAndGreet implements CommandExecutor, TabCompleter{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = ((Player)sender);
			TitleManager.sendActionBar(player, "§c§lH§6§le§3§ly", 20);
			//player.openInventory(McDreams.instance.getMeetAndGreetGUI().mainMenu.build());
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return getCompleters(args);
	}
	
	private List<String> getCompleters(String[] args) {
		ArrayList<String> returned = new ArrayList<>();
		return returned;
	}
}
