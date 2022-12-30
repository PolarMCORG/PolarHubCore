package net.pvpmines.command;

import net.pvpmines.Hub;
import net.pvpmines.queue.Queue;
import net.pvpmines.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveQueueCommand implements CommandExecutor {

    private final Queue queue = new Queue();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            queue.removePlayer((Player) commandSender);
            commandSender.sendMessage(CC.translate(Hub.getInstance().getConfig().getString("queue-leave")));
        }
        return true;
    }
}
