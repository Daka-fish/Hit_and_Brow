package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.Manager.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {

    private final Main main;

    public Commands(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if(sender instanceof Player){
            Player snd = (Player) sender;
            if(command.getName().equalsIgnoreCase("hab")){
                if(args.length > 0){
                    CommandManager commandManager = new CommandManager(snd, main.getHabGame());
                    switch(args[0]){
                        case "start":
                            commandManager.start();
                            break;

                        case "finish":
                            commandManager.finish();
                            break;

                        case "join":
                            commandManager.join();
                            break;

                        case "leave":
                            commandManager.leave();
                            break;

                        case "list":
                            commandManager.list();
                            break;

                        case "book":
                            commandManager.book();
                            break;

                        case "help":
                            commandManager.help();
                            break;

                        default:
                            snd.sendMessage("§c不明なコマンド");
                    }
                }
            }
        }
        return false;
    }
}
