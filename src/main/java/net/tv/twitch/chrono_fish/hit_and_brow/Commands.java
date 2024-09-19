package net.tv.twitch.chrono_fish.hit_and_brow;

import net.tv.twitch.chrono_fish.hit_and_brow.game.HabGame;
import net.tv.twitch.chrono_fish.hit_and_brow.player.HabPlayer;
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
                    HabGame habGame = hit_and_brow.getHabGame();
                    switch(args[0]){
                        case "start":
                            habGame.start();
                            break;

                        case "join":
                            for(HabPlayer habPlayer : habGame.getHabPlayers()){
                                if(habPlayer.getPlayer().equals(snd)){
                                    snd.sendMessage("§c既に参加しています");
                                    return false;
                                }
                            }
                            HabPlayer habPlayer = new HabPlayer(habGame, snd);
                            habGame.getHabPlayers().add(habPlayer);
                            snd.sendMessage("[Hit_and_brow] ゲームに参加しました");
                            break;

                        case "leave":
                            HabPlayer target = habGame.getHabPlayer(snd);
                            if(target != null){
                                habGame.getHabPlayers().remove(target);
                            }else{
                                snd.sendMessage("§cゲームに参加していません");
                            }
                            break;

                        case "finish":
                            habGame.finish();
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
