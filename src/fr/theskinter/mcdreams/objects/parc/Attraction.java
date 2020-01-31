package fr.theskinter.mcdreams.objects.parc;

import java.util.Comparator;
import java.util.LinkedList;

import org.bukkit.Location;

import fr.theskinter.mcdreams.objects.Portal;
import lombok.Getter;
import lombok.Setter;

public class Attraction {

	public enum STATE { OPEN("§a§l"),CLOSE("§c§l"),MAINTENANCE("§6§l"); 
		
		@Getter private String couleur;
		
		private STATE(String couleur) {
			this.couleur = couleur;
		}
		
	    public STATE next() {
	        return STATE.values()[(this.ordinal()+1) % STATE.values().length];
	    }
		
	    public static STATE forName(String name) {
	    	for (STATE value : values()) { if (value.name().equals(name)) { return value; } } return null;
	    }
	    
	}
	
	@Getter public static AttractionSorter sorter = new AttractionSorter();
	
	@Getter private LinkedList<Portal> portals = new LinkedList<Portal>();
	
	@Getter @Setter private String name = "";
	@Getter @Setter private Location warpLocation; public boolean canBeWarpedAt() { return (getWarpLocation() != null); }
	@Getter @Setter private boolean arePortalUsable = true;
	@Getter @Setter STATE state = STATE.CLOSE;
	
	public Attraction(String name) {
		this.name = name;
	}
	
	public static class AttractionSorter implements Comparator<Attraction> {
		@Override public int compare(Attraction o1, Attraction o2) { return o1.getName().compareToIgnoreCase(o2.getName()); }
	}
	
}
