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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {

    private final Game game;

    public Commands(Main main){this.game = main.getGame();}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if(sender instanceof Player){
            Player snd = (Player) sender;
            if(command.getName().equalsIgnoreCase("hab")){
                if(!snd.hasPermission("hab.op")){
                    snd.sendMessage("§c権限がありません");
                    return false;
                }
                if(args.length > 0){
                    switch(args[0]){
                        case "book":
                            ItemStack book = game.getMain().getCustomItems().GameSettingBook();
                            snd.getInventory().addItem(book);
                            snd.sendMessage(book.displayName().append(Component.text("を追加しました")));
                            break;

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
                            Player player = (Player) sender; // 実行者がプレイヤーであることを確認
                            List<String> helpMessages = new ArrayList<>();

                            if (args.length == 1) { // `/hab help` の場合（基本ヘルプ）
                                helpMessages.add("§6[Hit & Brow Plugin Help]");
                                helpMessages.add("§e/hab start §f- ゲームを開始する");
                                helpMessages.add("§e/hab finish §f- ゲームを強制終了する");
                                helpMessages.add("§e/hab book §f- 設定ブックを取得");
                                helpMessages.add("§e/hab list §f- 参加者一覧を表示");
                                helpMessages.add("§e/hab setting §f- 設定オプションを開く");
                                helpMessages.add("§e/hab mode [normal/speed] §f- ゲームモードを変更");
                                helpMessages.add("§e/hab color-repeat [true/false] §f- 色繰り返し設定を変更");
                                helpMessages.add("§e/hab speed-time [秒数] §f- スピードモードの秒数を変更");
                                helpMessages.add("§e詳細は /hab help <コマンド> で確認できます。");

                            } else if (args.length == 2) { // `/hab help <項目>` の場合
                                switch (args[1].toLowerCase()) {
                                    case "start":
                                        helpMessages.add("§e/hab start §f- ゲームを開始します");
                                        helpMessages.add("  - プレイヤーがいないと開始できません");
                                        break;

                                    case "finish":
                                        helpMessages.add("§e/hab finish §f- ゲームを強制終了します");
                                        break;

                                    case "book":
                                        helpMessages.add("§e/hab book §f- 設定ブックを取得");
                                        break;

                                    case "list":
                                        helpMessages.add("§e/hab list §f- 参加者一覧を表示");
                                        break;

                                    case "setting":
                                        helpMessages.add("§e/hab setting §f- 設定オプションを開く");
                                        break;

                                    case "mode":
                                        helpMessages.add("§e/hab mode [normal/speed] §f- ゲームモードを切り替えます。");
                                        helpMessages.add("  - §6normal§f: 通常モード");
                                        helpMessages.add("  - §6speed§f: スピードモード（制限時間あり）");
                                        break;

                                    case "color-repeat":
                                        helpMessages.add("§e/hab color-repeat [true/false] §f- 色の繰り返しを設定します。");
                                        helpMessages.add("  - §6true§f: 同じ色が出る可能性あり");
                                        helpMessages.add("  - §6false§f: 同じ色は出ない");
                                        break;

                                    case "speed-time":
                                        helpMessages.add("§e/hab speed-time [秒数] §f- スピードモードの時間を変更します。");
                                        break;

                                    default:
                                        helpMessages.add("§c指定したヘルプは存在しません。");
                                        helpMessages.add("§e/hab help §fで確認してください。");
                                        break;
                                }
                            }

                            for (String message : helpMessages) {
                                player.sendMessage(message);
                            }
                            return true;


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

                        case "speed-time":
                            if(args.length==2){
                                if(args[1].equalsIgnoreCase("+")){
                                    game.setSpeedModeSeconds(game.getSpeedModeSeconds()+5);
                                    snd.sendMessage("スピードモードの制限時間を§e"+game.getSpeedModeSeconds()+"§f秒に設定しました");
                                    return false;
                                }
                                if(args[1].equalsIgnoreCase("-")){
                                    game.setSpeedModeSeconds(game.getSpeedModeSeconds()-5);
                                    snd.sendMessage("スピードモードの制限時間を§e"+game.getSpeedModeSeconds()+"§f秒に設定しました");
                                    return false;
                                }

                                try{
                                    int time = Integer.parseInt(args[1]);
                                    game.setSpeedModeSeconds(time);
                                    snd.sendMessage("スピードモードの制限時間を§e"+game.getSpeedModeSeconds()+"§f秒に設定しました");
                                    return false;
                                } catch (NumberFormatException e) {
                                    snd.sendActionBar(Component.text("§c数字を指定して下さい"));
                                }
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
