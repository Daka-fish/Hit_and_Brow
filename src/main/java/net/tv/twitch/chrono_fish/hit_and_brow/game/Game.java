package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.Manager.BlockManager;
import net.tv.twitch.chrono_fish.hit_and_brow.Manager.ConfigManager;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class Game {

    private final Main main;

    private final ArrayList<CustomPlayer> participants;
    private final ArrayList<CustomColor> correctColors;

    private final ConfigManager configManager;

    private boolean isRunning;
    private int turnCount;

    private CustomPlayer turnPlayer;

    private BlockManager blockManager;

    public Game(Main main){
        this.main = main;
        this.participants = new ArrayList<>(2);
        this.correctColors = new ArrayList<>(4);
        this.isRunning = false;
        this.turnCount = 0;
        this.configManager = new ConfigManager(main);
    }

    public ArrayList<CustomPlayer> getParticipants() {return participants;}
    public ArrayList<CustomColor> getCorrectColors() {return correctColors;}

    public ConfigManager getConfigManager() {return configManager;}

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
        if(isRunning){
            if(getTurnCount()==0){
                setTurnPlayer(participants.get(0));
            }else{
                int index = (participants.indexOf(getTurnPlayer()) + 1) % participants.size();
                setTurnPlayer(participants.get(index));
            }
            Player currentPlayer = getTurnPlayer().getPlayer();
            for(CustomColor cc: CustomColor.values()){
                if(cc.equals(CustomColor.BLACK)) continue;
                currentPlayer.getInventory().addItem(new ItemStack(cc.getMaterial(),4-getMaterialCount(currentPlayer, cc.getMaterial())));
            }
            currentPlayer.getInventory().addItem(HabItem.HAB_BLAZE_ROD());
        }
    }

    public int getMaterialCount(Player player, Material material){
        int count = 0;
        for(ItemStack item : player.getInventory().getContents()){
            if(item != null && item.getType() == material){
                count +=item.getAmount();
            }
        }
        return count;
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
            correctColors.add(i,colorPool.get(i));
        }
    }

    public void start(){
        if(!isRunning()){
            this.blockManager = new BlockManager(this);
            setRunning(true);
            setTurnCount(0);
            assignColors();
            Collections.shuffle(participants);

            blockManager.setBlackBlocks();

            sendMessage("§eゲーム開始！");
            if(configManager.getConsoleToggle()){
                main.consoleLog("This message is only for admin. If you want to hide this message, edit 'console' section in config.yml 'true' to 'false'");
                for(CustomColor cc : correctColors){
                    main.consoleLog(cc.getName());
                }
            }
            setNextPlayer();
            setTurnCount(getTurnCount()+1);
            sendMessage("[ターン" + getTurnCount() + "] §e" + getTurnPlayer().getPlayer().getName() +"§fのターン");
            participants.forEach(customPlayer -> customPlayer.getHabScoreboard().resetScoreboard());
        }
    }

    public void finish(){
        if(!isRunning) return;
        sendMessage("....");
        setRunning(false);
        Bukkit.getScheduler().runTaskLater(main,()->{
            participants.forEach(customPlayer -> customPlayer.getHabScoreboard().setCorrectColor());
            StringBuilder correctIs = new StringBuilder("正解は、");
            for(CustomColor customColor :correctColors){
                correctIs.append(customColor.getColorBlock());
            }
            sendMessage("ゲーム終了！§a"+getTurnPlayer().getPlayer().getName()+"§fの勝ち！");
            sendMessage(correctIs.toString());
            blockManager.openCorrectBlock();
            sendMessage("かかったターン数:§e "+getTurnCount());
        },40L);
    }
}
