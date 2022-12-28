package net.pvpmines.listener;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.pvpmines.Hub;
import net.pvpmines.cosmetics.Cosmetics;
import net.pvpmines.queue.Queue;
import net.pvpmines.utils.CC;
import net.pvpmines.utils.bungeecord.BungeeListener;
import net.pvpmines.utils.inventory.CosmeticInventory;
import net.pvpmines.utils.inventory.SelectorInventory;
import net.pvpmines.utils.inventory.impl.ParticleInventory;
import net.pvpmines.utils.items.JoinItem;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class HubListener implements Listener {

    private final Hub hub;
    public ArrayList<UUID> hidden = new ArrayList<>();
    private final Queue queue = new Queue();
    private SelectorInventory selectorInventory;
    private CosmeticInventory cosmeticInventory;
    private ParticleInventory particleInventory;
    private final Cosmetics cosmetics = new Cosmetics();

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
    public void onMove(PlayerMoveEvent event){
        if (event.getPlayer().getLocation().subtract(0,1,0).getBlock().getType().toString()
                .equals(this.hub.getConfig().getString("launchpad.block"))) {
            event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(2).setY(1));
        }
        this.cosmetics.cosmeticEvent(event);
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

    @EventHandler
    public void onSelectorClick(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() == null
        || event.getPlayer().getItemInHand().getItemMeta() == null
        || event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;

        if (!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate(
                this.hub.getConfig().getString("items.selector.name")
        ))) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            this.selectorInventory = new SelectorInventory(this.hub, event.getPlayer());
            this.selectorInventory.loadItems();
            this.selectorInventory.open();
        }
    }

    @EventHandler
    public void onCosmeticInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() == null
                || event.getPlayer().getItemInHand().getItemMeta() == null
                || event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;

        if (!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate(
                this.hub.getConfig().getString("items.cosmetic.name")
        ))) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            this.cosmeticInventory = new CosmeticInventory(this.hub, event.getPlayer());
            this.cosmeticInventory.loadItems();
            this.cosmeticInventory.open();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.translate(this.hub.getConfig()
                .getString("inventory.selector.title")))
        || event.getInventory().getTitle().equalsIgnoreCase(CC.translate(this.hub.getConfig()
                .getString("inventory.cosmetic.title")))) {
            event.setCancelled(true);
        }

        for (final String i : this.hub.getConfig().getConfigurationSection("inventory.selector.items").getKeys(false)) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate(this.hub
                    .getConfig().getString("inventory.selector.items." + i + ".name")))) {
                event.getWhoClicked().sendMessage(this.hub.getConfig().getString("inventory.selector.items." + i + ".server"));
                this.queue.insert((Player) event.getWhoClicked(), this.hub.getConfig().getString("inventory.selector.items." + i + ".server"));
            }
        }
    }

    @EventHandler
    public void onCosmeticClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.translate(this.hub.getConfig()
                .getString("inventory.cosmetic.title")))) {
            event.setCancelled(true);
        }

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate(this.hub
                .getConfig().getString("inventory.cosmetic.items.1.name")))) {
            event.getWhoClicked().sendMessage("armor");
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate(this.hub
                .getConfig().getString("inventory.cosmetic.items.2.name")))) {
            this.particleInventory = new ParticleInventory(this.hub, (Player) event.getWhoClicked());
            event.getWhoClicked().closeInventory();
            this.particleInventory.loadItems();
            this.particleInventory.open();
        }
    }

    @EventHandler
    public void onParticleClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.translate(this.hub.getConfig()
                .getString("cosmetic.particle.title")))) {
            event.setCancelled(true);
        }

        for (final String i : this.hub.getConfig().getConfigurationSection("cosmetic.particle.items").getKeys(false)) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate(this.hub.getConfig()
                    .getString("cosmetic.particle.items." + i + ".name")))) {
                if ((this.hub.getConfig().getString("cosmetic.particle.items." + i + ".particle")) != null) {
                    if (event.getWhoClicked().hasPermission("hub.cosmetic." + this.hub.getConfig().getString("cosmetic.particle.items." + i +".particle"))) {
                        this.cosmetics.applyEffect(event.getWhoClicked().getUniqueId(),
                                this.cosmetics.getEffectFromString(this.hub.getConfig().getString("cosmetic.particle.items." + i + ".particle")));
                        event.getWhoClicked().sendMessage(CC.translate("&aParticle successfully applied"));
                    } else {
                        event.getWhoClicked().sendMessage(CC.translate("&cNo Permissions"));
                    }
                }
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate(this.hub.getConfig()
                    .getString("cosmetic.particle.items.reset.name")))) {
                this.cosmetics.clearEffect(event.getWhoClicked().getUniqueId());
                event.getWhoClicked().sendMessage(CC.translate("&cParticle successfully removed"));
            }
        }
    }
}
