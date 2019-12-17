package fr.theskinter.mcdreams.apis;

import fr.theskinter.mcdreams.McDreams;

public class API_Manager {
	
	private McDreams plugin;
	
	public API_Manager(McDreams plugin) {
		this.plugin = plugin;
		if (McDreams.isEnabled("Citizens")) {
			citizens();
		}
	}
	
	private void citizens() {
		plugin.getServer().getPluginManager().registerEvents(plugin.getCitizensGUI().getList(), plugin);
		plugin.getServer().getPluginManager().registerEvents(plugin.getCitizensGUI().getInfos(), plugin);
		plugin.getServer().getPluginManager().registerEvents(plugin.getCitizensGUI().getSkins(), plugin);
	}
}
