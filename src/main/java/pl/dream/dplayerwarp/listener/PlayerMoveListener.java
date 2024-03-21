package pl.dream.dplayerwarp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.dream.dplayerwarp.controller.TeleportationController;

public class PlayerMoveListener implements Listener {
    private final TeleportationController teleportationController;

    public PlayerMoveListener(TeleportationController teleportationController){
        this.teleportationController = teleportationController;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(e.hasChangedBlock()){
            teleportationController.cancelTeleportation(e.getPlayer());
        }
    }
}
