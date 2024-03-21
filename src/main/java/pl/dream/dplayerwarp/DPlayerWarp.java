package pl.dream.dplayerwarp;

import org.bukkit.plugin.java.JavaPlugin;
import pl.dream.dplayerwarp.data.Warp;

import java.util.HashMap;

public final class DPlayerWarp extends JavaPlugin {

    public HashMap<String, Warp> warps;

    private SQLite database;

    @Override
    public void onEnable() {
        database = new SQLite(this);
        database.connect();
        database.loadWarps();

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

    }
}
