package fr.theskinter.mcdreams.utils.filter;

import java.util.Comparator;

import net.citizensnpcs.api.npc.NPC;

public class NPCSorter implements Comparator<NPC>
{
    @Override
    public int compare(NPC e1, NPC e2) {
        return e1.getName().compareToIgnoreCase( e2.getName() );
    }
}
