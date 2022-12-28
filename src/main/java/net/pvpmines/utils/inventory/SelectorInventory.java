package net.pvpmines.utils.inventory;

import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SelectorInventory {

    private final Hub hub;
    private final Player player;
    private Inventory inventory;

    public SelectorInventory(Hub hub, Player player){
        this.hub = hub;
        this.player = player;
        init();
    }

    private void init(){
        this.inventory = Bukkit.createInventory(player,
                this.hub.getConfig().getInt("inventory.selector.size"),
                CC.translate(this.hub.getConfig().getString("inventory.selector.title")));
    }

    public void loadItems(){
        for (final String i : this.hub.getConfig().getConfigurationSection("inventory.selector.items").getKeys(false)) {
            String material = this.hub.getConfig().getString("inventory.selector.items." + i + ".item");
            String name = this.hub.getConfig().getString("inventory.selector.items." + i +".name");
            ArrayList<String> lore = new ArrayList<>();
            for (final String l : this.hub.getConfig().getStringList("inventory.selector.items." + i + ".lore")) {
                lore.add(CC.translate(l));
            }
            int slot = this.hub.getConfig().getInt("inventory.selector.items." + i +".slot");
            int data = this.hub.getConfig().getInt("inventory.selector.items." + i + ".data");

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
        if (this.hub.getConfig().getBoolean("inventory.selector.overlay")) {
            ItemStack item = new ItemStack(Material.valueOf(this.hub.getConfig().getString("inventory.selector.overlay-item")),
                    1, (byte) this.hub.getConfig().getInt("inventory.selector.overlay-data"));
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
