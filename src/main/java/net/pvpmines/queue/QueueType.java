package net.pvpmines.queue;

import net.pvpmines.Hub;

import java.util.List;

public class QueueType {

    private final Hub hub;
    public QueueType(Hub hub) {
        this.hub = hub;
    }
    public List<String> getQueues(){
        return this.hub.getConfig().getStringList("queues");
    }
}
