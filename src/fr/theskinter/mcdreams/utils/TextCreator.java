package fr.theskinter.mcdreams.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.entity.Player;

import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextCreator {
	
	public TextCreator() {}
	
	public ChatMessageCreator createChatMessage() {
		return new ChatMessageCreator();
	}
	
	public class ChatMessageCreator {
		
		ArrayList<Text> words = new ArrayList<>();
		
		public ChatMessageCreator() { }
		
		public Text createText(String text) {
			return new Text(text);
		}
		
		public void sendToPlayer(Player... players) {
			TextComponent fullMessage = new TextComponent();
			for (Text text : words) {
				fullMessage.addExtra(text.getMessage());
			}
			for (Player player : players) {
				player.spigot().sendMessage(fullMessage);
			}
		}
		
		public void sendToPlayer(Collection<Player> players) {
			TextComponent fullMessage = new TextComponent();
			for (Text text : words) {
				fullMessage.addExtra(text.getMessage());
			}
			for (Player player : players) {
				player.spigot().sendMessage(fullMessage);
			}
		}
		
		public ChatMessageCreator addText(Text text) {
			words.add(text);
			return this;
		}
		
		public class Text {
			
			@Getter private TextComponent message;

			public Text(String text) {
				this.message = new TextComponent(text);
			}
			
			public Text runCommandOnClick(String rawCommand) {
				String command;
				if (rawCommand.startsWith("/")) { command = rawCommand; }
				else {command = "/"+rawCommand; }
				message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
				return this;
			}
			
			public Text openUrlOnClick(String url) {
				message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
				return this;
			}
			
			public Text showTextOnHover(String text) {
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(text).create()));
				return this;
			}
			
		}
		
	}
	
	/*TextComponent homeComponent = new TextComponent("§7§l[§6§lHome§7§l]§r");
	String homeHover = "§8§lUnique home name";
	homeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(homeHover).create()));
	homeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/meetandgreet"));*/
}