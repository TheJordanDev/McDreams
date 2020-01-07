package fr.theskinter.mcdreams;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import fr.theskinter.mcdreams.apis.API_Manager;
import fr.theskinter.mcdreams.apis.citizens.CitizensAPI_GUI;
import fr.theskinter.mcdreams.commands.CMD_McDParc;
import fr.theskinter.mcdreams.commands.CMD_McDRegions;
import fr.theskinter.mcdreams.commands.CMD_McDWarp;
import fr.theskinter.mcdreams.commands.CMD_McDreams;
import fr.theskinter.mcdreams.commands.CMD_MeetAndGreet;
import fr.theskinter.mcdreams.guis.GUI_McDreams;
import fr.theskinter.mcdreams.guis.GUI_MeetAndGreet;
import fr.theskinter.mcdreams.guis.GUI_Regions;
import fr.theskinter.mcdreams.guis.GUI_Warps;
import fr.theskinter.mcdreams.listeners.JoueurEventListener;
import fr.theskinter.mcdreams.objects.Joueur;
import fr.theskinter.mcdreams.objects.RegionManager;
import fr.theskinter.mcdreams.objects.ScoreBoard;
import fr.theskinter.mcdreams.objects.parc.ParcManager;
import fr.theskinter.mcdreams.ping.PingListener;
import fr.theskinter.mcdreams.schedulers.SchedulerManager;
import fr.theskinter.mcdreams.utils.Skins;
import fr.theskinter.mcdreams.utils.TextCreator;
import fr.theskinter.mcdreams.utils.joueurs.JoueurEventCaller;
import fr.theskinter.mcdreams.utils.joueurs.JoueurManager;
import fr.theskinter.mcdreams.utils.prompts.PromptManager;
import lombok.Getter;
import lombok.Setter;

public class McDreams extends JavaPlugin implements Listener {
	
	public static McDreams instance;
	@Getter @Setter private boolean maintenance = true;
	
	@Getter private TextCreator textCreator;
	
 	@Getter private ScoreBoard scoreboard;
 	@Getter private Skins skins;
	
 	@Getter private PromptManager promptManager;
 	
	@Getter private GUI_McDreams mcDreamsGUI;
	@Getter private GUI_Warps warpGUI;
	@Getter private GUI_MeetAndGreet meetAndGreetGUI;
	@Getter @Setter private CitizensAPI_GUI citizensGUI;
	@Getter private GUI_Regions regionGUI;
	
	
	@Override public void onEnable() { instance = this; start(); }
	
	private void start() {
		if (isEnabled("WorldEdit")) {
			this.mcDreamsGUI = new GUI_McDreams(instance);
			this.meetAndGreetGUI = new GUI_MeetAndGreet(instance);
			this.regionGUI = new GUI_Regions(instance);
			this.warpGUI = new GUI_Warps(instance);
			this.promptManager = new PromptManager();
			this.scoreboard = new ScoreBoard();
			this.textCreator = new TextCreator();
			this.skins = new Skins(instance);
			new ParcManager(); new RegionManager();
			new JoueurManager(); new API_Manager(this);
			new SchedulerManager(instance);
			scoreboard.start();
			Joueur.loadAll();
			for (OfflinePlayer offPlayer : Bukkit.getOfflinePlayers()) { if (!Joueur.doesJoueurExist(offPlayer.getUniqueId())) { new Joueur(offPlayer.getUniqueId()); } }
			registerListeners();
			registerCommands();
		} else {
			Bukkit.getLogger().log(Level.WARNING, "Le plugin [WORLDEDIT] n'existe pas, McDreams ne peut² fonctionner sans ce dernier !");
		}
	}

	@Override
	public void onDisable() {
		Joueur.saveAll();
		scoreboard.stop();
		/*this.autopia.hidePointsToPlayer((Player[])Bukkit.getOnlinePlayers().toArray());
		this.autopia.getShowedPointsID().clear();*/
	}
	
	public void registerListeners() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new JoueurEventCaller(), this);
		getServer().getPluginManager().registerEvents(new JoueurEventListener(), this);
	}
	
	public void registerCommands() {
		getCommand("mcwarp").setExecutor(new CMD_McDWarp()); getCommand("mcwarp").setTabCompleter(new CMD_McDWarp());
		getCommand("mcdreams").setExecutor(new CMD_McDreams()); getCommand("mcdreams").setTabCompleter(new CMD_McDreams());
		getCommand("meetandgreet").setExecutor(new CMD_MeetAndGreet()); getCommand("meetandgreet").setTabCompleter(new CMD_MeetAndGreet());
		getCommand("mcregions").setExecutor(new CMD_McDRegions()); getCommand("mcregions").setTabCompleter(new CMD_McDRegions());
		getCommand("mcparc").setExecutor(new CMD_McDParc()); getCommand("mcparc").setTabCompleter(new CMD_McDParc());
	}
	
	// ======= PING DETECTOR ======= //
	@EventHandler
	public void serverPing(ServerListPingEvent event) throws Exception { new PingListener(); }
	
	public static boolean isEnabled(String plugin) {
		Plugin plug = Bukkit.getServer().getPluginManager().getPlugin(plugin);
		if (plug == null) {
			return false;
		}
		return plug.isEnabled();
	}
    
}
