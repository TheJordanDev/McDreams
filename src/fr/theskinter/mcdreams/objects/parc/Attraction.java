package fr.theskinter.mcdreams.objects.parc;

import java.util.Comparator;
import java.util.LinkedList;

import org.bukkit.Location;

import fr.theskinter.mcdreams.objects.Portal;
import lombok.Getter;
import lombok.Setter;

public class Attraction {

	@Getter public static AttractionSorter sorter = new AttractionSorter();
	
	@Getter private String name = "";
	@Getter @Setter private Location warpLocation; public boolean canBeWarpedAt() { return (getWarpLocation() != null); }
	@Getter private LinkedList<Portal> portals = new LinkedList<Portal>(); 
	
	public Attraction(String name) {
		this.name = name;
	}
	
	public static class AttractionSorter implements Comparator<Attraction> {
		@Override public int compare(Attraction o1, Attraction o2) { return o1.getName().compareToIgnoreCase(o2.getName()); }
	}
	
}
