package fr.theskinter.mcdreams.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

import lombok.Getter;

public class RegionManager {

	public static RegionManager instance;
	@Getter private List<Region> regions = new ArrayList<>();
	
	public RegionManager() {
		instance = this;
	}
	
	public List<Region> getRegionByName(String name) {
		List<Region> returnes = new ArrayList<>();
		for (Region region : regions) {
			if (region.getName().equalsIgnoreCase(name)) {
				returnes.add(region);
			}
		}
		return returnes;
	}
	
	public List<Region> getRegionIn(Location loc) {
		List<Region> regions = new ArrayList<>();
		for (Region region : getRegions()) {
			if (region.isIn(loc)) {
				regions.add(region);
			}
		}
		return regions;
	}
	
	public Region getRegionByID(UUID id) {
		for (Region region : regions) {
			if (region.getId().equals(id)) {
				return region;
			}
		}
		return null;
	}
	
	public UUID registerRegion(String name) {
		Region region = new Region(name);
		return region.getId();
	}
	
	public void addRegion(Region region) {
		getRegions().add(region);
	}
	
	public class Region {
		
		@Getter private UUID id;
		@Getter private String name;
		@Getter private List<Cuboid> zones = new ArrayList<Cuboid>();
		
		public Region(String name) {
			this.id = UUID.randomUUID();
			this.name = name;
			regions.add(this);
		}
		
		public void addZone(Cuboid cuboid) {
			zones.add(cuboid);
		}
		
		public boolean isIn(Location location) {
			if (zones.isEmpty()) return false;
			for (Cuboid cuboid : zones) {
				if (cuboid.isIn(location)) {
					return true;
				}
			}
			return false;
		}
		
		public List<Cuboid> getInZones(Location loc) {
			List<Cuboid> inZones = new ArrayList<>();
			for (Cuboid cuboid : zones) {
				if (cuboid.isIn(loc)) {
					inZones.add(cuboid);
				}
			}
			return inZones;
		}
		
	}
	
}
