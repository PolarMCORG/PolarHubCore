package net.pvpmines.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import net.pvpmines.utils.bungeecord.BungeeListener;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter {

    private final Hub hub;

    public ScoreboardAdapter(Hub hub) {
        this.hub = hub;
    }
    @Override
    public String getTitle(Player player) {
        return CC.translate(this.hub.getConfig().getString("scoreboard.title"));
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        for (final String i : this.hub.getConfig().getStringList("scoreboard.normal")) {
            lines.add(CC.translate(i)
                    .replace("%player%", player.getName())
                    .replace("%online%", String.valueOf(BungeeListener.PLAYER_COUNT)));
        }
        return lines;
    }
}
