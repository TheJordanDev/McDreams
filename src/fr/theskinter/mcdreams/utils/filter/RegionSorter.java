package fr.theskinter.mcdreams.utils.filter;

import java.util.Comparator;

import fr.theskinter.mcdreams.objects.managers.RegionManager.Region;

public class RegionSorter implements Comparator<Region> {
    
	@Override
    public int compare(Region e1, Region e2) {
        return e1.getName().compareToIgnoreCase( e2.getName() );
    }
}
