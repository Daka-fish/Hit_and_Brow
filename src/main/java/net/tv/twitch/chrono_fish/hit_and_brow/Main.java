package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.command.TabCompleter;
import net.tv.twitch.chrono_fish.hit_and_brow.game.ConfigManager;
import net.tv.twitch.chrono_fish.hit_and_brow.command.Commands;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Events;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private Game game;
    private CustomItems customItems;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.game = new Game(this);
        this.customItems = new CustomItems();
        this.configManager = new ConfigManager(this);
        configManager.loadOptions();
        getCommand("hab").setExecutor(new Commands(this));
        getCommand("hab").setTabCompleter(new TabCompleter());
        Bukkit.getPluginManager().registerEvents(new Events(this),this);
        customItems.setGameSettingBook();
        game.getSidebar().resetModeScore(game.getGameMode());
        game.getSidebar().resetColorRepeat(game.isColorRepeat());
    }

    @Override
    public void onDisable(){
        game.setBlackBlocks();
        configManager.saveOptions();
        saveConfig();
    }

    public Game getGame() {return game;}
    public CustomItems getCustomItems() {return customItems;}
    public ConfigManager getConfigManager() {return configManager;}
}
