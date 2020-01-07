package fr.theskinter.mcdreams.schedulers;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.theskinter.mcdreams.objects.Portal;
import fr.theskinter.mcdreams.objects.parc.Attraction;
import fr.theskinter.mcdreams.objects.parc.Land;
import fr.theskinter.mcdreams.objects.parc.ParcManager;
import lombok.Getter;

public class SchedulerManager {

	public static SchedulerManager instance;
	@Getter PortalScheduler portalScheduler;
	
	public SchedulerManager(JavaPlugin plugin) {
		instance = this;
		this.portalScheduler = new PortalScheduler(); portalScheduler.runTaskTimer(plugin, 0L, 20L);
	}
		
	public class PortalScheduler extends BukkitRunnable {

		@Override
		public void run() {
			Iterator<Land> landsIte = ParcManager.instance.getLands().iterator();
			while (landsIte.hasNext()) {
				Land land = landsIte.next();
				Iterator<Attraction> attraIte = land.getAttractions().iterator();
				while (attraIte.hasNext()) {
					Attraction attraction = attraIte.next();
					Iterator<Portal> portalsIte = attraction.getPortals().iterator();
					while (portalsIte.hasNext()) {
						Portal portal = portalsIte.next();
						if (portal.isEnabled()) {
							if (portal.getTimer() > 0) {
								portal.setTimer(portal.getTimer()-1);
								Iterator<UUID> entitiesIte = portal.getEntityTimerID().iterator();
								while (entitiesIte.hasNext()) {
									UUID uuid = entitiesIte.next();
									Bukkit.getEntity(uuid).setCustomName("§e§l[§6§l"+portal.getTimer()+"§e§l]");
								}
							} else {
								Iterator<UUID> entitiesIte = portal.getEntityTimerID().iterator();
								while (entitiesIte.hasNext()) {
									UUID uuid = entitiesIte.next();
									Bukkit.getEntity(uuid).remove();
								}
								portal.disable(); portal.clear();
								attraction.getPortals().remove(portal);
								return;
							}
						} else {
							Iterator<UUID> entitiesIte = portal.getEntityTimerID().iterator();
							while (entitiesIte.hasNext()) {
								UUID uuid = entitiesIte.next();
								Bukkit.getEntity(uuid).setCustomName("§4§l[§c§lDESACTIVER§4§l]");
							}
						}
					}
				}
			}
		}
		
	}
 	
}
