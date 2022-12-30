package net.pvpmines.queue.task;

import net.pvpmines.Hub;
import net.pvpmines.queue.Queue;
import net.pvpmines.queue.QueueAdapter;
import org.bukkit.Bukkit;

public class QueueTask {

    private final Hub hub;
    private final Queue queue = new Queue();
    private final String queueType;
    public QueueTask(Hub hub, String queueType){
        this.hub = hub;
        this.queueType = queueType;
        init();
    }

    private void init(){
        Bukkit.getScheduler().runTaskTimer(this.hub, new Runnable() {
            @Override
            public void run() {
                queue.send(queueType);

            }
        },0L,hub.getConfig().getInt("delay") * 20L);
    }
}
