package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            config.set("game.mode","NORMAL");
            return GameMode.NORMAL;
        }
    }

    private void setGameMode() {config.set("game.mode",game.getGameMode().name());}

    private int get_max_turn() {return config.getInt("game.max-turn-count");}

    private int get_max_player_size() {return config.getInt("game.max-player-size");}

    private boolean get_is_color_repeat() {return config.getBoolean("game.color-repeat", false);}

    private void set_is_color_repeat() {config.set("game.color-repeat",game.isColorRepeat());}

    private int getSpeedModeTimerSeconds() {return config.getInt("game.speed-mode-seconds");}
    private void setSpeedModeTimerSeconds() {config.set("game.speed-mode-seconds", game.getSpeedModeSeconds());}

    public List<String> getLastParticipants() {return config.getStringList("last-participants");}
    public void saveParticipants() {
        List<String> participantsNames = new ArrayList<>();
        for(GamePlayer participant : game.getParticipants()) participantsNames.add(participant.getPlayerName());
        config.set("last-participants",participantsNames);
        game.getMain().saveConfig();
    }

    public void loadOptions() {
        game.setGameMode(getGameMode());
        game.setBaseLocation(getBaseLocation());
        game.setCorrectLocation(getCorrectLocation());
        game.setMaxTurn(get_max_turn());
        game.setMaxPlayerSize(get_max_player_size());
        game.setColorRepeat(get_is_color_repeat());
        game.setSpeedModeSeconds(getSpeedModeTimerSeconds());
    }

    public void saveOptions() {
        setGameMode();
        setSpeedModeTimerSeconds();
        set_is_color_repeat();
        game.getMain().saveConfig();
    }
}
