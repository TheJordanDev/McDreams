package fr.theskinter.mcdreams.ping;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.authlib.GameProfile;

import fr.theskinter.mcdreams.McDreams;
import net.minecraft.server.v1_12_R1.ChatModifier;
import net.minecraft.server.v1_12_R1.ChatTypeAdapterFactory;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketStatusOutServerInfo;
import net.minecraft.server.v1_12_R1.ServerPing; 
import net.minecraft.server.v1_12_R1.ServerPing.ServerData;
import net.minecraft.server.v1_12_R1.ServerPing.ServerPingPlayerSample;

public class PingListener{

	public static String MOTD() {
		Boolean state = McDreams.instance.isMaintenance();
		if (state) {
			return "                    §c§l▶   §3§lMc§b§lDreams  §c§L◀\n"+
					"                §6§l⚠ Maintenance ⚠\n";
		}
		return "                     §c§l▶ §3§lMc§b§lDreams §c§L◀"
		+"\n         §3§l⬧ §a§l"+"P"+"§2§l"+"a"+"§a§l"+"r"+"§2§l"+"c "
		+"§6§l"+"D"+"§e§l"+"i"+"§6§l"+"s§e§l"+"n"+"§6§l"+"e"+"§e§l"+"y"+"§6§l"+"L"+"§e§l"+"a"+"§6§l"+"n"+"§e§ld "
		+"§6§l"+"P"+"§e§l"+"a"+"§6§l"+"r"+"§e§l"+"i"+"§6§l"+"s §3§l⬧";
	}
	
	public static String PLAYER_HOVER() {
		//
		Boolean state = McDreams.instance.isMaintenance();
		if (state) {
			return null;
		}
		StringBuilder players_builder = new StringBuilder();
		if (Bukkit.getOnlinePlayers().size() != 0) {
				Iterator<? extends Player> players = Bukkit.getOnlinePlayers().iterator();
				while (players.hasNext()) {
					if (!players.hasNext()) {
						players_builder.append(players.next().getName());
					} else {
						players_builder.append(players.next().getName()+"\n");
					}
			}
			return players_builder.toString();
		}
		return null;
	}
	
	public static String PLAYER_DISPLAY() {
		Boolean state = McDreams.instance.isMaintenance();
		if (state) {
			return "§6§lMaintenance";
		}
		return "§7§l≫§6§l"+Bukkit.getOnlinePlayers().size()+"§7§l/§b§l"+Bukkit.getMaxPlayers();
	}
	
    private static final Gson a;

    static{
        a = new GsonBuilder().registerTypeAdapter(ServerPing.ServerData.class, new OverridenServerDataSerializer())
                .registerTypeAdapter(ServerPing.ServerPingPlayerSample.class, new OverridenServerSampleSerializer())
                .registerTypeAdapter(ServerPing.class, new PingSerializer())
                .registerTypeHierarchyAdapter(IChatBaseComponent.class, new IChatBaseComponent.ChatSerializer())
                .registerTypeHierarchyAdapter(ChatModifier.class, new ChatModifier.ChatModifierSerializer())
                .registerTypeAdapterFactory(new ChatTypeAdapterFactory()).create();
    }

    public PingListener() {    	
        ReflectedObject.setStaticField(PacketStatusOutServerInfo.class, "a", a);
    }

    public static class PingSerializer extends ServerPing.Serializer {
        @Override
        public JsonElement a(ServerPing serverPing, Type type, JsonSerializationContext jsonSerializationContext) {
        	
        	serverPing.setMOTD(getMessage(MOTD()));
            ReflectedObject ping = new ReflectedObject(serverPing);

            ReflectedObject playerSample = ping.get("b");
            if (PLAYER_HOVER() != null) {
	            playerSample.setField("c",new GameProfile[] {new com.mojang.authlib.GameProfile(new UUID(0,0), PLAYER_HOVER())});
            }

            ReflectedObject serverData = ping.get("c");
            serverData.setField("a", PLAYER_DISPLAY()); // if we do not set the chat color, it will be red
            serverData.setField("b",serverPing.getServerData().getProtocolVersion());// POUR UN [0/20] CUSTOM 
            return super.a(serverPing, type, jsonSerializationContext);
        }
    }

    public static IChatBaseComponent getMessage(String message) {
        return ChatSerializer.a("{\"text\": \"" + message + "\"}");
    }

    public static class OverridenServerDataSerializer extends ServerPing.ServerData.Serializer{

        @Override
        public JsonElement a(ServerData serverData, Type type, JsonSerializationContext jsonSerializationContext) {
            return super.a(serverData, type, jsonSerializationContext);
        }

    }
    public static class OverridenServerSampleSerializer extends ServerPing.ServerPingPlayerSample.Serializer {
        @Override
        public JsonElement a(ServerPingPlayerSample serverPingPlayerSample, Type type,
                JsonSerializationContext jsonSerializationContext) {
  
            JsonObject jsonObject = (JsonObject) super.a(serverPingPlayerSample, type, jsonSerializationContext);

            return jsonObject;
  
        }
    }
}