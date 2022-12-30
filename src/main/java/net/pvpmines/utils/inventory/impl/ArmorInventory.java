package net.pvpmines.utils.inventory.impl;

import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ArmorInventory {

    private final Hub hub;
    private final Player player;
    private Inventory inventory;

    public ArmorInventory(Hub hub, Player player){
        this.hub = hub;
        this.player = player;
        init();
    }

    private void init(){
        this.inventory = Bukkit.createInventory(player,
                this.hub.getConfig().getInt("cosmetic.armor.size"),
                CC.translate(this.hub.getConfig().getString("cosmetic.armor.title")));
    }

    public void loadItems(){
        for (final String i : this.hub.getConfig().getConfigurationSection("cosmetic.armor.items").getKeys(false)) {
            String material = this.hub.getConfig().getString("cosmetic.armor.items." + i + ".item");
            String name = this.hub.getConfig().getString("cosmetic.armor.items." + i +".name");
            ArrayList<String> lore = new ArrayList<>();
            for (final String l : this.hub.getConfig().getStringList("cosmetic.armor.items." + i + ".lore")) {
                lore.add(CC.translate(l));
            }
            int slot = this.hub.getConfig().getInt("cosmetic.armor.items." + i +".slot");
            int data = this.hub.getConfig().getInt("cosmetic.armor.items." + i + ".data");

            ItemStack stack = new ItemStack(Material.valueOf(material), 1, (byte) data);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(CC.translate(name));
            meta.setLore(lore);
            stack.setItemMeta(meta);
            inventory.setItem(slot, stack);
            applyOverlay();
        }
    }

    private void applyOverlay(){
        if (this.hub.getConfig().getBoolean("cosmetic.armor.overlay")) {
            ItemStack item = new ItemStack(Material.valueOf(this.hub.getConfig().getString("cosmetic.armor.overlay-item")),
                    1, (byte) this.hub.getConfig().getInt("cosmetic.armor.overlay-data"));
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, item);
                }
            }
        }
    }

    public void open() {
        player.openInventory(inventory);
    }
}
