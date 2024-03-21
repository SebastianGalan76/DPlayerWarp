package pl.dream.dplayerwarp;

import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

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
}

