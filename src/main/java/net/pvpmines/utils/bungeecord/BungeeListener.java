package net.pvpmines.utils.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.pvpmines.Hub;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * CREDITS
 * > https://github.com/LBuddyBoy/xHub-OPENSOURCED
 * > for BungeeListener.java
 */

@Getter
public class BungeeListener implements PluginMessageListener {

    public static int PLAYER_COUNT;

    @SuppressWarnings("ALL")
    public static void updateCount(Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerCount");
        output.writeUTF("ALL");
        player.sendPluginMessage(Hub.getInstance(), "BungeeCord", output.toByteArray());
    }

    @Override
    @SuppressWarnings("ALL")
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (!channel.equalsIgnoreCase("BungeeCord"))
            return;

        ByteArrayDataInput input = ByteStreams.newDataInput(message);

        String subchannel = input.readUTF();

        if (subchannel.equals("PlayerCount") && input.readUTF().equalsIgnoreCase("ALL")) {
            BungeeListener.PLAYER_COUNT = input.readInt();
        }

    }

    @SuppressWarnings("ALL")
    public static void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
    }

    static {
        BungeeListener.PLAYER_COUNT = 0;
    }
}
