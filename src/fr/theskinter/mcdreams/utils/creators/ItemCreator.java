package fr.theskinter.mcdreams.utils.creators;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;

public class ItemCreator {
	
	@Getter
	private String name;
	@Getter
	private Material mat = Material.AIR;
	@Getter
	private List<String> lore = new ArrayList<String>();
	@Getter
	private Integer amount = 1;
	@Getter
	private byte data = (byte)0;
	@Getter
	private boolean hideFlags = false;
	@Getter int _savedInvSlot = -1;
	
	public ItemCreator setSavedInvSlot(int slot) {
		this._savedInvSlot = slot;
		return this;
	}
	
	public ItemCreator setByte(byte data) {
		this.data = data;
		return this;
	}
	
	public ItemCreator setName(String name) {
		this.name = name;
		return this;
	}
	
	public ItemCreator setMaterial(Material mat) {
		this.mat = mat;
		return this;
	}
	
	public ItemCreator setAmount(Integer amount) {
		this.amount = amount;
		return this;
	}
	
	public ItemCreator setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	
	public ItemCreator hideFlags(boolean yes) {
		hideFlags = yes;
		return this;
	}
	
	public ItemStack build() {
		ItemStack item = new ItemStack(getMat(),getAmount(),getData());
		ItemMeta im = item.getItemMeta();
		if (getName() != null) { im.setDisplayName(getName()); }
		if (!getLore().isEmpty()) { im.setLore(getLore()); }
		if (isHideFlags()) {
			for (ItemFlag flag : ItemFlag.values()) { im.addItemFlags(flag); }
		}
		item.setItemMeta(im);
		return item;
	}
	
}
