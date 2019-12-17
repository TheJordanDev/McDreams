package fr.theskinter.mcdreams.utils.creators;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;

import fr.theskinter.mcdreams.utils.Skulls.SkullUtils;
import lombok.Getter;
import lombok.Setter;

public class SkullCreator extends ItemCreator {

	@Getter @Setter private String SKIN_TEXTURE = "";
	@Getter @Setter private String SKIN_TEXTURE_t = "";
	
	public SkullCreator() {
		setMaterial(Material.SKULL_ITEM);
	}
	
	public SkullCreator setName(String name) {
		super.setName(name);
		return this;
	}
	
	public SkullCreator setLore(List<String> lore) {
		super.setLore(lore);
		return this;
	}
	
	public SkullCreator setAmount(Integer amount) {
		super.setAmount(amount);
		return this;
	}
	
	public ItemStack getFromURL(String url) {
		ItemStack stack = SkullUtils.getCustomSkull(url);
		stack.setAmount(getAmount());
		if (stack.hasItemMeta()) {
			ItemMeta meta =stack.getItemMeta();
			meta.setDisplayName(getName());
			meta.setLore(getLore());
			stack.setItemMeta(meta);
		}
		return stack;
	}
	
    public static ItemStack getSkull(Player player) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1);
       
        ItemMeta headMeta = head.getItemMeta();
        GameProfile profile = ((CraftPlayer)player).getProfile();
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
	
}
