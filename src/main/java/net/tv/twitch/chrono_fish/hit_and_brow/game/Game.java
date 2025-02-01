package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameColor;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class Game {

    private final Main main;
    private final ArrayList<GamePlayer> participants;
    private final ArrayList<GameColor> correctColors;
    private boolean isRunning;

    private int turnCount;
    private GamePlayer turnPlayer;

    private GameMode gameMode;
    private Location baseLocation;
    private Location correctLocation;
    private int max_player_size;
    private int max_turn;
    private boolean color_repeat;

    public Game(Main main){
        this.main = main;
        this.participants = new ArrayList<>(2);
        this.correctColors = new ArrayList<>(4);
        this.isRunning = false;
    }

    public Main getMain() {return main;}

    public ArrayList<GamePlayer> getParticipants() {return participants;}
    public ArrayList<GameColor> getCorrectColors() {return correctColors;}

    public boolean isRunning() {return isRunning;}

    public int getTurnCount() {return turnCount;}
    public void setTurnCount(int turnCount) {this.turnCount = turnCount;}

    public GamePlayer getTurnPlayer() {return turnPlayer;}
    public void setTurnPlayer(GamePlayer turnPlayer) {this.turnPlayer = turnPlayer;}

    public GameMode getGameMode() {return gameMode;}
    public void setGameMode(GameMode gameMode) {this.gameMode = gameMode;}

    public Location getBaseLocation() {return baseLocation;}
    public void setBaseLocation(Location baseLocation) {this.baseLocation = baseLocation;}

    public Location getCurrentLocation() {return baseLocation.clone().add(2*(turnCount-1),0,0);}

    public Location getCorrectLocation() {return correctLocation;}
    public void setCorrectLocation(Location correctLocation) {this.correctLocation = correctLocation;}

    public int getMax_player_size() {return max_player_size;}
    public void setMax_player_size(int max_player_size) {this.max_player_size = max_player_size;}

    public int getMax_turn() {return max_turn;}
    public void setMax_turn(int max_turn) {this.max_turn = max_turn;}

    public boolean isColor_repeat() {return color_repeat;}
    public void setColor_repeat(boolean color_repeat) {this.color_repeat = color_repeat;}

    public GamePlayer getGamePlayer(Player player){
        for(GamePlayer gamePlayer : participants){
            if(gamePlayer.getPlayer().equals(player)) return gamePlayer;
        }
        return null;
    }

    public void join(GamePlayer gamePlayer) {
        if (!participants.contains(gamePlayer)){
            participants.add(gamePlayer);
            broadCastMessage("§e"+gamePlayer.getName()+"§fがゲームに参加しました");
        }
    }

    public void leave(GamePlayer gamePlayer) {
        if (participants.contains(gamePlayer)){
            participants.remove(gamePlayer);
            broadCastMessage("§e"+gamePlayer.getName()+"§fがゲームから退室しました");
        }
    }

    public void broadCastMessage(String message){participants.forEach(gamePlayer -> gamePlayer.sendMessage(((!isRunning) ? "[game] ":"")+message));}

    public void setNextPlayer(){
        if(isRunning){
            if(getTurnCount()==1){
                setTurnPlayer(participants.get(0));
            }else{
                int index = (participants.indexOf(getTurnPlayer()) + 1) % participants.size();
                setTurnPlayer(participants.get(index));
            }
            Player currentPlayer = turnPlayer.getPlayer();
            for(GameColor cc: GameColor.values()){
                if(cc.equals(GameColor.BLACK)) continue;
                currentPlayer.getInventory().addItem(new ItemStack(cc.getMaterial(),4-getMaterialCount(currentPlayer, cc.getMaterial())));
            }
            currentPlayer.getInventory().addItem(new ItemStack(Material.BLAZE_ROD));
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
        ArrayList<GameColor> colorPool = new ArrayList<>(6);
        colorPool.add(GameColor.RED);
        colorPool.add(GameColor.WHITE);
        colorPool.add(GameColor.BLUE);
        colorPool.add(GameColor.GREEN);
        colorPool.add(GameColor.PINK);
        colorPool.add(GameColor.YELLOW);
        Collections.shuffle(colorPool);
        for(int i=0; i<4; i++){
            correctColors.add(i,colorPool.get(i));
        }
    }

    public void start(GamePlayer gamePlayer){
        if(!isRunning){
            if(participants.size()>0){
                isRunning = true;
                turnCount = 1;
                Collections.shuffle(participants);
                assignColors();
                setBlackBlocks();
                broadCastMessage("§eゲーム開始！");
                setNextPlayer();
                broadCastMessage("[ターン" + turnCount + "] §e" + turnPlayer.getName() +"§fのターン");
                participants.forEach(participant -> participant.getHabScoreboard().resetScoreboard());
            }
        }else{
            gamePlayer.sendActionBar("§c既にゲームが進行中です");
        }
    }

    public void finish(GamePlayer gamePlayer){
        if(isRunning){
            broadCastMessage("....");
            isRunning = false;
            Bukkit.getScheduler().runTaskLater(main,()->{
                participants.forEach(participant -> participant.getHabScoreboard().setCorrectColor());
                StringBuilder correct_is = new StringBuilder("正解は、");
                for(GameColor gameColor :correctColors){
                    correct_is.append(gameColor.getColorBlock());
                }
                broadCastMessage("ゲーム終了！§a"+turnPlayer.getName()+"§fの勝ち！");
                broadCastMessage(correct_is.toString());
                openCorrectBlock();
                broadCastMessage("かかったターン数:§e "+turnCount);
            },40L);
        }else{
            gamePlayer.sendActionBar("§c進行中のゲームがありません");
        }
    }

    public boolean checkColor(ArrayList<GameColor> colors) {
        if(colors.size()==4){
            StringBuilder message = new StringBuilder("[ターン§e"+turnCount+"§f] ");
            int hit = 0;
            int brow = 0;

            for (GameColor color : colors) {
                message.append(color.getColorBlock());
                if(correctColors.contains(color)){
                    if(correctColors.indexOf(color) == colors.indexOf(color)){
                        hit ++;
                        continue;
                    }
                    brow ++;
                }
            }
            broadCastMessage(message+" §a"+hit+"§fヒット、"+"§e"+brow+"§fブロー！");
            return hit == 4 && brow == 0;
        }
        return false;
    }

    private void openCorrectBlock(){
        Location correctBlocks = correctLocation.clone();
        for (int i=0; i<4; i++){
            correctBlocks.getBlock().setType(correctColors.get(i).getMaterial());
            correctBlocks.add(0,0,1);
        }
    }

    private void setBlackBlocks(){
        Location baseLoc = baseLocation.clone();
        for (int i=0; i<max_turn; i++) {
            for(int j=0; j<4; j++){
                if(!baseLoc.getBlock().getType().equals(Material.BLACK_WOOL)){
                    baseLoc.getBlock().setType(Material.BLACK_WOOL);
                }
                baseLoc.add(0,0,1);
            }
            baseLoc.add(2,0,-4);
        }

        Location correctLoc = correctLocation.clone();
        for(int j=0; j<4; j++){
            if(!correctLoc.getBlock().getType().equals(Material.BLACK_WOOL)){
                correctLoc.getBlock().setType(Material.BLACK_WOOL);
            }
            correctLoc.add(0,0,1);
        }
    }
}
