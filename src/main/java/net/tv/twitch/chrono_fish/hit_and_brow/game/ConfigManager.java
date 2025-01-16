package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {

    private final FileConfiguration config;
    private final Game game;

    public ConfigManager(Main main){
        this.game = main.getGame();
        File configFile = new File(main.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            main.saveDefaultConfig();
        }
        config = main.getConfig();
        main.saveDefaultConfig();
    }

    private Location getBaseLocation(){
        return new Location(Bukkit.getWorld("world"),
                config.getDouble("hab.base-location.x"),
                config.getDouble("hab.base-location.y"),
                config.getDouble("hab.base-location.z"));
    }

    private Location getCorrectLocation(){
        return new Location(Bukkit.getWorld("world"),
                config.getDouble("hab.correct-location.x"),
                config.getDouble("hab.correct-location.y"),
                config.getDouble("hab.correct-location.z"));
    }

    private GameMode getGameMode() {
        try{
            return GameMode.valueOf(config.getString("game.mode"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private int get_max_turn() {return config.getInt("game.max-turn-count");}

    private int get_max_player_size() {return config.getInt("game.max-player-size");}

    private boolean get_is_color_repeat() {return config.getBoolean("game.color-repeat");}

    public void loadOptions() {
        game.setGameMode(getGameMode());
        game.setBaseLocation(getBaseLocation());
        game.setCorrectLocation(getCorrectLocation());
        game.setMax_turn(get_max_turn());
        game.setMax_player_size(get_max_player_size());
        game.setColor_repeat(get_is_color_repeat());
    }

    public void saveOptions() {}
}
