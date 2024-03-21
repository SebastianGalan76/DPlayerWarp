package pl.dream.dplayerwarp.controller;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import pl.dream.dplayerwarp.DPlayerWarp;
import pl.dream.dplayerwarp.Locale;
import pl.dream.dplayerwarp.data.Warp;
import pl.dream.dreamlib.Message;

import java.util.HashMap;
import java.util.UUID;

public class TeleportationController {
    private final DPlayerWarp plugin;
    private final HashMap<UUID, BukkitTask> teleportation;

    private int delay;

    public TeleportationController(DPlayerWarp plugin){
        this.plugin = plugin;
        teleportation = new HashMap<>();
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public void startTeleportation(Player player, Warp warp){
        BukkitTask bukkitTask = plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if(player.isOnline()){
                    player.teleport(warp.getLocation());
                }

                teleportation.remove(player.getUniqueId());
                Message.sendMessage(player, Locale.TELEPORTATION_EXECUTED.toString()
                        .replace("{PLAYER}", warp.getOwner()));
            }
        }, delay*20L);


        teleportation.put(player.getUniqueId(), bukkitTask);
        Message.sendMessage(player, Locale.TELEPORTATION_STARTED.toString()
                .replace("{PLAYER}", warp.getOwner())
                .replace("{DELAY}", String.valueOf(delay)));
    }

    public void cancelTeleportation(Player player){
        if(!teleportation.containsKey(player.getUniqueId())){
            return;
        }

        BukkitTask task = teleportation.remove(player.getUniqueId());
        if(task!=null){
            Message.sendMessage(player, Locale.TELEPORTATION_CANCELED.toString());
            task.cancel();
        }
    }
}
