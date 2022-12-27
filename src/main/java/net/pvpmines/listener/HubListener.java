package net.pvpmines.listener;

import lombok.Getter;
import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import net.pvpmines.utils.bungeecord.BungeeListener;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

@Getter
public class HubListener implements Listener {

    private final Hub hub;

    public HubListener(Hub hub) {
        this.hub = hub;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        for (final String i : this.hub.getConfig().getStringList("join.message")) {
            player.sendMessage(CC.translate(i)
                    .replace("%player%", player.getName())
                    .replace("%discord%", this.hub.getConfig().getString("discord"))
                    .replace("%website%", this.hub.getConfig().getString("website"))
                    .replace("%store%", this.hub.getConfig().getString("store"))
                    .replace("%twitter%", this.hub.getConfig().getString("twitter")));
        }
        BungeeListener.updateCount(player);
        player.setAllowFlight(true);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }


    @EventHandler
    public void onPlayerDoubleJump(PlayerToggleFlightEvent event){
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) return;
        event.setCancelled(true);
        player.setVelocity(player.getLocation().getDirection().multiply(2).setY(1));
    }
}
