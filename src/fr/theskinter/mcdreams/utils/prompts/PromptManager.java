package fr.theskinter.mcdreams.utils.prompts;

import lombok.Getter;

public class PromptManager {

	@Getter private RegionPrompts regionPrompts;
	
	public PromptManager() {
		this.regionPrompts = new RegionPrompts();
	}

}
