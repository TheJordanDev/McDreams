package fr.theskinter.mcdreams.utils;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleManager {
    @SuppressWarnings("rawtypes")
    public static void sendTitle(Player player, String msgTitle, String msgSubTitle, int ticks) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + msgTitle + "\"}"));
        IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + msgSubTitle + "\"}"));
        PacketPlayOutTitle p = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle p2 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)p);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)p2);
        TitleManager.sendTime(player, ticks);
    }

    @SuppressWarnings("rawtypes")
    private static void sendTime(Player player, int ticks) {
        PacketPlayOutTitle p = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, 20, ticks, 20);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)p);
    }

    @SuppressWarnings("rawtypes")
    public static void sendActionBar(Player player, String msg, int ticks) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + msg + "\"}"));
        PacketPlayOutTitle ppoc = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR	, cbc);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)ppoc);
        TitleManager.sendTime(player, ticks);
    }
}