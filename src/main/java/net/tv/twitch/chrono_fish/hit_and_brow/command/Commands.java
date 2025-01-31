package net.tv.twitch.chrono_fish.hit_and_brow.command;

import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.game.GamePlayer;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameMode;
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
                            gamePlayer.sendMessage("§6[参加者]§f");
                            int index = 1;
                            for (GamePlayer participant : game.getParticipants()) {
                                gamePlayer.sendMessage(index+": "+participant.getName());
                            }
                            break;

                        case "help":
                            break;

                        case "setting":
                            gamePlayer.sendMessage("§6[ゲーム設定]§f");
                            gamePlayer.sendMessage("参加可能人数: §e"+game.getMax_player_size()+"§f人");
                            gamePlayer.sendMessage("最終ターン:   §e"+game.getMax_turn()+"§fターン");
                            gamePlayer.sendMessage("同色重複:     §e"+((game.isColor_repeat()) ? "オン" : "オフ"));
                            gamePlayer.sendMessage("ゲームモード: §e"+game.getGameMode().get_mode_name());
                            break;

                        case "mode":
                            if(args.length==1){
                                gamePlayer.sendMessage("現在のゲームモード: §e"+game.getGameMode().get_mode_name());
                            }
                            if(args.length==2){
                                try{
                                    GameMode gamemode = GameMode.valueOf(args[1].toUpperCase());
                                    game.setGameMode(gamemode);
                                    gamePlayer.sendMessage("ゲームモードを§e"+game.getGameMode().get_mode_name()+"§fに変更しました");
                                } catch (IllegalArgumentException e) {
                                    gamePlayer.sendActionBar("§cゲームモードが見つかりませんでした("+args[1]+")");
                                    return false;
                                }
                            }
                            break;

                        case "color-repeat":
                            if(args.length==2){
                                switch(args[1]){
                                    case "true":
                                        game.setColor_repeat(true);
                                        gamePlayer.sendMessage("同色重複をオンにしました");
                                        break;

                                    case "false":
                                        game.setColor_repeat(false);
                                        gamePlayer.sendMessage("同色重複をオフにしました");
                                        break;

                                    default:
                                        gamePlayer.sendActionBar("§c"+"trueかfalseを指定してください");
                                }
                            }else{
                                gamePlayer.sendActionBar("§c/hab color-repeat {true, false}");
                            }
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
