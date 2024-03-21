package pl.dream.dplayerwarp.controller;

import org.bukkit.configuration.file.FileConfiguration;
import pl.dream.dplayerwarp.DPlayerWarp;

import java.util.HashMap;

public class ConfigController {
    private final DPlayerWarp plugin;

    public ConfigController(DPlayerWarp plugin){
        this.plugin = plugin;
    }

    public void loadPlugin(){
        FileConfiguration config = plugin.getConfig();

        plugin.teleportationController.setDelay(config.getInt("teleportationDelay"));

        plugin.prices = new HashMap<>();
        for(String rank:config.getConfigurationSection("prices.token").getKeys(false)){
            int price = config.getInt("prices.token."+rank);

            plugin.prices.put(rank, price);
        }
    }
}
