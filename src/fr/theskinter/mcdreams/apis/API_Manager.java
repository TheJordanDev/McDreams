package fr.theskinter.mcdreams.apis;

import fr.theskinter.mcdreams.McDreams;
import fr.theskinter.mcdreams.apis.citizens.CitizensAPI_GUI;
import fr.theskinter.mcdreams.utils.Autopia;

public class API_Manager {
	
	private McDreams plugin;
	
	public API_Manager(McDreams plugin) {
		this.plugin = plugin;
		if (McDreams.isEnabled("Citizens")) {
			citizens();
		}
		if (McDreams.isEnabled("ProtocolLib")) {
			protocolLib();
		}
	}
	
	private void citizens() {
		McDreams.instance.setCitizensGUI(new CitizensAPI_GUI());
		plugin.getServer().getPluginManager().registerEvents(plugin.getCitizensGUI().getList(), plugin);
		plugin.getServer().getPluginManager().registerEvents(plugin.getCitizensGUI().getInfos(), plugin);
		plugin.getServer().getPluginManager().registerEvents(plugin.getCitizensGUI().getSkins(), plugin);
	}
	
	private void protocolLib() {
		McDreams.instance.setAutopia(new Autopia());
	}
}
