package net.pvpmines.listener;

import lombok.Getter;
import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import net.pvpmines.utils.bungeecord.BungeeListener;
import net.pvpmines.utils.items.JoinItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class HubListener implements Listener {

    private final Hub hub;
    public ArrayList<UUID> hidden = new ArrayList<>();

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
        new JoinItem(this.hub, event);
        player.setHealth(20);
        player.setFoodLevel(20);

        for (UUID uuid : hidden) {
            Player p = Bukkit.getPlayer(uuid);
            p.hidePlayer(event.getPlayer());
        }
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

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setDamage(0);
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        event.setDamage(0);
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByBlockEvent event) {
        event.setDamage(0);
        event.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLaunchPad(PlayerMoveEvent event){
        if (event.getPlayer().getLocation().subtract(0,1,0).getBlock().getType().toString()
                .equals(this.hub.getConfig().getString("launchpad.block"))) {
            event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(2).setY(1));
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onVisiblityInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() == null
        || event.getPlayer().getItemInHand().getItemMeta() == null
        || event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;

        if (!event.getPlayer().getItemInHand().getItemMeta().getDisplayName()
                .equalsIgnoreCase(CC.translate(this.hub.getConfig().getString("items.visiblity.name")))) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (!hidden.contains(event.getPlayer().getUniqueId())) {
                hidden.add(event.getPlayer().getUniqueId());
                for (Player online : Bukkit.getOnlinePlayers()) {
                    event.getPlayer().hidePlayer(online);
                }
                event.getPlayer().sendMessage(CC.translate(this.hub.getConfig().getString("hidden")));
                JoinItem.update(event.getPlayer(), this.hub.getConfig().getInt("items.visiblity.new"));
            } else {
                hidden.remove(event.getPlayer().getUniqueId());
                for (Player online : Bukkit.getOnlinePlayers()) {
                    event.getPlayer().showPlayer(online);
                }
                event.getPlayer().sendMessage(CC.translate(this.hub.getConfig().getString("shown")));
                JoinItem.update(event.getPlayer(), this.hub.getConfig().getInt("items.visiblity.data"));
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickUp(PlayerPickupItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }
}
