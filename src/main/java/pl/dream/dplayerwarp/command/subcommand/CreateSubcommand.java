package pl.dream.dplayerwarp.command.subcommand;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.dream.dplayerwarp.DPlayerWarp;
import pl.dream.dplayerwarp.Locale;
import pl.dream.dplayerwarp.data.Warp;
import pl.dream.dreamlib.Message;
import pl.dream.dtokenmanager.TokenEconomy;
import pl.dream.dtokenmanager.exception.NumberLessThanZeroException;

public class CreateSubcommand {
    private final DPlayerWarp plugin;
    public CreateSubcommand(DPlayerWarp plugin){
        this.plugin = plugin;
    }

    public void run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args){
        if(args.length==1){
            if(!(sender instanceof Player)){
                Message.sendMessage(sender, Locale.ONLY_PLAYER.toString());
                return;
            }
            if(!sender.hasPermission("dplayerwarp.pwarp.create")){
                Message.sendMessage(sender, Locale.NO_REQUIRED_RANK.toString());
                return;
            }

            Player player = (Player) sender;
            int price = getPrice(sender);
            if(price==-1){
                Message.sendMessage(sender, Locale.NO_REQUIRED_RANK.toString());
                return;
            }

            if(plugin.warps.containsKey(player.getName())){
                Message.sendMessage(player, Locale.COMMAND_CREATE_ALREADY_HAVE.toString());
                return;
            }

            if(TokenEconomy.getBalance(player.getUniqueId())<price){
                Message.sendMessage(sender, Locale.NOT_ENOUGH_TOKENS.toString());
                return;
            }

            if(!onOwnCuboid(player)){
                Message.sendMessage(player, Locale.ONLY_OWN_CUBOID.toString());
                return;
            }

            try{
                TokenEconomy.withdraw(player.getUniqueId(), price);
                createNewWarp(player.getName(), player.getLocation());
                Message.sendMessage(player, Locale.COMMAND_CREATE_SUCCESS.toString());
            }catch (NumberLessThanZeroException e){
                Message.sendMessage(sender, Locale.NO_REQUIRED_RANK.toString());
            }
        }
        else{
            showHelp(sender);
        }
    }

    private void createNewWarp(String owner, Location location){
        plugin.warps.put(owner, new Warp(owner, location));
        plugin.database.createNewWarp(owner, location);
    }
    private int getPrice(CommandSender sender){
        int finalPrice = -1;

        for(String rank:plugin.prices.keySet()){
            if(sender.hasPermission("dplayerwarp.rank."+rank)){
                int price = plugin.prices.get(rank);
                if(price < finalPrice || finalPrice == -1){
                    finalPrice = price;
                }
            }

        }

        return finalPrice;
    }

    private boolean onOwnCuboid(Player player){
        Location location = player.getLocation();
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
        com.sk89q.worldguard.LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);

        for(ProtectedRegion region:set){
            if(region.isMember(localPlayer)){
                return true;
            }
        }
        return false;
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
