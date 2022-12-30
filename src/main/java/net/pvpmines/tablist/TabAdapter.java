package net.pvpmines.tablist;

import io.github.nosequel.tab.entry.TabElement;
import io.github.nosequel.tab.entry.TabElementHandler;
import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TabAdapter implements TabElementHandler {

    private final Hub hub;

    public TabAdapter(Hub hub) {
        this.hub = hub;
    }
    @Override
    public TabElement getElement(Player player) {
        final TabElement element = new TabElement();
        element.setHeader(CC.translate(this.hub.getConfig().getString("tab.header")));
        element.setFooter(CC.translate(this.hub.getConfig().getString("tab.footer")));

        for (int i = 0; i < 20; i++) {
            element.add(0, i, CC.translate(this.hub.getConfig().getString("tab.zero." + i)
                    .replace("%player%", player.getName())));
            element.add(1, i, CC.translate(this.hub.getConfig().getString("tab.first." + i)
                    .replace("%player%", player.getName())));
            element.add(2, i, CC.translate(this.hub.getConfig().getString("tab.second." + i)
                    .replace("%player%", player.getName())));
            element.add(3, i, CC.translate(this.hub.getConfig().getString("tab.third." + i)
                    .replace("%player%", player.getName())));
        }

        return element;
    }
}
