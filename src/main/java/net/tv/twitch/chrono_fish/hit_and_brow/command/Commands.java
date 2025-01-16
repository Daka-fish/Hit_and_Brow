package net.tv.twitch.chrono_fish.hit_and_brow.command;

import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.game.GamePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {

    private final Game game;

    public Commands(Main main){
        this.game = main.getGame();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if(sender instanceof Player){
            Player snd = (Player) sender;
            if(command.getName().equalsIgnoreCase("hab")){
                if(args.length > 0){
                    GamePlayer gamePlayer = game.getGamePlayer(snd);
                    if (gamePlayer==null) return false;
                    switch(args[0]){
                        case "start":
                            game.start(gamePlayer);
                            break;

                        case "finish":
                            game.finish(gamePlayer);
                            break;

                        case "list":
                            gamePlayer.sendMessage("[参加者]");
                            int index = 1;
                            for (GamePlayer participant : game.getParticipants()) {
                                gamePlayer.sendMessage(index+": "+participant.getName());
                            }
                            break;

                        case "help":
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
