package net.pvpmines.command;

import net.pvpmines.Hub;
import net.pvpmines.queue.task.QueueTask;
import net.pvpmines.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class PauseQueueCommand implements CommandExecutor {

    private final Hub hub;
    public ArrayList<String> paused = new ArrayList<>();

    public PauseQueueCommand(Hub hub) {
        this.hub = hub;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender.hasPermission("hub.queue.pause"))) {
            commandSender.sendMessage(CC.translate("&cNo Permissions"));
            return false;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(CC.translate("&cPlease provide a queue"));
        } else {
            String queue = strings[0];
            if (queue.equalsIgnoreCase("smp")) {
                if (!paused.contains(queue)) {
                    paused.add(queue);
                    if (Hub.queueTaskHashMap.get("smp") != null) {
                        QueueTask queueTask = Hub.queueTaskHashMap.get("smp");
                        queueTask.shutdown();
                        Bukkit.broadcastMessage(CC.translate(this.hub.getConfig().getString("queue-paused")
                                .replace("%queue%", queue)));
                    }
                } else {
                    paused.remove(queue);
                    Bukkit.broadcastMessage(CC.translate(this.hub.getConfig().getString("queue-unpause")
                            .replace("%queue%", queue)));
                    QueueTask queueTask = new QueueTask(this.hub, queue);
                    Hub.queueTaskHashMap.replace("smp", Hub.queueTaskHashMap.get("smp"), queueTask);
                    queueTask.init();
                }
            }
        }

        return true;
    }
}
