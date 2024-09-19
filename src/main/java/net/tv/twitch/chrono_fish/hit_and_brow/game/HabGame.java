package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.tv.twitch.chrono_fish.hit_and_brow.HabColor;
import net.tv.twitch.chrono_fish.hit_and_brow.Hit_and_Brow;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import net.tv.twitch.chrono_fish.hit_and_brow.player.HabPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;

public class HabGame {

    private final Hit_and_Brow hit_and_brow;

    private final ArrayList<HabPlayer> habPlayers;
    private final ArrayList<HabColor> correctColors;

    private boolean isRunning;
    private int turnCount;

    private HabPlayer turnPlayer;

    public HabGame(Hit_and_Brow hit_and_brow){
        this.hit_and_brow = hit_and_brow;
        this.habPlayers = new ArrayList<>(2);
        this.correctColors = new ArrayList<>(4);
        this.isRunning = false;
        this.turnCount = 0;
    }

    public ArrayList<HabPlayer> getHabPlayers() {return habPlayers;}
    public ArrayList<HabColor> getCorrectColors() {return correctColors;}

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean running) {isRunning = running;}

    public int getTurnCount() {return turnCount;}
    public void setTurnCount(int turnCount) {this.turnCount = turnCount;}

    public HabPlayer getTurnPlayer() {return turnPlayer;}
    public void setTurnPlayer(HabPlayer turnPlayer) {this.turnPlayer = turnPlayer;}

    public HabPlayer getHabPlayer(Player player){
        HabPlayer target = null;
        for(HabPlayer habPlayer : habPlayers){
            if(habPlayer.getPlayer().equals(player)){
                target = habPlayer;
                break;
            }
        }
        return target;
    }

    public void sendMessage(String message){habPlayers.forEach(habPlayer -> habPlayer.getPlayer().sendMessage(message));}
    public void sendTitle(String message) {
        Title.Times times = Title.Times.times(
                Duration.ofMillis(500),  // フェードイン時間
                Duration.ofMillis(1000),  // 表示される時間
                Duration.ofMillis(500)   // フェードアウト時間
        );

        habPlayers.forEach(habPlayer -> habPlayer.getPlayer().showTitle(Title.title(
                Component.text(message),
                Component.text(""),
                times
        )));
    }

    public void setNextPlayer(){
        int index = (habPlayers.indexOf(getTurnPlayer()) + 1) % habPlayers.size();
        setTurnPlayer(habPlayers.get(index));
        getTurnPlayer().getPlayer().sendMessage("[Hit_and_Brow] あなたのターンです");
    }

    public void assignColors(){
        ArrayList<HabColor> colorPool = new ArrayList<>(6);
        colorPool.add(HabColor.RED);
        colorPool.add(HabColor.WHITE);
        colorPool.add(HabColor.BLUE);
        colorPool.add(HabColor.GREEN);
        colorPool.add(HabColor.PINK);
        colorPool.add(HabColor.YELLOW);
        Collections.shuffle(colorPool);
        for(int i=0; i<4; i++){
            correctColors.add(colorPool.get(i));
        }
    }

    public void start(){
        if(!isRunning()){
            if(getHabPlayers().size() == 2){
                setRunning(true);
                setTurnCount(0);
                assignColors();
                sendMessage("§aゲームを開始します");
            }else if(getHabPlayers().size() == 1){
                setRunning(true);
                setTurnCount(0);
                assignColors();
                sendMessage("§e【ソロモード】§aでゲームを開始します");
            }

            if(getHabPlayers().size() > 2){
                sendMessage("§cプレイヤーの人数を確認してください(現在の人数:§a"+getHabPlayers().size()+"§f人)");
                return;
            }

            setNextPlayer();

            habPlayers.forEach(habPlayer -> {
                habPlayer.getPlayer().getInventory().setItem(0, HabItem.HAB_STICK());
                habPlayer.getPlayer().getInventory().setItem(1, new ItemStack(HabColor.WHITE.getMaterial(),4));
                habPlayer.getPlayer().getInventory().setItem(2, new ItemStack(HabColor.RED.getMaterial(),4));
                habPlayer.getPlayer().getInventory().setItem(3, new ItemStack(HabColor.BLUE.getMaterial(),4));
                habPlayer.getPlayer().getInventory().setItem(5, new ItemStack(HabColor.GREEN.getMaterial(),4));
                habPlayer.getPlayer().getInventory().setItem(6, new ItemStack(HabColor.PINK.getMaterial(),4));
                habPlayer.getPlayer().getInventory().setItem(7, new ItemStack(HabColor.YELLOW.getMaterial(),4));
                habPlayer.getPlayer().getInventory().setItem(8, HabItem.HAB_BLAZE_ROD());
                habPlayer.getHabScoreboard().resetScoreboard();
            });

        }else{
            sendMessage("§c既に進行中のゲームがあります");
        }
    }

    public void finish(){
        sendMessage("....");
        setRunning(false);
        Bukkit.getScheduler().runTaskLater(hit_and_brow,()->{
            sendTitle("ゲーム終了！");
        },20L);
        Bukkit.getScheduler().runTaskLater(hit_and_brow,()->{
            habPlayers.forEach(habPlayer -> habPlayer.getHabScoreboard().setCorrectColor());
            sendMessage("正解は、");
            for(HabColor habColor : correctColors){
                sendMessage(habColor.getName());
            }
            sendMessage("かかったターン数:§e "+getTurnCount());
        },40L);
    }
}
