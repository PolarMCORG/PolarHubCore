package net.pvpmines.command;

import net.pvpmines.Hub;
import net.pvpmines.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WebsiteCommand implements CommandExecutor {

    private final Hub hub;

    public WebsiteCommand(Hub hub) {
        this.hub = hub;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(CC.translate(this.hub.getConfig().getString("discord")));
        return true;
    }
}
