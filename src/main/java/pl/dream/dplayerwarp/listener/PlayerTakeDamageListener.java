package pl.dream.dplayerwarp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.dream.dplayerwarp.controller.TeleportationController;

public class PlayerTakeDamageListener implements Listener {
    private final TeleportationController teleportationController;

    public PlayerTakeDamageListener(TeleportationController teleportationController){
        this.teleportationController = teleportationController;
    }
    @EventHandler(ignoreCancelled = true)
    public void onDamageTaken(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            teleportationController.cancelTeleportation((Player) e.getEntity());
        }
    }
}
