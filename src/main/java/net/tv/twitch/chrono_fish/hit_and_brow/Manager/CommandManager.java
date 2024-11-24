package net.tv.twitch.chrono_fish.hit_and_brow.Manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import net.tv.twitch.chrono_fish.hit_and_brow.player.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class CommandManager {

    private final Player sender;
    private final Game game;

    public CommandManager(Player sender, Game game){
        this.sender = sender;
        this.game = game;
    }

    public void help(){
        Component url = Component.text("こちら")
                .clickEvent(ClickEvent.openUrl("https://youtu.be/remMhfUJ630"))
                .hoverEvent(HoverEvent.showText(Component.text("ルール説明動画を見ます")))
                .decorate(TextDecoration.UNDERLINED);
        sender.sendMessage("\n§e==ヒット&ブローへようこそ==");
        sender.sendMessage("§a◆コマンド一覧");
        sender.sendMessage(String.format("%-13s %-25s", "§6/hab help","§f--このプラグインについて説明します"));
        sender.sendMessage(String.format("%-13s %-25s", "§6/hab book","§f--ゲームを管理する本をもらえます"));
        sender.sendMessage("\n§a◆ルール");
        sender.sendMessage(Component.text("ルールは").append(url).append(Component.text("(YouTubeに飛びます)")));
        sender.sendMessage("\n§a◆ゲームの始め方");
        sender.sendMessage("bookコマンドで管理用の本を取得してください。" +
                "\n参加しているプレイヤーを確認してゲームを開始してください" +
                "\n§aまたゲーム中は羊毛を直接右クリックすることで、ブロックを入れ替えることができます");
        sender.sendMessage("§e======================");
    }

    public void book(){
        sender.getInventory().addItem(HabItem.HAB_BOOK());
        sender.sendMessage("§e「管理用の本」§fを与えました");
    }

    public void list(){
        sender.sendMessage("\n§e=参加者一覧を表示します=");
        for(int i=0; i< game.getParticipants().size(); i++){
            sender.sendMessage("[プレイヤー"+(i+1)+"] "+game.getParticipants().get(i).getPlayer().getName());
        }
    }

    public void join(){
        for(CustomPlayer customPlayer : game.getParticipants()){
            if(customPlayer.getPlayer().equals(sender)){
                sender.sendMessage("§c既に参加しています");
                return;
            }
        }
        CustomPlayer customPlayer = new CustomPlayer(game, sender);
        game.getParticipants().add(customPlayer);
        sender.sendMessage("§eゲームに参加しました");
    }

    public void leave(){
        CustomPlayer target = game.getCustomPlayer(sender);
        if(target != null){
            game.getParticipants().remove(target);
        }else{
            sender.sendMessage("§cゲームに参加していません");
            return;
        }
        sender.sendMessage("§eゲームから退室しました");
        sender.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }

    public void start(){
        if(game.getParticipants().size() == 0){
            sender.sendMessage("§c参加しているプレイヤーがいないため、ゲームを開始できません");
            return;
        }
        game.start();
    }

    public void finish(){
        if(game.getParticipants().size() == 0){
            sender.sendMessage("§c進行中のゲームがありません");
            return;
        }
        game.finish();
    }
}
