package fr.theskinter.mcdreams.utils.filter;

import java.util.Comparator;

import org.bukkit.entity.Player;

public class PlayerSorter implements Comparator<Player> {
    @Override
    public int compare(Player e1, Player e2) {
        return e1.getName().compareToIgnoreCase( e2.getName() );
    }
}
