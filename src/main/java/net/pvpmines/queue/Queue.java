package net.pvpmines.queue;

import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import net.pvpmines.utils.bungeecord.BungeeListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;;

import java.util.*;

public class Queue {
    private final QueueType queueType = new QueueType(Hub.getInstance());
    public static Map<UUID, String> queues = new LinkedHashMap<>();

    public void insert(Player player, String queue){
        if (queueType.getQueues().contains(queue)) {
            queues.put(player.getUniqueId(), queue);
            player.sendMessage(CC.translate("&aYou have entered queue " + queue));
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4FAILED TO FIND A QUEUE " + queue));
        }
    }

    public void remove(Player player){
        queues.remove(player.getUniqueId());
        player.sendMessage(CC.translate("&cYou have left queue"));
    }

    public void insertion(String queue){
        List<UUID> queued = new ArrayList<>();
        for (Map.Entry<UUID, String> set : queues.entrySet()) {

        }
    }

    public void send(){
        Optional<UUID> first = queues.keySet().stream().findFirst();
        if (first.isPresent()) {
            Player player = Bukkit.getPlayer(first.get());
            String server = queues.get(first.get());
            BungeeListener.sendPlayerToServer(player, server);
            remove(player);
        }
    }
}
