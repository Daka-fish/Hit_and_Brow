package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameMode;
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
        this.config = main.getConfig();
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

    public GameMode getGameMode() {
        try{
            return GameMode.valueOf(config.getString("game.mode"));
        } catch (IllegalArgumentException e) {
            config.set("game.mode","NORMAL");
            return GameMode.NORMAL;
        }
    }

    public void setGameMode(GameMode gameMode) {config.set("game.mode",gameMode.name());}

    public int getMaxTurn() {return config.getInt("game.max-turn-count");}

    public int getMaxPlayerSize() {return config.getInt("game.max-player-size");}

    public boolean getIsColorRepeat() {return config.getBoolean("game.color-repeat", false);}
    public void setIsColorRepeat(Boolean isRepeat) {config.set("game.color-repeat",isRepeat);}

    public int getSpeedModeTimerSeconds() {return config.getInt("game.speed-mode-seconds");}
    public void setSpeedModeTimerSeconds(int seconds) {config.set("game.speed-mode-seconds", seconds);}
}
