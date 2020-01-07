package fr.theskinter.mcdreams.objects.parc;

import java.util.LinkedList;

import fr.theskinter.mcdreams.objects.Autopia;
import lombok.Getter;
import lombok.Setter;

public class ParcManager {

	public static ParcManager instance;
	@Getter @Setter private Autopia autopia;
	@Getter private LinkedList<Land> lands = new LinkedList<Land>();
	
	public ParcManager() {
		instance = this;
	}
	
	public boolean hasLand(String name) {
		for (Land land : getLands()) {
			if (land.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public Land getLandByName(String name) {
		if (hasLand(name)) {
			for (Land land : getLands()) {
				if (land.getName().equals(name)) {
					return land;
				}
			}
		}
		return null;
	}
	
	public void registerLand(String name) {
		if (hasLand(name)) {
			return;
		} else {
			getLands().add(new Land(name));
		}
	}
	
}
