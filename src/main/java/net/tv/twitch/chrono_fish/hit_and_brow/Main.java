package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.game.ConfigManager;
import net.tv.twitch.chrono_fish.hit_and_brow.command.Commands;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Events;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private Game game;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.game = new Game(this);
        this.configManager = new ConfigManager(this);
        configManager.loadOptions();
        Objects.requireNonNull(getCommand("hab")).setExecutor(new Commands(this));
        Bukkit.getPluginManager().registerEvents(new Events(game),this);

        game.getSidebar().resetModeScore(game.getGameMode());
    }

    @Override
    public void onDisable(){
        configManager.saveOptions();
    }

    public Game getGame() {return game;}
}
