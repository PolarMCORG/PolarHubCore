package net.pvpmines;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import net.pvpmines.command.DiscordCommand;
import net.pvpmines.command.StoreCommand;
import net.pvpmines.command.TwitterCommand;
import net.pvpmines.command.WebsiteCommand;
import net.pvpmines.listener.HubListener;
import net.pvpmines.scoreboard.ScoreboardAdapter;
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

    }
}
