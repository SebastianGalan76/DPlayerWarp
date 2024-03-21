package pl.dream.dplayerwarp;

import org.bukkit.plugin.java.JavaPlugin;
import pl.dream.dplayerwarp.controller.ConfigController;
import pl.dream.dplayerwarp.controller.TeleportationController;
import pl.dream.dplayerwarp.data.Warp;
import pl.dream.dplayerwarp.listener.PlayerMoveListener;
import pl.dream.dplayerwarp.listener.PlayerTakeDamageListener;

import java.util.HashMap;

public final class DPlayerWarp extends JavaPlugin {

    public HashMap<String, Warp> warps;

    private SQLite database;
    private ConfigController configController;
    public TeleportationController teleportationController;
    
    public HashMap<String, Integer> prices;


    @Override
    public void onEnable() {
        database = new SQLite(this);
        database.connect();
        database.loadWarps();

        configController = new ConfigController(this);
        teleportationController = new TeleportationController(this);

        getServer().getPluginManager().registerEvents(new PlayerMoveListener(teleportationController), this);
        getServer().getPluginManager().registerEvents(new PlayerTakeDamageListener(teleportationController), this);

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
