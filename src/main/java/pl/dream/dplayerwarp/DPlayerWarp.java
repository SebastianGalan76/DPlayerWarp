package pl.dream.dplayerwarp;

import org.bukkit.plugin.java.JavaPlugin;

public final class DPlayerWarp extends JavaPlugin {

    private SQLite database;

    @Override
    public void onEnable() {
        database = new SQLite(this);
        database.connect();


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
