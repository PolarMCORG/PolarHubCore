package net.pvpmines.queue;

import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import net.pvpmines.utils.bungeecord.BungeeListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;;

import java.util.*;

public class Queue {
    public static LinkedHashMap<String, LinkedList<UUID>> queues = new LinkedHashMap<>();
    private final QueueAdapter adapter = new QueueAdapter(Hub.getInstance());

    public void loadQueues(){
        for (String queue : adapter.getQueues()) {
            queues.put(queue, new LinkedList<>());
        }
    }

    public void addPlayer(Player player, String queue){
        if (!queues.containsKey(queue)) {
            player.sendMessage(CC.translate("&cFailed to join queue, QUEUE NOT EXISTS"));
            return;
        }
        if (Queue.queues.get(queue).contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cFailed to join queue, YOU ARE ALREADY IN QUEUE"));
            return;
        }
        if (queues.get(queue) != null) {
            LinkedList<UUID> queuesUUID = queues.get(queue);
            queuesUUID.add(player.getUniqueId());
            queues.replace(queue, queues.get(queue), queuesUUID);
        } else {
            LinkedList<UUID> newList = new LinkedList<>();
            newList.add(player.getUniqueId());
            queues.put(queue, newList);
        }
        player.sendMessage(CC.translate(Hub.getInstance().getConfig().getString("queue-join")
                .replace("%player%", player.getName())
                .replace("%queue_size%", String.valueOf(queues.get(queue).size()))
                .replace("%queue_position%", String.valueOf(getPlayerPosition(queue, player.getUniqueId())))));

    }

    public int getPlayerPosition(String queue, UUID player) {
        for (int i = 0; i < queues.get(queue).size(); i++) {
            if (queues.get(queue).get(i).equals(player)) {
                return i;
            }
        }
        return 0;
    }

    public void removePlayer(Player player){
        queues.get("smp").remove(player.getUniqueId());
    }

    public void updateQueue(String queue) {
        if (queues.get(queue) == null) return;
        LinkedList<UUID> uuids = queues.get(queue);
        if (uuids != null) {
            if (uuids.size() >= 1 && uuids.get(0) != null) {
                BungeeListener.sendPlayerToServer(Bukkit.getPlayer(uuids.get(0)), queue);
                uuids.removeFirst();
            }
        }
    }
}
