package net.pvpmines.queue;

import lombok.Getter;
import net.pvpmines.Hub;

import java.util.List;

@Getter
public class QueueAdapter {

    private final Hub hub;

    public QueueAdapter(Hub hub) {
        this.hub = hub;
    }
    public List<String> getQueues(){
       return this.hub.getConfig().getStringList("queues");
    }
}
