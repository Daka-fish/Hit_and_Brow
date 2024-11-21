package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.Hit_and_Brow;
import net.tv.twitch.chrono_fish.hit_and_brow.Manager.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {

    private final Hit_and_Brow hit_and_brow;

    public Commands(Hit_and_Brow hit_and_brow){
        this.hit_and_brow = hit_and_brow;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if(sender instanceof Player){
            Player snd = (Player) sender;
            if(command.getName().equalsIgnoreCase("hab")){
                if(args.length > 0){
                    CommandManager commandManager = new CommandManager(snd,args,hit_and_brow.getHabGame());
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
