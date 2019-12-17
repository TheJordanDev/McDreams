package fr.theskinter.mcdreams.utils.Skulls;

import java.util.Base64;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import fr.theskinter.mcdreams.utils.Utils;
import net.citizensnpcs.api.npc.NPC;

public class SkullUtils {

    public static ItemStack getCustomSkull(String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        Reflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        head.setItemMeta(headMeta);
        return head;
    }
    
    public static ItemStack getCustomSkullFromTexture(String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        if (texture != null) {
        	propertyMap.put("textures", new Property("textures", texture));
        }
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        Reflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        head.setItemMeta(headMeta);
        return head;
    }
    
    public static ItemStack getNPCSkull(NPC npc) {
    	ItemStack skull = SkullUtils.getCustomSkullFromTexture(npc.data().get(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA));
    	ItemMeta skullM = skull.getItemMeta();
    	skullM.setDisplayName(npc.getName());
    	skull.setItemMeta(skullM);
    	return skull;
    }
    
    public static ItemStack getPlayerSkull(Player player) {
    	String texture = Utils.getPlayerTexture(player).getValue();
    	ItemStack skull = SkullUtils.getCustomSkullFromTexture(texture);
    	ItemMeta skullM = skull.getItemMeta();
    	skullM.setDisplayName("§r§l"+player.getName());
    	skull.setItemMeta(skullM);
    	return skull;
    }

}
