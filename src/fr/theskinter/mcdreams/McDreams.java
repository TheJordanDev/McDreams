package fr.theskinter.mcdreams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import fr.theskinter.mcdreams.apis.API_Manager;
import fr.theskinter.mcdreams.apis.citizens.CitizensAPI_GUI;
import fr.theskinter.mcdreams.commands.CMD_McDreams;
import fr.theskinter.mcdreams.commands.CMD_MeetAndGreet;
import fr.theskinter.mcdreams.guis.GUI_McDreams;
import fr.theskinter.mcdreams.guis.GUI_MeetAndGreet;
import fr.theskinter.mcdreams.listeners.JoueurEventListener;
import fr.theskinter.mcdreams.ping.PingListener;
import fr.theskinter.mcdreams.utils.ScoreBoard;
import fr.theskinter.mcdreams.utils.Skins;
import fr.theskinter.mcdreams.utils.TextCreator;
import fr.theskinter.mcdreams.utils.joueurs.Joueur;
import fr.theskinter.mcdreams.utils.joueurs.JoueurEventCaller;
import fr.theskinter.mcdreams.utils.joueurs.JoueurManager;
import lombok.Getter;
import lombok.Setter;

public class McDreams extends JavaPlugin implements Listener {
	
	public static McDreams instance;
	@Getter private List<Joueur> joueurs = new ArrayList<>();
	@Getter @Setter private boolean maintenance = true;
	@Getter private TextCreator textCreator;
	@Getter private CitizensAPI_GUI citizensGUI;
	@Getter private GUI_McDreams mcDreamsGUI;
	@Getter private GUI_MeetAndGreet meetAndGreetGUI;
 	@Getter private JoueurManager joueurManager;
 	@Getter private ScoreBoard scoreboard;
 	@Getter private Skins skins;
	
	@Override	
	public void onEnable() {
		instance = this;
		start();
	}
	
	private void start() {
		registerListeners();
		registerCommands();
		this.mcDreamsGUI = new GUI_McDreams(instance);
		this.meetAndGreetGUI = new GUI_MeetAndGreet(instance);
		this.joueurManager = new JoueurManager();
		this.scoreboard = new ScoreBoard();
		this.textCreator = new TextCreator();
		this.skins = new Skins(instance);
		scoreboard.start();
		Joueur.loadAll();
		if (isEnabled("Citizens")) {
			this.citizensGUI = new CitizensAPI_GUI();
			new API_Manager(this);
		}
	}

	@Override
	public void onDisable() {
		Joueur.saveAll();
	}
	
	public void registerListeners() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new JoueurEventCaller(), this);
		getServer().getPluginManager().registerEvents(new JoueurEventListener(), this);
	}
	
	public void registerCommands() {
		getCommand("mcdreams").setExecutor(new CMD_McDreams());
		getCommand("mcdreams").setTabCompleter(new CMD_McDreams());
		getCommand("meetandgreet").setExecutor(new CMD_MeetAndGreet());
		getCommand("meetandgreet").setTabCompleter(new CMD_MeetAndGreet());
	}
	
	// ======= PING DETECTOR ======= //
	@EventHandler
	public void serverPing(ServerListPingEvent event) throws Exception {
		new PingListener();
	}
	
	public static boolean isEnabled(String plugin) {
		Plugin plug = Bukkit.getServer().getPluginManager().getPlugin(plugin);
		if (plug == null) {
			return false;
		}
		return plug.isEnabled();
	}
    
}
