package pl.dream.dplayerwarp.data;

import org.bukkit.Location;

public class Warp {
    private final String owner;
    private final Location location;

    public Warp(String owner, Location location){
        this.owner = owner;
        this.location = location;
    }

    public Location getLocation(){
        return location;
    }

    public String getOwner(){
        return owner;
    }
}
