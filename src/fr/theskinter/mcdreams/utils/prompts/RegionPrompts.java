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
					context.getForWhom().sendRawMessage("�e�lR�gion �7[�3"+answer+"�7] �e�lcr�e");
					RegionManager.instance.registerRegion(answer);
				} else {
					return alreadyExistPrompt;
				}
				return END_OF_CONVERSATION;
			} else {
				context.getForWhom().sendRawMessage("�c�lLe nom de la r�gion dois faire moins de 16 caract�re.");
				context.getAllSessionData().put("noAsk", true);
				return this;
			}
			
		}

		@Override
		public String getPromptText(ConversationContext context) {
			if (!context.getAllSessionData().containsKey("noAsk")) {
				context.getForWhom().sendRawMessage("�a�lQuel nom voulez vous donner � votre r�gion ?");
				return "�cAnnulez � tous moment en �crivant \"cancel\"";
			}
			return "�6�lVeuillez r�essayer !";
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
				who.sendRawMessage("�e�lR�gion �7[�3"+answer+"�7] �e�lcr�e");
				RegionManager.instance.registerRegion(answer);
			} else if (isNo(answer)) {
				who.sendRawMessage("�c�lCr�ation de r�gion annul�e !");
			} else {
				context.getForWhom().sendRawMessage("�c�lVeuillez entr� une r�ponse valide [OUI/NON] !.");
				context.getAllSessionData().put("noAsk", true);
				return this;
			}
			return END_OF_CONVERSATION;
		}
		
		@Override
		public String getPromptText(ConversationContext context) {
			if (!context.getAllSessionData().containsKey("noAsk")) {
				context.getForWhom().sendRawMessage("�a�lUne r�gion nomm� "+""+" �xiste d�j�.\n Voulez-vous quand m�me la cr�e ?");
				return "�cAnnulez � tous moment en �crivant \"cancel\"";
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
