package pl.dream.dplayerwarp;

import org.bukkit.plugin.java.JavaPlugin;
import pl.dream.dplayerwarp.command.PWarpCommand;
import pl.dream.dplayerwarp.controller.ConfigController;
import pl.dream.dplayerwarp.controller.TeleportationController;
import pl.dream.dplayerwarp.data.Warp;
import pl.dream.dplayerwarp.listener.PlayerMoveListener;
import pl.dream.dplayerwarp.listener.PlayerTakeDamageListener;

import java.util.HashMap;

public final class DPlayerWarp extends JavaPlugin {
    public TeleportationController teleportationController;
    public HashMap<String, Warp> warps;
    public HashMap<String, Integer> prices;
    public SQLite database;

    private ConfigController configController;


    @Override
    public void onEnable() {
        database = new SQLite(this);
        database.connect();
        database.loadWarps();

        configController = new ConfigController(this);
        teleportationController = new TeleportationController(this);

        getServer().getPluginManager().registerEvents(new PlayerMoveListener(teleportationController), this);
        getServer().getPluginManager().registerEvents(new PlayerTakeDamageListener(teleportationController), this);

        getCommand("pwarp").setExecutor(new PWarpCommand(this, teleportationController));

        loadPlugin();
    }

    @Override
    public void onDisable() {
        database.disconnect();

    }

    public void reloadPlugin(){
        reloadConfig();

        loadPlugin();
    }

    private void loadPlugin(){
        saveDefaultConfig();
        Locale.loadMessages(this);
        configController.loadPlugin();
    }
}
