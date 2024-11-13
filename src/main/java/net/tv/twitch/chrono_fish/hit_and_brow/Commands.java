package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.player.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final Hit_and_Brow hit_and_brow;

    public Commands(Hit_and_Brow hit_and_brow){
        this.hit_and_brow = hit_and_brow;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player snd = (Player) sender;
            if(command.getName().equalsIgnoreCase("hab")){
                if(args.length > 0){
                    Game game = hit_and_brow.getHabGame();
                    switch(args[0]){
                        case "start":
                            game.start();
                            break;

                        case "join":
                            for(CustomPlayer customPlayer : game.getHabPlayers()){
                                if(customPlayer.getPlayer().equals(snd)){
                                    snd.sendMessage("§c既に参加しています");
                                    return false;
                                }
                            }
                            CustomPlayer customPlayer = new CustomPlayer(game, snd);
                            game.getHabPlayers().add(customPlayer);
                            snd.sendMessage("[Hit_and_brow] ゲームに参加しました");
                            break;

                        case "leave":
                            CustomPlayer target = game.getHabPlayer(snd);
                            if(target != null){
                                game.getHabPlayers().remove(target);
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
