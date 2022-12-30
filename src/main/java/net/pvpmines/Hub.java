package net.pvpmines;

import io.github.nosequel.tab.TabHandler;
import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import net.pvpmines.command.*;
import net.pvpmines.listener.HubListener;
import net.pvpmines.queue.Queue;
import net.pvpmines.queue.task.QueueTask;
import net.pvpmines.scoreboard.ScoreboardAdapter;

import net.pvpmines.tablist.TabAdapter;
import net.pvpmines.utils.bungeecord.BungeeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Hub extends JavaPlugin {

    private static Hub instance;
    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
        this.commands();
        this.listener();
        this.scoreboard();
        this.tab();
        new Queue().loadQueues();
        for (final String queue : this.getConfig().getStringList("queues")) {
            new QueueTask(this, queue);
        }
    }

    public static Hub getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void commands(){
        getCommand("discord").setExecutor(new DiscordCommand(this));
        getCommand("store").setExecutor(new StoreCommand(this));
        getCommand("twitter").setExecutor(new TwitterCommand(this));
        getCommand("website").setExecutor(new WebsiteCommand(this));
        getCommand("leavequeue").setExecutor(new LeaveQueueCommand());
    }

    private void listener(){
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new HubListener(this),this);
    }

    private void scoreboard(){
        Assemble assemble = new Assemble(this, new ScoreboardAdapter(this));
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.KOHI);
    }

    private void tab(){
        new TabHandler(new TabAdapter(this), this, 20L);
    }
}
