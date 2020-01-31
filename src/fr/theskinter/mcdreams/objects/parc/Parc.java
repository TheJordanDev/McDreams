package fr.theskinter.mcdreams.objects.parc;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.io.FilenameUtils;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.objects.Portal;
import fr.theskinter.mcdreams.objects.managers.ParcManager;
import fr.theskinter.mcdreams.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;

public class Parc {
	
	public static Parc instance;
	@Getter @Setter private Autopia autopia;
	@Getter @Setter private LinkedList<Land> lands = new LinkedList<Land>();
	@Getter private LinkedList<Hotel> hotels = new LinkedList<Hotel>();

	public Parc() {
		instance = this;
	}
	
	public boolean hasLand(String name) {
		for (Land land : getLands()) {
			if (land.getName().equalsIgnoreCase(name)) { return true; }
		} return false;
	}
	
	public Land getLandByName(String name) {
		if (hasLand(name)) { for (Land land : getLands()) {
			if (land.getName().equals(name)) { return land;} 
		} }
		return null;
	}
	
	public void registerLand(String name) {
		if (hasLand(name)) { return; }
		else { getLands().add(new Land(name)); }
	}
	
	public boolean hasHotel(String name) {
		for (Hotel hotel : getHotels()) {
			if (hotel.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public Hotel getHotelByName(String name) {
		for (Hotel hotel : getHotels()) {
			if (hotel.getName().equalsIgnoreCase(name)) {
				return hotel;
			}
		}
		return null;
	}
	
	public void registerHotel(String name,int rooms) {
		Hotel hotel = new Hotel(name, rooms);
		for (int i = 1;i<=rooms;i++) {
			hotel.createChamber(i);
		}
		getHotels().add(hotel);
	}
	
	public static void load() {
		File pluginDir = McDreams.instance.getDataFolder();
		if (!pluginDir.exists()) { pluginDir.mkdirs(); }
		for (File file : pluginDir.listFiles()) {
			if (FilenameUtils.getBaseName(file.getPath()).equals("parc")) {
				if (FilenameUtils.getExtension(file.getPath()).equals("json")) {
					Parc.instance = new Parc();
					ParcManager.instance.getPsm().deserialize(FileUtils.load(file));
				}
			}
		}
	}
	
	public void save() {
		File pluginDir = McDreams.instance.getDataFolder();
		File file = new File(pluginDir+"/parc.json");
		file.getParentFile().mkdirs();
		FileUtils.save(file, ParcManager.instance.getPsm().serialize(instance));
	}
	
	public void clearAllPortals() {
		Iterator<Land> landsIte = Parc.instance.getLands().iterator();
		while (landsIte.hasNext()) {
			Land land = landsIte.next();
			Iterator<Attraction> attraIte = land.getAttractions().iterator();
			while (attraIte.hasNext()) {
				Attraction attraction = attraIte.next();
				Iterator<Portal> portalsIte = attraction.getPortals().iterator();
				while (portalsIte.hasNext()) {
					Portal portal = portalsIte.next();
					portal.clear();
					continue;
				}
			}
		}
	}
	
}
