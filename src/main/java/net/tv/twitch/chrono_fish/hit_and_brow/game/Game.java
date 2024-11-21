package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.CustomColor;
import net.tv.twitch.chrono_fish.hit_and_brow.Hit_and_Brow;
import net.tv.twitch.chrono_fish.hit_and_brow.Manager.BlockManager;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import net.tv.twitch.chrono_fish.hit_and_brow.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class Game {

    private final Hit_and_Brow hit_and_brow;

    private final ArrayList<CustomPlayer> participants;
    private final ArrayList<CustomColor> correctColors;

    private boolean isRunning;
    private int turnCount;

    private CustomPlayer turnPlayer;

    private BlockManager blockManager;

    public Game(Hit_and_Brow hit_and_brow){
        this.hit_and_brow = hit_and_brow;
        this.participants = new ArrayList<>(2);
        this.correctColors = new ArrayList<>(4);
        this.isRunning = false;
        this.turnCount = 0;
    }

    public Hit_and_Brow getMain() {return hit_and_brow;}

    public ArrayList<CustomPlayer> getParticipants() {return participants;}
    public ArrayList<CustomColor> getCorrectColors() {return correctColors;}

    public boolean isRunning() {return isRunning;}
    public void setRunning(boolean running) {isRunning = running;}

    public int getTurnCount() {return turnCount;}
    public void setTurnCount(int turnCount) {this.turnCount = turnCount;}

    public CustomPlayer getTurnPlayer() {return turnPlayer;}
    public void setTurnPlayer(CustomPlayer turnPlayer) {this.turnPlayer = turnPlayer;}

    public CustomPlayer getCustomPlayer(Player player){
        CustomPlayer target = null;
        for(CustomPlayer customPlayer : participants){
            if(customPlayer.getPlayer().equals(player)){
                target = customPlayer;
                break;
            }
        }
        return target;
    }

    public void sendMessage(String message){participants.forEach(customPlayer -> customPlayer.getPlayer().sendMessage(message));}

    public void setNextPlayer(){
        int index = (participants.indexOf(getTurnPlayer()) + 1) % participants.size();
        setTurnPlayer(participants.get(index));
    }

    public void assignColors(){
        correctColors.clear();
        ArrayList<CustomColor> colorPool = new ArrayList<>(6);
        colorPool.add(CustomColor.RED);
        colorPool.add(CustomColor.WHITE);
        colorPool.add(CustomColor.BLUE);
        colorPool.add(CustomColor.GREEN);
        colorPool.add(CustomColor.PINK);
        colorPool.add(CustomColor.YELLOW);
        Collections.shuffle(colorPool);
        for(int i=0; i<4; i++){
            correctColors.add(colorPool.get(i));
            hit_and_brow.consoleLog("color["+(i+1)+"] "+colorPool.get(i).getName());
        }
    }

    public void start(){
        if(!isRunning()){

            this.blockManager = new BlockManager(this);
            setRunning(true);
            setTurnCount(0);
            assignColors();

            setNextPlayer();

            blockManager.setBlackBlocks();

            participants.forEach(customPlayer -> {
                customPlayer.getPlayer().getInventory().addItem(new ItemStack(CustomColor.WHITE.getMaterial(),4));
                customPlayer.getPlayer().getInventory().addItem(new ItemStack(CustomColor.RED.getMaterial(),4));
                customPlayer.getPlayer().getInventory().addItem(new ItemStack(CustomColor.BLUE.getMaterial(),4));
                customPlayer.getPlayer().getInventory().addItem(new ItemStack(CustomColor.GREEN.getMaterial(),4));
                customPlayer.getPlayer().getInventory().addItem(new ItemStack(CustomColor.PINK.getMaterial(),4));
                customPlayer.getPlayer().getInventory().addItem(new ItemStack(CustomColor.YELLOW.getMaterial(),4));
                customPlayer.getPlayer().getInventory().addItem(HabItem.HAB_BLAZE_ROD());
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
            participants.forEach(customPlayer -> customPlayer.getHabScoreboard().setCorrectColor());
            StringBuilder correctIs = new StringBuilder("正解は、");
            for(CustomColor customColor :correctColors){
                correctIs.append(customColor.getColorBlock());
            }
            sendMessage(correctIs.toString());
            for(CustomColor customColor : correctColors){
                sendMessage(customColor.getName());
            }
            blockManager.openCorrectBlock();
            sendMessage("かかったターン数:§e "+getTurnCount());
        },40L);
    }
}
