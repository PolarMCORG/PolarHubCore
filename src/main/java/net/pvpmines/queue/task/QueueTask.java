package net.pvpmines.queue.task;

import net.pvpmines.Hub;
import net.pvpmines.queue.Queue;
import net.pvpmines.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class QueueTask {

    private final Hub hub;
    private final Queue queue = new Queue();
    private final String queueType;
    private BukkitTask bukkitTask;
    public QueueTask(Hub hub, String queueType){
        this.hub = hub;
        this.queueType = queueType;
    }

    public void init(){
        bukkitTask = Bukkit.getScheduler().runTaskTimer(this.hub, new Runnable() {
            @Override
            public void run() {
                queue.updateQueue(queueType);

                if (Queue.queues.get(queueType) != null) {
                    for (UUID uuid : Queue.queues.get(queueType)) {
                        int position = queue.getPlayerPosition(queueType, uuid);
                        Bukkit.getPlayer(uuid).sendMessage(CC.translate(hub.getConfig().getString("queue-update")
                                .replace("%player%", Bukkit.getPlayer(uuid).getName()))
                                .replace("%position%", String.valueOf(position) + 1)
                                .replace("%size%", String.valueOf(Queue.queues.get(queueType).size())));
                    }
                }
            }
        },0L,hub.getConfig().getInt("delay") * 20L);
    }

    public void shutdown(){
        bukkitTask.cancel();
        bukkitTask = null;
    }
}
