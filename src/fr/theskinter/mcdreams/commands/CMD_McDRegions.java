package fr.theskinter.mcdreams.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.objects.Cuboid;
import fr.theskinter.mcdreams.objects.RegionManager;
import fr.theskinter.mcdreams.objects.RegionManager.Region;
import fr.theskinter.mcdreams.utils.TextCreator.ChatMessageCreator.Text;

public class CMD_McDRegions implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("create")) {
					Location downCorner = player.getLocation().add(10D,10D,10D);
					Location upCorner = player.getLocation().add(-10D,-10D,-10D);
					UUID newRegionID = RegionManager.instance.registerRegion("1");
					Region region = RegionManager.instance.getRegionByID(newRegionID);
					region.addZone(new Cuboid(downCorner, upCorner));
					player.sendMessage("Region crée");
					/*Conversation conv = new ConversationFactory(McDreams.instance)
							.withFirstPrompt(McDreams.instance.getPromptManager().getRegionPrompts().getCreationPrompt())
							.withLocalEcho(false)
							.buildConversation(player);
					conv.begin();*/
				} else if (args[0].equalsIgnoreCase("list")) {
					player.sendMessage("§6§l========");
					for (Region region : RegionManager.instance.getRegions()) {
						Text text = McDreams.instance.getTextCreator().createChatMessage()
							.createText("§7§l- §6§l"+region.getName())
							.showTextOnHover("9§l"+region.getId()+"\n§3§l"+region.getZones().size())
							.runCommandOnClick("mcregion info "+region.getName());
						player.spigot().sendMessage(text.getMessage());
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> returned = new ArrayList<String>();
		List<String> args0 = Arrays.asList("create","list");
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
