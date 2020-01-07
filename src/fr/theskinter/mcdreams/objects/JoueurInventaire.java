package fr.theskinter.mcdreams.objects;

import java.util.Comparator;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Deprecated
public class JoueurInventaire {

	public static ItemSorter sorter = new ItemSorter();
	
	@Getter @Setter private Integer maxSlots = 4*9;
	@Getter @Setter private Inventory inventory;
	
	public JoueurInventaire() {}
	
	public static class ItemSorter implements Comparator<ItemStack> {
		@Override public int compare(ItemStack o1, ItemStack o2) { return o1.getType().name().compareToIgnoreCase(o2.getType().name()); }
	}

}
