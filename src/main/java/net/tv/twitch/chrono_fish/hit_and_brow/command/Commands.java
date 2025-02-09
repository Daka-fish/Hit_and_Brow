package net.tv.twitch.chrono_fish.hit_and_brow.command;

import net.kyori.adventure.text.Component;
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

    public Commands(Main main){this.game = main.getGame();}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if(sender instanceof Player){
            Player snd = (Player) sender;
            if(!snd.hasPermission("hab.op")){
                snd.sendMessage("§c権限がありません");
                return false;
            }
            if(command.getName().equalsIgnoreCase("hab")){
                if(args.length > 0){
                    switch(args[0]){
                        case "start":
                            game.start(snd);
                            break;

                        case "finish":
                            game.killGame(snd);
                            break;

                        case "list":
                            snd.sendMessage("§6[参加者]§f");
                            int index = 1;
                            for (GamePlayer participant : game.getParticipants()) {
                                snd.sendMessage(index+": "+participant.getPlayerName());
                            }
                            break;

                        case "help":
                            //ゲームについて説明する。最後に各モードについての説明はこちらのリンクを送る
                            break;

                        case "setting":
                            snd.sendMessage("§6[ゲーム設定]§f");
                            snd.sendMessage("参加可能人数: §e"+game.getMaxPlayerSize()+"§f人");
                            snd.sendMessage("最終ターン:   §e"+game.getMaxTurn()+"§fターン");
                            snd.sendMessage("同色重複:     §e"+((game.isColorRepeat()) ? "オン" : "オフ"));
                            snd.sendMessage("ゲームモード: §e"+game.getGameMode().get_mode_name());
                            break;

                        case "mode":
                            if(args.length==1){
                                snd.sendMessage("現在のゲームモード: "+game.getGameMode().get_mode_name());
                            }
                            if(args.length==2){
                                try{
                                    GameMode gamemode = GameMode.valueOf(args[1].toUpperCase());
                                    game.setGameMode(gamemode);
                                    snd.sendMessage("ゲームモードを§e"+game.getGameMode().get_mode_name()+"§fに変更しました");
                                } catch (IllegalArgumentException e) {
                                    snd.sendActionBar(Component.text("§cゲームモードが見つかりませんでした("+args[1]+")"));
                                    return false;
                                }
                            }
                            break;

                        case "color-repeat":
                            if(args.length==2){
                                switch(args[1]){
                                    case "true":
                                        game.setColorRepeat(true);
                                        snd.sendMessage("答えの同色をオンにしました");
                                        break;

                                    case "false":
                                        game.setColorRepeat(false);
                                        snd.sendMessage("答えの同色をオフにしました");
                                        break;

                                    default:
                                        snd.sendActionBar(Component.text("§c"+"trueかfalseを指定してください"));
                                }
                            }else{
                                snd.sendActionBar(Component.text("§c/hab color-repeat {true, false}"));
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
