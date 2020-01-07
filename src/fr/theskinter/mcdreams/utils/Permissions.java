package fr.theskinter.mcdreams.utils;

import lombok.Getter;

public enum Permissions {

	BREAK("mcdreams."),
	PLACE("mcdreams."),
	BUILD("mcdreams."),
	BYPASS_MUTE("mcdreams."),
	NEVER_STARVE("mcdreams."),
	INVICIBLE("mcdreams.");

	@Getter private String permission;
	
	private Permissions(String permission) {
		this.permission = permission;
	}
	
}
