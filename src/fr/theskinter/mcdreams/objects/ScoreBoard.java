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
				player.setScoreboard(getCreator().create(player));
			}
		}
		
	}

	public class Creator {

		private Scoreboard scoreboard;
		private Objective objective;
		
		public Creator() {
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		}

		public Scoreboard create(Player player) {
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
			Score time = objective.getScore("§7Heure : §6"+Time.repairTime(heure.getHour())+":"+Time.repairTime(heure.getMinutes())); time.setScore(6);
			Score sepa2 = objective.getScore("§f§l"+StringUtils.repeat("═",10)); sepa2.setScore(1);
			return scoreboard;
		}
		
		
	}
	
}
