package fr.theskinter.mcdreams.utils.joueurs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.guis.GUI_JoueurInventaire;
import fr.theskinter.mcdreams.objects.Joueur;
import fr.theskinter.mcdreams.utils.creators.ItemCreator;
import lombok.Getter;

public class JoueurManager {
	
	public static JoueurManager instance;
	@Getter private JoueurSerializationManager jsm;
	@Getter private GUI_JoueurInventaire GUIJoueurInventaire;
	@Getter private List<Joueur> joueurs = new ArrayList<>();
	@Getter private JoueurSorter sorter = new JoueurSorter();
	
	public JoueurManager() {
		instance = this;
		this.jsm = new JoueurSerializationManager();
		this.GUIJoueurInventaire = new GUI_JoueurInventaire(McDreams.instance);
	}
	
	public class JoueurSorter implements Comparator<Joueur> {
	    @Override
	    public int compare(Joueur e1, Joueur e2) {
	    	boolean e1IsOnline = e1.getOfflinePlayerIfHasPlayed().isOnline();
	    	boolean e2IsOnline = e2.getOfflinePlayerIfHasPlayed().isOnline();
	    	if (e1IsOnline && e2IsOnline) {
	    		return e1.getOfflinePlayerIfHasPlayed().getName().compareToIgnoreCase(e2.getOfflinePlayerIfHasPlayed().getName());
	    	} else {
	    		return (-1*Boolean.compare(e1IsOnline, e2IsOnline));
	    	}
	    }
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
			
			String currItemName = null;
			Material currItemMaterial = Material.AIR;
			byte currItemByte =(byte)0;
			int currItemSlot = -1;
			int currItemAmount = 1;
			
			Map<Integer,ItemStack> items = new HashMap<Integer,ItemStack>();
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
					case "use_fake_inv":
						edit_inv = reader.nextBoolean();
						break;
					case "inventaire":
						reader.beginArray();
						while(reader.hasNext()) {
							reader.beginObject();
							while (reader.hasNext()) {
								switch (reader.nextName()) {
									case "name":
										currItemName = reader.nextString(); break;
									case "material":
										currItemMaterial = Material.valueOf(reader.nextString().toUpperCase()); break;
									case "amount": 
										currItemAmount = reader.nextInt(); break;
									case "byte": 
										currItemByte = (byte)reader.nextInt(); break;
									case "slot":
										currItemSlot = reader.nextInt(); break;
								}
								items.put(currItemSlot, new ItemCreator().setName(currItemName).setMaterial(currItemMaterial).setAmount(currItemAmount).setByte(currItemByte).build());
							}
							
							reader.endObject();
							
						}
						reader.endArray();
						break;
				}
			}
			reader.endObject();
			
			Joueur joueur;
			if (Joueur.doesJoueurExist(uuid)) {
				joueur = Joueur.getJoueur(uuid);
			} else {
				joueur = new Joueur(uuid);
			}
			joueur.setMoney((double)money);
			joueur.setGod(god);
			joueur.setChatMuted(muted);
			joueur.setPlace_break(break_place);
			joueur.setUse_fake_inv(edit_inv);
			joueur.setSaturation(saturation);
			for (Entry<Integer,ItemStack> entry : items.entrySet()) {
				if (entry.getKey() != -1) {
					joueur.getBackpack().setItem(entry.getKey(), entry.getValue());
				}
			}
			return joueur;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void write(JsonWriter writer, Joueur joueur) throws IOException {
			writer.beginObject();
			writer.name("uuid").value(joueur.getUuid().toString());
			writer.name("money").value(joueur.getMoney());
			writer.name("god").value(joueur.isGod());
			writer.name("saturation").value(joueur.isSaturation());
			writer.name("muted").value(joueur.isChatMuted());
			writer.name("break_place").value(joueur.isPlace_break());
			writer.name("use_fake_inv").value(joueur.isUse_fake_inv());
			writer.name("inventaire").beginArray();
				for (int i = 0; i<joueur.getBackpack().getSize();i++) {
					if (joueur.getBackpack().getItem(i) != null) {
						if (joueur.getBackpack().getItem(i).getType() != Material.AIR) {
							ItemStack item = joueur.getBackpack().getItem(i);
							writer.beginObject();
							if (item.getItemMeta().hasDisplayName()) {
								writer.name("name").value(item.getItemMeta().getDisplayName());
							}
							writer.name("material").value(item.getType().name().toUpperCase());
							writer.name("amount").value(item.getAmount());
							writer.name("byte").value((int)item.getData().getData());
							writer.name("slot").value(i);
							writer.endObject();
						}
					}
				}
			writer.endArray();
			writer.endObject();
		}
		
	}
}
