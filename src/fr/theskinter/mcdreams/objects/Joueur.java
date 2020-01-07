package fr.theskinter.mcdreams.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.utils.FileUtils;
import fr.theskinter.mcdreams.utils.JoueurTempStates;
import fr.theskinter.mcdreams.utils.joueurs.JoueurManager;
import lombok.Getter;
import lombok.Setter;

public class Joueur {
	
	@Getter private UUID uuid;
	//SAVED
	@Getter @Setter private boolean god = true;
	@Getter @Setter private boolean saturation = true;
	@Getter @Setter private boolean chatNotified = true;
	@Getter @Setter private boolean chatMuted = false;
	@Getter @Setter private boolean place_break = false;
	@Getter @Setter private boolean use_fake_inv = true;
	@Getter @Setter private boolean can_edit_inv = false;
	@Getter @Setter private double money = 0D;
	@Getter @Setter private Inventory backpack;
	//NOT SAVED
	@Getter private List<JoueurTempStates> states = new ArrayList<JoueurTempStates>();
	
	public Joueur(UUID uuid) {
		this.uuid = uuid;
		this.backpack = Bukkit.createInventory(null, 5*9, "§3§lSac à dos de §6§l"+Bukkit.getOfflinePlayer(uuid).getName());
		JoueurManager.instance.getJoueurs().add(this);
	}
	
	public Joueur(Player player) {
		this.uuid = player.getUniqueId();
		this.backpack = Bukkit.createInventory(null, 5*9, "§3§lSac à dos de §6§l"+player.getName());
		JoueurManager.instance.getJoueurs().add(this);
	}
	
	public OfflinePlayer getOfflinePlayerIfHasPlayed() {
		OfflinePlayer player = Bukkit.getOfflinePlayer(getUuid());
		if (player.isOnline()) {
			return player;
		}
		if (player.hasPlayedBefore()) {
			return player;
		}
		return null;
	}
	
	public Player getPlayerIfOnline() {
		OfflinePlayer player = Bukkit.getOfflinePlayer(getUuid());
		if (player.hasPlayedBefore() && player.isOnline()) {
			return player.getPlayer();
		}
		return null;
	}
	
	public boolean is(JoueurTempStates state) {
		return getStates().contains(state);
	}
	
	public void setState(JoueurTempStates state,boolean is) {
		if (is) {
			getStates().add(state);
		} else {
			if (is(state)) {
				getStates().remove(state);
			}
		}
	}
	
	public static Joueur getJoueur(Player player) {
		return getJoueur(player.getUniqueId());
	}
	
	public static Joueur getJoueur(UUID uuid) {
		for (Joueur joueur : JoueurManager.instance.getJoueurs()) {
			if (joueur.getUuid().equals(uuid)) {
				return joueur;
			}
		}
		JoueurManager.instance.getJoueurs().add(new Joueur(uuid));
		return getJoueur(uuid);
	}
	
	public static void loadAll() {
		File pluginDir = new File(McDreams.instance.getDataFolder()+"/joueurs/");
		if (!pluginDir.exists()) { pluginDir.mkdirs(); }
		for (File file : pluginDir.listFiles()) {
			if (FilenameUtils.getExtension(file.getPath()).equals("json")) {
				Joueur joueur = JoueurManager.instance.getJsm().deserialize(FileUtils.load(file));
				if (doesJoueurExist(joueur.getUuid())) {
					JoueurManager.instance.getJoueurs().remove(getJoueur(joueur.getUuid()));
				}
				JoueurManager.instance.getJoueurs().add(joueur);
			}
		}
	}
	
	public static void saveAll() {
		for (Joueur joueur : JoueurManager.instance.getJoueurs()) {
			joueur.save();
		}
	}
	
	public void save() {
		File pluginDir = McDreams.instance.getDataFolder();
		File file = new File(pluginDir+"/joueurs/"+getUuid()+".json");
		file.getParentFile().mkdirs();
		FileUtils.save(file, JoueurManager.instance.getJsm().serialize(this));
	}
	
	public static boolean doesJoueurExist(Player player) {
		return doesJoueurExist(player.getUniqueId());
	}
	
	public static boolean doesJoueurExist(UUID uuid) {
		for (Joueur joueur : JoueurManager.instance.getJoueurs()) {
			if (joueur.getUuid().equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	
	public void reload() {
		File playerFile = new File(McDreams.instance.getDataFolder()+"/joueurs/"+getUuid()+".json");
		if (playerFile.exists()) {
			Joueur joueur = JoueurManager.instance.getJsm().deserialize(FileUtils.load(playerFile));
			if (doesJoueurExist(joueur.getUuid())) {
				JoueurManager.instance.getJoueurs().remove(getJoueur(joueur.getUuid()));
			}
			JoueurManager.instance.getJoueurs().add(joueur);
		}
	}
	
	
}
