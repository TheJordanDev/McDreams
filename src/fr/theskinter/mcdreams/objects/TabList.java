package fr.theskinter.mcdreams.objects;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;

public class TabList extends BukkitRunnable {

	@Getter private String HEADER;
	@Getter private String FOOTER;
	
	public TabList(String HEADER,String FOOTER) {
		this.HEADER = HEADER;
		this.FOOTER = FOOTER;
	}
	
	@Override
	public void run() {
		
	}

}
