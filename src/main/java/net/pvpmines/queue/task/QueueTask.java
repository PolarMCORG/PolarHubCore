package net.pvpmines.queue.task;

import net.pvpmines.Hub;
import net.pvpmines.queue.Queue;
import org.bukkit.Bukkit;

public class QueueTask {

    private final Hub hub;
    private final Queue queue = new Queue();
    public QueueTask(Hub hub){
        this.hub = hub;
        init();
    }

    private void init(){
        Bukkit.getScheduler().runTaskTimer(hub, new Runnable() {

            int i = hub.getConfig().getInt("delay");
            @Override
            public void run() {
                i--;
                if (i <= 0) {
                    queue.send();
                    i = hub.getConfig().getInt("delay");
                }
            }
        },0L,20L);
    }
}
