package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.tv.twitch.chrono_fish.hit_and_brow.HabColor;
import net.tv.twitch.chrono_fish.hit_and_brow.Hit_and_Brow;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import net.tv.twitch.chrono_fish.hit_and_brow.player.HabPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
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

    private final FileConfiguration config;

    public HabGame(Hit_and_Brow hit_and_brow){
        this.hit_and_brow = hit_and_brow;
        this.habPlayers = new ArrayList<>(2);
        this.correctColors = new ArrayList<>(4);
        this.isRunning = false;
        this.turnCount = 0;

        File configFile = new File(hit_and_brow.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            hit_and_brow.saveDefaultConfig();
        }
        this.config = hit_and_brow.getConfig();
        hit_and_brow.saveConfig();
    }

    public Hit_and_Brow getMain() {return hit_and_brow;}

    public ArrayList<HabPlayer> getHabPlayers() {return habPlayers;}
    public ArrayList<HabColor> getCorrectColors() {return correctColors;}

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean running) {isRunning = running;}

    public int getTurnCount() {return turnCount;}
    public void setTurnCount(int turnCount) {this.turnCount = turnCount;}

    public HabPlayer getTurnPlayer() {return turnPlayer;}
    public void setTurnPlayer(HabPlayer turnPlayer) {this.turnPlayer = turnPlayer;}

    public FileConfiguration getConfig() {return config;}

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
        }
    }

    public Location getBaseLocation(){
        return new Location(hit_and_brow.getServer().getWorld("world"),
                config.getDouble("hab.base-location.x"),
                config.getDouble("hab.base-location.y"),
                config.getDouble("hab.base-location.z"));
    }
    public Location getCorrectLocation(){
        return new Location(hit_and_brow.getServer().getWorld("world"),
                config.getDouble("hab.correct-location.x"),
                config.getDouble("hab.correct-location.y"),
                config.getDouble("hab.correct-location.z"));
    }

    public void start(){
        if(!isRunning()){
            if(getHabPlayers().size() > 2 || getHabPlayers().size() == 0){
                sendMessage("§cプレイヤーの人数を確認してください(現在の人数:§a"+getHabPlayers().size()+"§f人)");
                return;
            }

            if(getHabPlayers().size() <= 2){
                setRunning(true);
                setTurnCount(0);
                assignColors();
                sendMessage("§aゲームを開始します");
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

    public void setBlackBlocks(){
        Location baseLoc = getBaseLocation();
        for(int i=0; i<14; i++){
            for(int j=0; j<4; j++){
                if(!baseLoc.getBlock().getType().equals(Material.BLACK_WOOL)){
                    baseLoc.getBlock().setType(Material.BLACK_WOOL);
                }
                baseLoc.add(0,0,1);
            }
            baseLoc.add(2,0,-4);
        }
    }

    public void setBlackCorrectBlock(){
        Location correctLoc = getCorrectLocation();
        for(int j=0; j<4; j++){
            if(!correctLoc.getBlock().getType().equals(Material.BLACK_WOOL)){
                correctLoc.getBlock().setType(Material.BLACK_WOOL);
            }
            correctLoc.add(0,0,1);
        }
    }

    public void openCorrectBlock(){
        Location correctLoc = getCorrectLocation();
        for(int i=0; i<4; i++){
            correctLoc.getBlock().setType(correctColors.get(i).getMaterial());
            correctLoc.add(0,0,1);
        }
    }
}
