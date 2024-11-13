package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.game.HabListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hit_and_Brow extends JavaPlugin {

    private Game game;

    @Override
    public void onEnable() {
        this.game = new Game(this);
        getCommand("hab").setExecutor(new Commands(this));
        Bukkit.getPluginManager().registerEvents(new HabListener(game),this);
    }

    public Game getHabGame() {return game;}

    public void consoleLog(String message){getLogger().info(message);}
}
