package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.HabColor;
import net.tv.twitch.chrono_fish.hit_and_brow.Hit_and_Brow;
import net.tv.twitch.chrono_fish.hit_and_brow.Manager.BlockManager;
import net.tv.twitch.chrono_fish.hit_and_brow.Manager.ConfigManager;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import net.tv.twitch.chrono_fish.hit_and_brow.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Game {

    private final Hit_and_Brow hit_and_brow;

    private final ArrayList<CustomPlayer> customPlayers;
    private final ArrayList<HabColor> correctColors;

    private boolean isRunning;
    private int turnCount;

    private CustomPlayer turnPlayer;

    private BlockManager blockManager;

    public Game(Hit_and_Brow hit_and_brow){
        this.hit_and_brow = hit_and_brow;
        this.customPlayers = new ArrayList<>(2);
        this.correctColors = new ArrayList<>(4);
        this.isRunning = false;
        this.turnCount = 0;
    }

    public Hit_and_Brow getMain() {return hit_and_brow;}

    public ArrayList<CustomPlayer> getHabPlayers() {return customPlayers;}
    public ArrayList<HabColor> getCorrectColors() {return correctColors;}

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean running) {isRunning = running;}

    public int getTurnCount() {return turnCount;}
    public void setTurnCount(int turnCount) {this.turnCount = turnCount;}

    public CustomPlayer getTurnPlayer() {return turnPlayer;}
    public void setTurnPlayer(CustomPlayer turnPlayer) {this.turnPlayer = turnPlayer;}

    public CustomPlayer getHabPlayer(Player player){
        CustomPlayer target = null;
        for(CustomPlayer customPlayer : customPlayers){
            if(customPlayer.getPlayer().equals(player)){
                target = customPlayer;
                break;
            }
        }
        return target;
    }

    public void sendMessage(String message){customPlayers.forEach(customPlayer -> customPlayer.getPlayer().sendMessage(message));}

    public void setNextPlayer(){
        int index = (customPlayers.indexOf(getTurnPlayer()) + 1) % customPlayers.size();
        setTurnPlayer(customPlayers.get(index));
        getTurnPlayer().getPlayer().sendMessage("[Hit_and_Brow] あなたのターンです");
    }

    public void assignColors(){
        correctColors.clear();
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
            hit_and_brow.consoleLog("color["+(i+1)+"] "+colorPool.get(i).getName());
        }
    }

    public void start(){
        if(!isRunning()){
            if(getHabPlayers().size() == 0){
                sendMessage("§cプレイヤーの人数を確認してください(現在の人数:§a"+getHabPlayers().size()+"§c人)");
                return;
            }
            this.blockManager = new BlockManager(this);
            setRunning(true);
            setTurnCount(0);
            assignColors();

            setNextPlayer();

            blockManager.setBlackBlocks();

            customPlayers.forEach(customPlayer -> {
                customPlayer.getPlayer().getInventory().setItem(0, HabItem.HAB_STICK());
                customPlayer.getPlayer().getInventory().setItem(1, new ItemStack(HabColor.WHITE.getMaterial(),4));
                customPlayer.getPlayer().getInventory().setItem(2, new ItemStack(HabColor.RED.getMaterial(),4));
                customPlayer.getPlayer().getInventory().setItem(3, new ItemStack(HabColor.BLUE.getMaterial(),4));
                customPlayer.getPlayer().getInventory().setItem(5, new ItemStack(HabColor.GREEN.getMaterial(),4));
                customPlayer.getPlayer().getInventory().setItem(6, new ItemStack(HabColor.PINK.getMaterial(),4));
                customPlayer.getPlayer().getInventory().setItem(7, new ItemStack(HabColor.YELLOW.getMaterial(),4));
                customPlayer.getPlayer().getInventory().setItem(8, HabItem.HAB_BLAZE_ROD());
                customPlayer.getHabScoreboard().resetScoreboard();
            });

        }else{
            sendMessage("§c既に進行中のゲームがあります");
        }
    }

    public void finish(){
        sendMessage("....");
        setRunning(false);
        Bukkit.getScheduler().runTaskLater(hit_and_brow,()->{
            customPlayers.forEach(customPlayer -> customPlayer.getHabScoreboard().setCorrectColor());
            StringBuilder correctIs = new StringBuilder("正解は、");
            for(HabColor habColor:correctColors){
                correctIs.append(habColor.getColorBlock());
            }
            sendMessage(correctIs.toString());
            for(HabColor habColor : correctColors){
                sendMessage(habColor.getName());
            }
            blockManager.openCorrectBlock();
            sendMessage("かかったターン数:§e "+getTurnCount());
        },40L);
    }
}
