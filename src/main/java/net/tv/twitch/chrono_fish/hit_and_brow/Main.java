package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private Game game;

    @Override
    public void onEnable() {
        this.game = new Game(this);
        Objects.requireNonNull(getCommand("hab")).setExecutor(new Commands(this));
        Bukkit.getPluginManager().registerEvents(new Events(game),this);
    }

    public Game getHabGame() {return game;}

    public void consoleLog(String message){getLogger().info(message);}
}
