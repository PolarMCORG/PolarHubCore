package net.pvpmines.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import net.pvpmines.Hub;
import net.pvpmines.queue.Queue;
import net.pvpmines.utils.CC;
import net.pvpmines.utils.bungeecord.BungeeListener;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ScoreboardAdapter implements AssembleAdapter {

    private final Hub hub;
    private final Queue queueC;

    public ScoreboardAdapter(Hub hub) {
        this.hub = hub;
        this.queueC = new Queue();
    }
    @Override
    public String getTitle(Player player) {
        return CC.translate(this.hub.getConfig().getString("scoreboard.title"));
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        String position;
        String size;
        String queue;
        LinkedList<UUID> linkedList = Queue.queues.get("smp");

        if (!linkedList.contains(player.getUniqueId())) {
            position = "NONE";
            size = "NONE";
            queue = "NONE";
        } else {
            position = String.valueOf(queueC.getPlayerPosition("smp", player.getUniqueId()) + 1);
            size = String.valueOf(Queue.queues.get("smp").size());
            queue = "SMP";
        }

        for (final String i : this.hub.getConfig().getStringList("scoreboard.normal")) {
            lines.add(CC.translate(i)
                    .replace("%player%", player.getName())
                    .replace("%online%", String.valueOf(BungeeListener.PLAYER_COUNT))
                    .replace("%rank%", Hub.getChat().getPrimaryGroup(player))
                    .replace("%queue%", queue)
                    .replace("%position%", position)
                    .replace("%size%", size));
        }
        return lines;
    }
}
