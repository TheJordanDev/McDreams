package fr.theskinter.mcdreams.utils.creators;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;

public class ToolCreator extends ItemCreator {

	@Getter
	private short durability = 0;
	
	public ItemCreator setDamage(Integer durability) {
		this.durability = durability.shortValue();
		return this;
	}
	
	public ItemStack build() {
		ItemStack item = new ItemStack(getMat(),getAmount(),getData());
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(getName());
		im.setLore(getLore());
		if (isHideFlags()) {
			for (ItemFlag flag : ItemFlag.values()) {
				im.addItemFlags(flag);
			}
		}
		int itemDura = item.getType().getMaxDurability();
		int damageGiven = durability;
		short dmg = new Integer(itemDura-damageGiven).shortValue();
		item.setDurability(dmg);
		item.setItemMeta(im);
		return item;

	}
	
}
