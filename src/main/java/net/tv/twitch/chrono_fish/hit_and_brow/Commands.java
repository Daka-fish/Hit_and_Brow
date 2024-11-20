package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.player.CustomPlayer;
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
                    Game game = hit_and_brow.getHabGame();
                    switch(args[0]){
                        case "start":
                            if(game.getParticipants().size() == 0){
                                snd.sendMessage("§a参加しているプレイヤーがいません");
                                return false;
                            }
                            game.start();
                            break;

                        case "join":
                            for(CustomPlayer customPlayer : game.getParticipants()){
                                if(customPlayer.getPlayer().equals(snd)){
                                    snd.sendMessage("§c既に参加しています");
                                    return false;
                                }
                            }
                            CustomPlayer customPlayer = new CustomPlayer(game, snd);
                            game.getParticipants().add(customPlayer);
                            snd.sendMessage("[Hit_and_brow] ゲームに参加しました");
                            break;

                        case "leave":
                            CustomPlayer target = game.getCustomPlayer(snd);
                            if(target != null){
                                game.getParticipants().remove(target);
                            }else{
                                snd.sendMessage("§cゲームに参加していません");
                            }
                            break;

                        case "finish":
                            game.finish();
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
