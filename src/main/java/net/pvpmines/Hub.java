package net.pvpmines;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.pvpmines.command.*;
import net.pvpmines.listener.HubListener;
import net.pvpmines.queue.Queue;
import net.pvpmines.queue.task.QueueTask;
import net.pvpmines.scoreboard.ScoreboardAdapter;

import net.pvpmines.utils.bungeecord.BungeeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
public final class Hub extends JavaPlugin {

    private static Hub instance;
    private static Chat chat = null;
    public static HashMap<String, QueueTask> queueTaskHashMap = new LinkedHashMap<>();
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
        new Queue().loadQueues();
        for (final String queue : this.getConfig().getStringList("queues")) {
            QueueTask queueTask = new QueueTask(this, queue);
            queueTaskHashMap.put(queue, queueTask);
            queueTask.init();
        }
        setupChat();
        saveResource("config.yml", false);
        reloadConfig();
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
        getCommand("pausequeue").setExecutor(new PauseQueueCommand(this));
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
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public static Chat getChat() {
        return chat;
    }
}
