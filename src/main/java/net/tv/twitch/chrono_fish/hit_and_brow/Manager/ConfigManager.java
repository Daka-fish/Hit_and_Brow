package net.tv.twitch.chrono_fish.hit_and_brow.Manager;

import net.tv.twitch.chrono_fish.hit_and_brow.Hit_and_Brow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {

    private final FileConfiguration config;

    public ConfigManager(Hit_and_Brow hit_and_brow){
        File configFile = new File(hit_and_brow.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            hit_and_brow.saveDefaultConfig();
        }
        config = hit_and_brow.getConfig();
        hit_and_brow.saveDefaultConfig();
    }

    public Location getBaseLocation(){
        return new Location(Bukkit.getWorld("world"),
                config.getDouble("hab.base-location.x"),
                config.getDouble("hab.base-location.y"),
                config.getDouble("hab.base-location.z"));
    }

    public Location getCorrectLocation(){
        return new Location(Bukkit.getWorld("world"),
                config.getDouble("hab.correct-location.x"),
                config.getDouble("hab.correct-location.y"),
                config.getDouble("hab.correct-location.z"));
    }
}
