package pl.dream.dplayerwarp.command.subcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.dream.dplayerwarp.DPlayerWarp;
import pl.dream.dplayerwarp.Locale;
import pl.dream.dplayerwarp.Utils;
import pl.dream.dreamlib.Message;

public class DeleteSubcommand {
    private final DPlayerWarp plugin;
    public DeleteSubcommand(DPlayerWarp plugin){
        this.plugin = plugin;
    }

    public void run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args){
        if(args.length==1){
            if(!(sender instanceof Player)){
                Message.sendMessage(sender, Locale.COMMAND_DELETE_CONSOLE_USAGE.toString());
                return;
            }

            Player player = (Player) sender;
            if(!plugin.warps.containsKey(player.getName())){
                Message.sendMessage(sender, Locale.COMMAND_DELETE_NOT_HAVE.toString());
                return;
            }

            deleteWarp(player.getName());
            Message.sendMessage(sender, Locale.COMMAND_DELETE_SUCCESS.toString());
        }
        if(args.length==2){
            if(Utils.checkPermission(sender, "dplayerwarp.admin")){
                if(!plugin.warps.containsKey(args[1])){
                    Message.sendMessage(sender, Locale.WARP_NOT_EXIST.toString());
                    return;
                }

                deleteWarp(args[1]);
                Message.sendMessage(sender, Locale.COMMAND_DELETE_SUCCESS.toString());
            }
        }
        else{
            showHelp(sender);
        }
    }

    private void deleteWarp(String playerName){
        plugin.database.removeWarp(playerName);
        plugin.warps.remove(playerName);
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
