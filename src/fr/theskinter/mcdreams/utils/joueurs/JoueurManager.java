package fr.theskinter.mcdreams.utils.joueurs;

import java.io.IOException;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import fr.theskinter.mcdreams.objects.Joueur;
import lombok.Getter;

public class JoueurManager {
	
	public static JoueurManager instance;
	@Getter private JoueurSerializationManager jsm;
	
	public JoueurManager() {
		instance = this;
		this.jsm = new JoueurSerializationManager();
	}
	
	public class JoueurSerializationManager {
		
		@Getter private Gson gson;
		
		public JoueurSerializationManager() {
			this.gson = createGsonInstance();
		}
		
		private Gson createGsonInstance() {
			return new GsonBuilder()
					.registerTypeHierarchyAdapter(Joueur.class, new JoueurTypeAdapter())
					.setPrettyPrinting()
					.serializeNulls()
					.disableHtmlEscaping()
					.create();
		}
		
		public String serialize(Joueur joueur) {
			return this.gson.toJson(joueur);
		}
		
		public Joueur deserialize(String json) {
			return this.gson.fromJson(json, Joueur.class);
		}
	}
	
	public class JoueurTypeAdapter extends TypeAdapter<Joueur> {

		@Override
		public Joueur read(JsonReader reader) throws IOException {
			reader.beginObject();
			UUID uuid = null;
			Double money = 0.0D;
			Boolean god = false;
			Boolean saturation = false;
			Boolean muted = false;
			Boolean break_place = false;
			Boolean edit_inv = false;
			while (reader.hasNext()) {
				switch (reader.nextName()) {
					case "uuid":
						uuid = UUID.fromString(reader.nextString());
						break;
					case "money":
						money = reader.nextDouble();
						break;
					case "god":
						god = reader.nextBoolean();
						break;
					case "saturation":
						saturation = reader.nextBoolean();
						break;
					case "muted":
						muted = reader.nextBoolean();
						break;
					case "break_place":
						break_place = reader.nextBoolean();
						break;
					case "edit_inv":
						edit_inv = reader.nextBoolean();
						break;
				}
			}
			reader.endObject();
			Joueur joueur = new Joueur(uuid);
			joueur.setMoney((double)money);
			joueur.setGod(god);
			joueur.setChatMuted(muted);
			joueur.setPlace_break(break_place);
			joueur.setEdit_inv(edit_inv);
			joueur.setSaturation(saturation);
			return joueur;
		}

		@Override
		public void write(JsonWriter writer, Joueur joueur) throws IOException {
			writer.beginObject();
			writer.name("uuid").value(joueur.getUuid().toString());
			writer.name("money").value(joueur.getMoney());
			writer.name("god").value(joueur.isGod());
			writer.name("saturation").value(joueur.isSaturation());
			writer.name("muted").value(joueur.isChatMuted());
			writer.name("break_place").value(joueur.isPlace_break());
			writer.name("edit_inv").value(joueur.isEdit_inv());
			writer.endObject();
		}
		
	}
}
