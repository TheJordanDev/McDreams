package fr.theskinter.mcdreams.objects.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import fr.theskinter.mcdreams.objects.parc.Attraction;
import fr.theskinter.mcdreams.objects.parc.Chambre;
import fr.theskinter.mcdreams.objects.parc.Hotel;
import fr.theskinter.mcdreams.objects.parc.Land;
import fr.theskinter.mcdreams.objects.parc.Parc;
import fr.theskinter.mcdreams.utils.Utils;
import lombok.Getter;

public class ParcManager {

	public static ParcManager instance;
	@Getter private ParcSerializationManager psm;
	
	public ParcManager() {
		instance = this;
		this.psm = new ParcSerializationManager();
		new Parc();
	}
	
	public class ParcSerializationManager {
		
		@Getter private Gson gson;
		
		public ParcSerializationManager() {
			this.gson = createGsonInstance();
		}
		
		private Gson createGsonInstance() {
			return new GsonBuilder()
					.registerTypeHierarchyAdapter(Parc.class, new ParcTypeAdapter())
					.setPrettyPrinting()
					.serializeNulls()
					.disableHtmlEscaping()
					.create();
		}
		
		public String serialize(Parc parc) {
			return this.gson.toJson(parc);
		}
		
		public Parc deserialize(String json) {
			return this.gson.fromJson(json, Parc.class);
		}
	}
	
	public class ParcTypeAdapter extends TypeAdapter<Parc> {

		@Override
		public Parc read(JsonReader reader) throws IOException {
			reader.beginObject();
			List<Land> lands = new ArrayList<Land>();
			
			String currLandName = "";
			Land.STATE currLandState = Land.STATE.CLOSE;
			
			String currAttraName = "";
			Attraction.STATE currAttraState = Attraction.STATE.CLOSE;
			Location currAttraWarpLoc = null;
			
			String worldName = "";
			double x = 0;
			double y = 0;
			double z = 0;
			
			List<Attraction> attractions = new ArrayList<>();
			
			String hotel_name = "";
			Integer hotel_max_room_num = 0;
			
			List<Hotel> hotels = new ArrayList<>();
			
			Integer room_id = 0;
			UUID owner = null;
			
			List<Chambre> chambres = new ArrayList<>();

			while (reader.hasNext()) {
				switch (reader.nextName()) {
					case "lands":
						reader.beginArray();
						while(reader.hasNext()) {
							reader.beginObject();
							while (reader.hasNext()) {
								switch (reader.nextName()) {
									case "name":
										currLandName = reader.nextString(); break;
									case "state":
										String landState = reader.nextString();
										if (Land.STATE.forName(landState) != null) { currLandState = Land.STATE.valueOf(landState.toUpperCase()); }
										break;
									case "attractions":
										reader.beginArray();
										while(reader.hasNext()) {
											reader.beginObject();
											while (reader.hasNext()) {
												switch (reader.nextName()) {	
													case "name":
														currAttraName = reader.nextString(); break;
													case "state":
														String attState = reader.nextString();
														if (Attraction.STATE.forName(attState) != null) { currAttraState = Attraction.STATE.valueOf(attState.toUpperCase()); }
														break;
													case "location":
														reader.beginObject();
														while (reader.hasNext()) {
															switch (reader.nextName()) {
																case "x":
																	x = reader.nextDouble(); break;
																case "y":
																	y = reader.nextDouble(); break;
																case "z":
																	z = reader.nextDouble(); break;
																case "world":
																	worldName = reader.nextString(); break;
															}
															if (Bukkit.getWorld(worldName) != null) {
																currAttraWarpLoc = new Location(Bukkit.getWorld(worldName), x, y, z);
															}
														}
														reader.endObject();
														break;
												}
											}
											Attraction attraction = new Attraction(currAttraName);
											attraction.setState(currAttraState);
											if (currAttraWarpLoc != null) {
												attraction.setWarpLocation(currAttraWarpLoc);
											} 
											attractions.add(attraction);
											reader.endObject();
										}
										reader.endArray();
										break;
								}
							}
							Land land = new Land(currLandName);
							land.setState(currLandState);
							for (Attraction at : attractions) {
								land.getAttractions().add(at);
							}
							lands.add(land);
							reader.endObject();
						}
						reader.endArray();
						break;
					case "hotels":
						reader.beginArray();
						while(reader.hasNext()) {
							reader.beginObject();
							while (reader.hasNext()) {
								switch (reader.nextName()) {
									case "name":
										hotel_name = reader.nextString(); break;
									case "max_rooms":
										hotel_max_room_num = reader.nextInt();
										break;
									case "chambres":
										reader.beginArray();
										while(reader.hasNext()) {
											reader.beginObject();
											while (reader.hasNext()) {
												switch (reader.nextName()) {	
													case "id":
														room_id = reader.nextInt(); break;
													case "owner":
														String ownUUID = reader.nextString();
														if (Utils.fromString(ownUUID) != null) { owner = Utils.fromString(ownUUID); }
														break;
												}
											}
											Chambre chambre = new Chambre(room_id);
											if (owner != null) { chambre.setOwner(owner); }
											chambres.add(chambre);
											reader.endObject();
										}
										reader.endArray();
										break;
								}
							}
							Hotel hotel = new Hotel(hotel_name,hotel_max_room_num);
							for (Chambre cb : chambres) {
								hotel.getChambres().add(cb);
							}
							hotels.add(hotel);
							reader.endObject();
						}
						reader.endArray();
						break;	
				}
			}
			reader.endObject();
			Parc parc = Parc.instance;
			for (Land land : lands) {
				parc.getLands().add(land);
			}
			for (Hotel hotel : hotels) {
				parc.getHotels().add(hotel);
			}
			return parc;
		}

