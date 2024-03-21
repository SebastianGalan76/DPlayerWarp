package pl.dream.dplayerwarp.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.dream.dplayerwarp.DPlayerWarp;
import pl.dream.dplayerwarp.Locale;
import pl.dream.dplayerwarp.Utils;
import pl.dream.dplayerwarp.controller.TeleportationController;
import pl.dream.dplayerwarp.data.Warp;
import pl.dream.dreamlib.Message;

public class PWarpCommand implements CommandExecutor {
    private final DPlayerWarp plugin;
    private final TeleportationController teleportationController;

    public PWarpCommand(DPlayerWarp plugin, TeleportationController teleportationController){
        this.plugin = plugin;
        this.teleportationController =  teleportationController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(args.length==0){
            showHelp(sender);
            return true;
        }

        if(args.length>1){
            if(args[0].equalsIgnoreCase("create")){

                return true;
            }
            if(args[0].equalsIgnoreCase("delete")){

                return true;
            }
            if(args[0].equalsIgnoreCase("reload")){
                if(Utils.checkPermission(sender, "dplayerwarp.admin")){
                    plugin.reloadPlugin();
                    Message.sendMessage(sender, Locale.RELOAD.toString());

                    return true;
                }
            }

            if(!(sender instanceof Player)){
                Message.sendMessage(sender, Locale.ONLY_PLAYER.toString());
                return true;
            }

            if(!Utils.checkPermission(sender, "dplayerwarp.pwarp")){
                return true;
            }

            String warpName = args[0];
            Player player = (Player) sender;
            Warp warp = plugin.warps.get(warpName);

            if(warp==null){
                Message.sendMessage(sender, Locale.WARP_NOT_EXIST.toString()
                        .replace("{PLAYER}", warpName));
                return true;
            }

            teleportationController.startTeleportation(player, warp);
        }

        return true;
    }

    private void showHelp(@NotNull CommandSender sender){
        if(sender.hasPermission("dplayerwarp.admin")){
            Message.sendMessage(sender, Locale.COMMAND_HELP_ADMIN.getList());
        }
        else{
            Message.sendMessage(sender, Locale.COMMAND_HELP_PLAYER.getList());
        }
    }
}
