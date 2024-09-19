package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.game.HabGame;
import net.tv.twitch.chrono_fish.hit_and_brow.game.HabListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hit_and_Brow extends JavaPlugin {

    private HabGame habGame;

    @Override
    public void onEnable() {
        this.habGame = new HabGame(this);
        getCommand("hab").setExecutor(new Commands(this));
        Bukkit.getPluginManager().registerEvents(new HabListener(habGame),this);
    }

    public HabGame getHabGame() {return habGame;}
}
