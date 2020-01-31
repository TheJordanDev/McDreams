package fr.theskinter.mcdreams.utils.prompts;

import java.util.Arrays;

import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import fr.theskinter.mcdreams.objects.managers.RegionManager;
import lombok.Getter;

public class RegionPrompts {
	
	@Getter private RegionCreatePrompt creationPrompt;
	@Getter private RegionAlreadyExistPrompt alreadyExistPrompt;
	
	public RegionPrompts() {
		this.creationPrompt = new RegionCreatePrompt();
		this.alreadyExistPrompt = new RegionAlreadyExistPrompt();
	}
	
	public class RegionCreatePrompt extends StringPrompt {

		public RegionCreatePrompt() {}
		
		@Override
		public Prompt acceptInput(ConversationContext context, String answer) {
			if (answer.length() <= 16) {
				if (RegionManager.instance.getRegionByName(answer).isEmpty()) {
					context.getForWhom().sendRawMessage("§e§lRégion §7[§3"+answer+"§7] §e§lcrée");
					RegionManager.instance.registerRegion(answer);
				} else {
					return alreadyExistPrompt;
				}
				return END_OF_CONVERSATION;
			} else {
				context.getForWhom().sendRawMessage("§c§lLe nom de la région dois faire moins de 16 caractère.");
				context.getAllSessionData().put("noAsk", true);
				return this;
			}
			
		}

		@Override
		public String getPromptText(ConversationContext context) {
			if (!context.getAllSessionData().containsKey("noAsk")) {
				context.getForWhom().sendRawMessage("§a§lQuel nom voulez vous donner à votre région ?");
				return "§cAnnulez à tous moment en écrivant \"cancel\"";
			}
			return "§6§lVeuillez réessayer !";
		}
		
	}

	public class RegionAlreadyExistPrompt extends FixedSetPrompt {

		public RegionAlreadyExistPrompt() {}
		
		private String[] yesInputs = (String[]) Arrays.asList("OUI").toArray();
		private String[] noInputs = (String[]) Arrays.asList("NON").toArray();

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String answer) {
			Conversable who = context.getForWhom();
			if (isYes(answer)) {
				who.sendRawMessage("§e§lRégion §7[§3"+answer+"§7] §e§lcrée");
				RegionManager.instance.registerRegion(answer);
			} else if (isNo(answer)) {
				who.sendRawMessage("§c§lCréation de région annulée !");
			} else {
				context.getForWhom().sendRawMessage("§c§lVeuillez entré une réponse valide [OUI/NON] !.");
				context.getAllSessionData().put("noAsk", true);
				return this;
			}
			return END_OF_CONVERSATION;
		}
		
		@Override
		public String getPromptText(ConversationContext context) {
			if (!context.getAllSessionData().containsKey("noAsk")) {
				context.getForWhom().sendRawMessage("§a§lUne région nommé "+""+" éxiste déjà.\n Voulez-vous quand même la crée ?");
				return "§cAnnulez à tous moment en écrivant \"cancel\"";
			}
			return "";
		}

		public boolean isYes(String input) { 
			for (String arg : yesInputs) { if (input.equalsIgnoreCase(arg)) { return true; } } return false; 
		}
		
		public boolean isNo(String input) { 
			for (String arg : noInputs) { if (input.equalsIgnoreCase(arg)) { return true; } } return false; 
		}
		
	}
	
}