		@Override
		public void write(JsonWriter writer, Parc parc) throws IOException {
			writer.beginObject();
			
			writer.name("lands").beginArray();
				for (int i = 0; i<parc.getLands().size();i++) {
					Land land = parc.getLands().get(i);
					writer.beginObject();
					writer.name("name").value(land.getName());
					writer.name("state").value(land.getState().toString());
					writer.name("attractions").beginArray();
						for (int j = 0; j<land.getAttractions().size();j++) {
							Attraction attraction = land.getAttractions().get(j);
							writer.beginObject();
							writer.name("name").value(attraction.getName());
							writer.name("state").value(attraction.getState().toString());
							if (attraction.getWarpLocation() != null) {
								writer.name("location").beginObject();
								writer.name("x").value(attraction.getWarpLocation().getX());
								writer.name("y").value(attraction.getWarpLocation().getY());
								writer.name("z").value(attraction.getWarpLocation().getZ());
								writer.name("world").value(attraction.getWarpLocation().getWorld().getName());
								writer.endObject();
							}
							writer.endObject();
						}
					writer.endArray();
					writer.endObject();
				}
			writer.endArray();
				
			writer.name("hotels").beginArray();
				for (int i = 0; i<parc.getHotels().size();i++) {
					Hotel hotel = parc.getHotels().get(i);
					writer.beginObject();
					writer.name("name").value(hotel.getName());
					writer.name("max_rooms").value(hotel.getRoom_number());
					writer.name("chambres").beginArray();
						for (int j = 0; j<hotel.getChambres().size();j++) {
							Chambre chambre = hotel.getChambres().get(j);
							writer.beginObject();
							writer.name("id").value(chambre.getNumero());
							if (chambre.getOwner() != null) {
								writer.name("owner").value(chambre.getOwner().toString());
							}
							writer.endObject();
						}
					writer.endArray();
					writer.endObject();
				}
			writer.endArray();
			
			writer.endObject();
		}
		
	}
	
}
