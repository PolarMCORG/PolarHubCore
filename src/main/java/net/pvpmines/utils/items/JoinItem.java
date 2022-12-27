package net.pvpmines.utils.items;

import lombok.Getter;
import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@Getter
public class JoinItem {

    private final Hub hub;

    public JoinItem(Hub hub, PlayerJoinEvent event) {
        this.hub = hub;

        selector(event.getPlayer());
        visiblity(event.getPlayer());

    }

    private void selector(Player player){
        String material = this.hub.getConfig().getString("items.selector.item");
        int data = this.hub.getConfig().getInt("items.selector.data");
        String name = this.hub.getConfig().getString("items.selector.name");
        ArrayList<String> lore = new ArrayList<>();
        for (final String i : this.hub.getConfig().getStringList("items.selector.lore")) {
            lore.add(CC.translate(i));
        }
        int slot = this.hub.getConfig().getInt("items.selector.slot");

        ItemStack stack = new ItemStack(Material.valueOf(material), 1, (byte) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        player.getInventory().setItem(slot, stack);
    }

    private void visiblity(Player player) {
        String material = this.hub.getConfig().getString("items.visiblity.item");
        int data = this.hub.getConfig().getInt("items.visiblity.data");
        String name = this.hub.getConfig().getString("items.visiblity.name");
        ArrayList<String> lore = new ArrayList<>();
        for (final String i : this.hub.getConfig().getStringList("items.visiblity.lore")) {
            lore.add(CC.translate(i));
        }
        int slot = this.hub.getConfig().getInt("items.visiblity.slot");

        ItemStack stack = new ItemStack(Material.valueOf(material), 1, (byte) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        player.getInventory().setItem(slot, stack);
    }

    public static void update(Player player, int data){
        String material = Hub.getInstance().getConfig().getString("items.visiblity.item");
        String name = Hub.getInstance().getConfig().getString("items.visiblity.name");
        ArrayList<String> lore = new ArrayList<>();
        for (final String i : Hub.getInstance().getConfig().getStringList("items.visiblity.lore")) {
            lore.add(CC.translate(i));
        }
        int slot = Hub.getInstance().getConfig().getInt("items.visiblity.slot");

        ItemStack stack = new ItemStack(Material.valueOf(material), 1, (byte) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        player.getInventory().setItem(slot, stack);
    }
}
