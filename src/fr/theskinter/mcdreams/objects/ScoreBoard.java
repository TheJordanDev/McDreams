package fr.theskinter.mcdreams.objects;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import fr.theskinter.mcdreams.McDreams;
import lombok.Getter;

public class ScoreBoard {
	
	@Getter private final Creator creator;
	@Getter private final Refresh refresh;
	
	public ScoreBoard() {
		this.creator = new Creator();
		this.refresh = new Refresh();
	}
	
	public void start() {
		refresh.runTaskTimer(McDreams.instance, 0L, 30L);
	}
	
	public void stop() {
		refresh.cancel();
	}
	
	public class Refresh extends BukkitRunnable {

		@Override
		public void run() {
			for (Player player : Bukkit.getOnlinePlayers()) {
				getCreator().create(player);
			}
		}
		
	}

	public class Creator {

		public Creator() { }

		public Scoreboard create(Player player) {
			Scoreboard scoreboard = player.getScoreboard();
			Objective objective;
			if (scoreboard.getObjective("mainscore") == null) {
				objective = scoreboard.registerNewObjective("mainscore", "dummy");
			} else {
				objective = scoreboard.getObjective("mainscore");
			}
			for (String entry : scoreboard.getEntries()) {
				scoreboard.resetScores(entry);
			}
			Joueur joueur = Joueur.getJoueur(player);
			Time heure = Time.getNow();
			objective.setDisplayName("[§3§lMc§b§lDreams§r]");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			Score sepa1 = objective.getScore("§f§l"+StringUtils.repeat("═",10)+"§1"); sepa1.setScore(10);
			Score money = objective.getScore("§6Money §7: §6"+joueur.getMoney()); money.setScore(9);
			Score nbJoueur = objective.getScore("§6En Ligne §7: §6"+Bukkit.getOnlinePlayers().size()); nbJoueur.setScore(8);
			Score espace1 = objective.getScore("§1"); espace1.setScore(7);
			Score uniquePlayer = objective.getScore("§7Unique Player : §6"+Bukkit.getOfflinePlayers().length); uniquePlayer.setScore(6);
			Score time = objective.getScore("§7Heure : §6"+Time.repairTime(heure.getHour())+":"+Time.repairTime(heure.getMinutes())); time.setScore(5);
			Score sepa2 = objective.getScore("§f§l"+StringUtils.repeat("═",10)); sepa2.setScore(1);
			return scoreboard;
		}
		
		
	}
/*package fr.redcraft.mp.scoreboard;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.redcraft.mp.Essentials;
import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerListHeaderFooter;

public class TablistUtils implements Runnable {

    @Override
    public void run() {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
   Object header = new ChatComponentText("§7[§f{}§7--§f{§3-§cMin§fParc §fResort§3-§f}§7--§f{}§7]");
       // Object header = new ChatComponentText("§7[§f{}§7--§f{§3-§eE§dL§cE§bC§aT§9R§6O§5P§4A§3R§2C§7}§7--§f{}§7]");

        Object footer = new ChatComponentText("En ligne : §a" + Essentials.list.size());
        try {
            Field a = packet.getClass().getDeclaredField("header");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("footer");
            b.setAccessible(true);
            a.set(packet, header);
            b.set(packet, footer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }    
        if (Bukkit.getOnlinePlayers() != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
               
           
                
            
            
            }
        }
    }

}*/
}
