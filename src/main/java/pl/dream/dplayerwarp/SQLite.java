package pl.dream.dplayerwarp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pl.dream.dplayerwarp.data.Warp;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

public class SQLite {
    private final DPlayerWarp plugin;
    private Connection con;

    public SQLite(DPlayerWarp plugin){
        this.plugin = plugin;
    }
    public void connect(){
        File dataFolder = new File(plugin.getDataFolder(), "playerWarp.db");

        if (!dataFolder.exists()){
            new File(plugin.getDataFolder().getPath()).mkdir();
        }

        String URL = "jdbc:sqlite:"+dataFolder;

        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(URL);
            createTables();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void disconnect(){
        try{
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createTables(){
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS PlayerWarp(ID INTEGER PRIMARY KEY AUTOINCREMENT, Nick varchar(30), World varchar(30), posX double, posY double, posZ double, yaw float, pitch float);");
            ps.execute();

            ps.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void loadWarps(){
        HashMap<String, Warp> warps = new HashMap<>();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;

                try{
                    ps = con.prepareStatement("SELECT * FROM PlayerWarp");
                    ResultSet rs = ps.executeQuery();

                    while(rs.next()){
                        String owner = rs.getString("Nick");
                        String worldName = rs.getString("World");
                        World world = Bukkit.getWorld(worldName);
                        if(world==null){
                            continue;
                        }
                        double posX = rs.getDouble("posX");
                        double posY = rs.getDouble("posY");
                        double posZ = rs.getDouble("posZ");
                        float yaw = rs.getFloat("yaw");
                        float pitch = rs.getFloat("pitch");

                        Location location = new Location(world, posX, posY, posZ, yaw, pitch);
                        warps.put(owner, new Warp(owner, location));
                    }

                    ps.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });

        plugin.warps = warps;
    }

    public void createNewWarp(String playerNick, Location location){
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;

                try{
                    ps = con.prepareStatement("INSERT INTO PlayerWarp(Nick, World, posX, posY, posZ, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?);");
                    ps.setString(1, playerNick);
                    ps.setString(2, location.getWorld().getName());
                    ps.setDouble(3, location.getX());
                    ps.setDouble(4, location.getY());
                    ps.setDouble(5, location.getZ());
                    ps.setFloat(6, location.getYaw());
                    ps.setFloat(7, location.getPitch());
                    ps.executeUpdate();

                    ps.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void removeWarp(String playerNick){
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;

                try{
                    ps = con.prepareStatement("DELETE FROM PlayerWarp WHERE Nick = ?");
                    ps.setString(1, playerNick);
                    ps.executeUpdate();

                    ps.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }
}

