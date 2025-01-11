package net.tv.twitch.chrono_fish.hit_and_brow.Manager;

import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {

    private final FileConfiguration config;

    public ConfigManager(Main main){
        File configFile = new File(main.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            main.saveDefaultConfig();
        }
        config = main.getConfig();
        main.saveDefaultConfig();
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

    public boolean getConsoleToggle(){
        return config.getBoolean("hab.console");
    }
}
