package fr.theskinter.mcdreams.objects.parc;

import java.util.Comparator;
import java.util.LinkedList;

import lombok.Getter;

public class Land {

	public static LandSorter sorter = new LandSorter();
	
	@Getter private String name;
	@Getter private LinkedList<Attraction> attractions = new LinkedList<Attraction>();
	
	public Land(String name) {
		this.name = name;
	}
	
	public boolean hasAttractionWichCanBeWarped() {
		for (Attraction attra : getAttractions()) {
			if (attra.canBeWarpedAt()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasAttraction(String name) {
		for (Attraction attra : getAttractions()) {
			if (attra.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public Attraction getAttractionByName(String name) {
		if (hasAttraction(name)) {
			for (Attraction attra : getAttractions()) {
				if (attra.getName().equals(name)) {
					return attra;
				}
			}
		}
		return null;
	}
	
	public Attraction registerAttraction(String name) {
		if (hasAttraction(name)) {
			return null;
		} else {
			getAttractions().add(new Attraction(name));
			return getAttractionByName(name);
		}
	}
	
	
	public static class LandSorter implements Comparator<Land> {
		@Override public int compare(Land o1, Land o2) { return o1.getName().compareToIgnoreCase(o2.getName()); }
	}

}
