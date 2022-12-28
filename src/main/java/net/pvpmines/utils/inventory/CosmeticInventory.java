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

public class CosmeticInventory {

    private final Hub hub;
    private final Player player;
    private Inventory inventory;

    public CosmeticInventory(Hub hub, Player player){
        this.hub = hub;
        this.player = player;
        init();
    }

    private void init(){
        this.inventory = Bukkit.createInventory(player,
                this.hub.getConfig().getInt("inventory.cosmetic.size"),
                CC.translate(this.hub.getConfig().getString("inventory.cosmetic.title")));
    }

    public void loadItems(){
        for (final String i : this.hub.getConfig().getConfigurationSection("inventory.cosmetic.items").getKeys(false)) {
            String material = this.hub.getConfig().getString("inventory.cosmetic.items." + i + ".item");
            String name = this.hub.getConfig().getString("inventory.cosmetic.items." + i +".name");
            ArrayList<String> lore = new ArrayList<>();
            for (final String l : this.hub.getConfig().getStringList("inventory.cosmetic.items." + i + ".lore")) {
                lore.add(CC.translate(l));
            }
            int slot = this.hub.getConfig().getInt("inventory.cosmetic.items." + i +".slot");
            int data = this.hub.getConfig().getInt("inventory.cosmetic.items." + i + ".data");

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
        if (this.hub.getConfig().getBoolean("inventory.cosmetic.overlay")) {
            ItemStack item = new ItemStack(Material.valueOf(this.hub.getConfig().getString("inventory.cosmetic.overlay-item")),
                    1, (byte) this.hub.getConfig().getInt("inventory.cosmetic.overlay-data"));
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
