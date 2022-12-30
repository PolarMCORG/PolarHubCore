package net.pvpmines.queue;

import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import net.pvpmines.utils.bungeecord.BungeeListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;;

import java.util.*;

public class Queue {
    public static LinkedHashMap<String, LinkedHashMap<String, Integer>> queues = new LinkedHashMap<>();
    private final QueueAdapter adapter = new QueueAdapter(Hub.getInstance());

    public void loadQueues(){
        for (String queue : adapter.getQueues()) {
            queues.put(queue, new LinkedHashMap<>());
        }
    }

    public void addPlayer(Player player, String queue){
        if (!queues.containsKey(queue)) {
            player.sendMessage(CC.translate("&cFailed to join queue, QUEUE NOT EXISTS"));
            return;
        }

        String permission = getQueuePriority(player);
        if (permission == null) {
            LinkedHashMap<String, Integer> current = queues.get(queue);
            if (current.size() < 1) {
                current.put(player.getUniqueId().toString(), 1);
                queues.put(queue, current);
            } else {
                Map.Entry<String, Integer> last = current.entrySet().stream().reduce((one, two) -> two).get();
                current.put(player.getUniqueId().toString(), last.getValue() + 1);
                queues.put(queue, current);
            }
            player.sendMessage(CC.translate(Hub.getInstance().getConfig()
                    .getString("queue-join")
                    .replace("%player%", player.getName())
                    .replace("%queue_size%", String.valueOf(queues.get(queue).size()))
                    .replace("%queue_position%", String.valueOf(queues.get(queue).get(player.getUniqueId().toString())))));
            return;
        }

        String[] perm = permission.split(".");
        int query = Integer.parseInt(perm[2]);
        LinkedHashMap<String, Integer> queuePlayers = queues.get(queue);
        for (Map.Entry<String, Integer> map : queuePlayers.entrySet()) {
            String p = getQueuePriority(Bukkit.getPlayer(UUID.fromString(map.getKey())));
            String[] pe = p.split(".");
            int q = Integer.parseInt(pe[2]);
            if (query < q) {
                queuePlayers.put(player.getUniqueId().toString(), q);
                queues.put(queue, queuePlayers);
                player.sendMessage(CC.translate(Hub.getInstance().getConfig()
                                .getString("queue-update")
                        .replace("%player%", player.getName())
                        .replace("%queue_size%", String.valueOf(queues.get(queue).size()))
                        .replace("%queue_position%", String.valueOf(queues.get(queue).get(player.getUniqueId().toString())))));
                return;
            }
        }
    }

    public void removePlayer(Player player){
        queues.remove(player.getUniqueId().toString());
    }

    public void send(String queue){
        for (Map.Entry<String, Integer> map : queues.get(queue).entrySet()) {
            if (map.getValue() == 1) {
                BungeeListener.sendPlayerToServer(Bukkit.getPlayer(UUID.fromString(map.getKey())),queue);
                queues.get(queue).remove(map.getKey());
                for (int i = 0; i < map.getKey().length(); i++) {
                    Bukkit.getPlayer(UUID.fromString(map.getKey())).sendMessage(CC.translate(Hub.getInstance().getConfig()
                            .getString("queue-join")
                            .replace("%player%", Bukkit.getPlayer(UUID.fromString(map.getKey())).getName())
                            .replace("%queue_size%", String.valueOf(queues.get(queue).size()))
                            .replace("%queue_position%", String.valueOf(queues.get(queue).get(Bukkit.getPlayer(UUID.fromString(map.getKey())).getUniqueId().toString())))));
                }
            }
        }
    }

    public String getQueuePriority(Player player){
        List<Integer> queries = Hub.getInstance().getConfig().getIntegerList("priority");
        for (int i = 0; i < queries.size(); i++) {
            if (player.hasPermission("priority." + i + ".permission")) {
                return ("priority." + i + ".permission");
            }
        }
        return null;
    }
}
