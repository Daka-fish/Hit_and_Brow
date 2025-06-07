package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameColor;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameMode;
import net.tv.twitch.chrono_fish.hit_and_brow.speed.CustomBossBar;
import net.tv.twitch.chrono_fish.hit_and_brow.speed.SpeedTimer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    private int maxPlayerSize;
    private int maxTurn;
    private boolean isColorRepeat;

    private final CustomSidebar sidebar;
    private final CustomBossBar customBossBar;

    private SpeedTimer speedTimer;
    private int speedModeSeconds;

    public Game(Main main){
        this.main = main;
        this.participants = new ArrayList<>(2);
        this.correctColors = new ArrayList<>(4);
        this.isRunning = false;
        this.turnCount = 0;
        this.sidebar = new CustomSidebar();
        this.customBossBar = new CustomBossBar();
    }

    public Main getMain() {return main;}

    public ArrayList<GamePlayer> getParticipants() {return participants;}

    public boolean isRunning() {return isRunning;}

    public int getTurnCount() {return turnCount;}
    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public GamePlayer getTurnPlayer() {return turnPlayer;}
    public void setTurnPlayer(GamePlayer gamePlayer) {
        this.turnPlayer = gamePlayer;
        turnPlayer.sendActionBar("§aあなたのターンです");
        sidebar.resetTurnPlayerScore(turnPlayer);
    }

    public GameMode getGameMode() {return gameMode;}
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        sidebar.resetModeScore(gameMode);
    }

    public Location getCurrentLocation() {return baseLocation.clone().add(2*((turnCount-1)%maxTurn),0,0);}

    public int getMaxPlayerSize() {return maxPlayerSize;}

    public int getMaxTurn() {return maxTurn;}

    public boolean isColorRepeat() {return isColorRepeat;}
    public void setColorRepeat(boolean colorRepeat) {
        this.isColorRepeat = colorRepeat;
        sidebar.resetColorRepeat(colorRepeat);
    }

    public CustomSidebar getSidebar() {return sidebar;}

    public CustomBossBar getCustomBossBar() {return customBossBar;}

    public void startTimer(){
        this.speedTimer = new SpeedTimer(this);
        speedTimer.startTimer();
    }

    public void cancelTimer(){
        if(this.speedTimer != null){
            speedTimer.taskCancel();
        }
    }

    public int getSpeedModeSeconds() {return speedModeSeconds;}
    public void setSpeedModeSeconds(int seconds){this.speedModeSeconds = seconds;}

    public GamePlayer getGamePlayer(Player player){
        for(GamePlayer gamePlayer : participants){
            if(gamePlayer.getPlayer().equals(player)) return gamePlayer;
        }
        return null;
    }

    public void join(Player player) {
        if(isRunning){
            player.sendActionBar(Component.text("§cゲーム進行中は参加できません"));
            return;
        }
        if (getGamePlayer(player) == null){
            GamePlayer joinedPlayer = new GamePlayer(this,player);
            participants.add(joinedPlayer);
            broadCastMessage("§e"+joinedPlayer.getPlayerName()+"§fがゲームに参加しました");
            joinedPlayer.setSidebar(sidebar);
        }else{
            player.sendActionBar(Component.text("§c既に参加しています"));
        }
    }

    public void leave(Player player) {
        if (getGamePlayer(player) != null){
            GamePlayer gamePlayer = getGamePlayer(player);
            gamePlayer.setEmptySidebar();
            broadCastMessage("§e"+gamePlayer.getPlayerName()+"§fがゲームから退室しました");
            participants.remove(gamePlayer);
        }else{
            player.sendActionBar(Component.text("§c参加していません"));
        }
    }

    public void broadCastMessage(String message){participants.forEach(gamePlayer -> gamePlayer.sendMessage(((!isRunning) ? "[H&B] ":"")+message));}

    public void setNextPlayer(){
        if(isRunning){
            if(getTurnCount()==1){
                setTurnPlayer(participants.get(0));
            }else{
                int index = (participants.indexOf(getTurnPlayer()) + 1) % participants.size();
                setTurnPlayer(participants.get(index));
            }
            addTurnPlayerItems(turnPlayer);
        }
    }

    private void addTurnPlayerItems(GamePlayer gamePlayer){
        Player currentPlayer = gamePlayer.getPlayer();
        for(GameColor cc: GameColor.values()){
            if(cc.equals(GameColor.BLACK)) continue;
            currentPlayer.getInventory().addItem(new ItemStack(cc.getMaterial(),4-getMaterialCount(currentPlayer, cc.getMaterial())));
        }
        currentPlayer.getInventory().addItem(new ItemStack(Material.BLAZE_ROD,1-getMaterialCount(currentPlayer,Material.BLAZE_ROD)));
        currentPlayer.getInventory().addItem(new ItemStack(Material.SHEARS,1-getMaterialCount(currentPlayer,Material.SHEARS)));
        if(gameMode.equals(GameMode.SPEED)){
            startTimer();
        }
    }

    private int getMaterialCount(Player player, Material material){
        int count = 0;
        for(ItemStack item : player.getInventory().getContents()){
            if(item != null && item.getType() == material){
                count +=item.getAmount();
            }
        }
        return count;
    }

    private void assignColors() {
        correctColors.clear();
        ArrayList<GameColor> colorPool = new ArrayList<>(6);
        colorPool.add(GameColor.RED);
        colorPool.add(GameColor.WHITE);
        colorPool.add(GameColor.BLUE);
        colorPool.add(GameColor.GREEN);
        colorPool.add(GameColor.PINK);
        colorPool.add(GameColor.YELLOW);
        Collections.shuffle(colorPool);

        if(isColorRepeat) {
            GameColor repeatColor = colorPool.get(0);
            correctColors.add(repeatColor);
            correctColors.add(repeatColor);

            correctColors.add(colorPool.get(1));
            correctColors.add(colorPool.get(2));
        }else {
            for(int i = 0; i < 4; i++) {
                correctColors.add(colorPool.get(i));
            }
        }

        Collections.shuffle(correctColors);
    }

    public void start(Player player){
        if(!isRunning){
            if(participants.size()>0){
                isRunning = true;
                Collections.shuffle(participants);
                turnCount = 1;
                sidebar.resetTurnCountScore(1);
                assignColors();
                setBlackBlocks();
                setNextPlayer();
                sidebar.hideCorrectScore();

                for(GamePlayer participant : participants) {
                    if(gameMode.equals(GameMode.SPEED)){
                        addPotionEffect(participant, new PotionEffect(PotionEffectType.SPEED,99*60*20,0,false,true));
                        participant.showCustomBossBar(customBossBar);
                    }
                }
            }
        }else{
            player.sendActionBar(Component.text("§c既にゲームが進行中です"));
        }
    }

    public void finish(Player player){
        if(isRunning){
            broadCastMessage("....");
            isRunning = false;
            Bukkit.getScheduler().runTaskLater(main,()->{
                cancelTimer();
                StringBuilder message = new StringBuilder("正解は、");
                for(GameColor gameColor : correctColors){
                    message.append(gameColor.getColorBlockStr());
                }
                sidebar.setCorrectScore(correctColors);
                broadCastMessage("ゲーム終了！§a"+turnPlayer.getPlayerName()+"§fの勝ち！");
                broadCastMessage(message.toString());
                openCorrectBlock();
                broadCastMessage("かかったターン数:§e "+turnCount);
                for (GamePlayer participant : participants) {
                    participant.getPlayer().getInventory().clear();
                    removePotionEffect(participant);
                    participant.hideCustomBossBar(customBossBar);
                    if(participant.getPlayer().isOp()) participant.getPlayer().getInventory().addItem(main.getCustomItems().GameSettingBook());
                }
            },40L);
        }else{
            player.sendActionBar(Component.text("§c進行中のゲームがありません"));
        }
    }

    public void draw(){
        if(isRunning){
            broadCastMessage("....");
            isRunning = false;
            Bukkit.getScheduler().runTaskLater(main,()->{
                StringBuilder message = new StringBuilder();
                for(GameColor gameColor : correctColors){
                    message.append(gameColor.getColorBlockStr());
                }
                broadCastMessage("引き分け！正解は "+message);
                openCorrectBlock();
                for (GamePlayer participant : participants) {
                    participant.getPlayer().getInventory().clear();
                    if(participant.getPlayer().isOp()) participant.getPlayer().getInventory().addItem(main.getCustomItems().GameSettingBook());
                    removePotionEffect(participant);
                    participant.hideCustomBossBar(customBossBar);
                }
            },40L);
        }
    }

    public void killGame(Player player){
        player.sendMessage("§Cゲームを強制終了します");
        if(isRunning){
            isRunning = false;
            sidebar.setCorrectScore(correctColors);
            cancelTimer();
            openCorrectBlock();
            broadCastMessage("§cゲームを終了しました");
            for (GamePlayer participant : participants) {
                participant.getPlayer().getInventory().clear();
                if(participant.getPlayer().isOp()) participant.getPlayer().getInventory().addItem(main.getCustomItems().GameSettingBook());
                removePotionEffect(participant);
                participant.hideCustomBossBar(customBossBar);
            }
        }
    }

    public boolean checkColor(ArrayList<GameColor> submittedColors) {
        //同色ありの場合で動作するようにしたら、複雑になった
        if (submittedColors.size() != 4) return false;

        int hit = 0;
        int brow = 0;
        ArrayList<GameColor> remainingCorrect = new ArrayList<>(correctColors);
        ArrayList<GameColor> remainingSubmitted = new ArrayList<>();

        // まずHITを判定し、該当する色を削除
        for (int i = 0; i < 4; i++) {
            if (submittedColors.get(i).equals(correctColors.get(i))) {
                hit++;
                remainingCorrect.set(i, null); // ヒットした色は削除 (null でマーク)
            } else {
                remainingSubmitted.add(submittedColors.get(i));
            }
        }

        // BROWを判定
        for (GameColor color : remainingSubmitted) {
            if (remainingCorrect.contains(color)) {
                brow++;
                remainingCorrect.remove(color); // 重複を防ぐために削除
            }
        }
        if(hit == 4 && brow == 0) return true;
        if(turnCount < maxTurn || gameMode.equals(GameMode.SPEED)) broadcastSubmitResult(submittedColors, hit, brow);
        return false;
    }

    private void broadcastSubmitResult(ArrayList<GameColor> colors, int hit, int brow){
        StringBuilder colors_str = new StringBuilder();
        String submit_result = "§a"+hit+"§fヒット §e"+brow+"§fブロー";
        for (GameColor color : colors) {
            colors_str.append(color.getColorBlockStr());
        }
        broadCastMessage("[ターン"+turnCount+"] "+colors_str+" "+submit_result);
    }

    private void openCorrectBlock(){
        Location correctBlocks = correctLocation.clone();
        for (int i=0; i<4; i++){
            correctBlocks.getBlock().setType(correctColors.get(i).getMaterial());
            correctBlocks.add(0,0,1);
        }
    }

    public void setBlackBlocks(){
        Location baseLoc = baseLocation.clone();
        for (int i = 0; i< maxTurn; i++) {
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

    public boolean isCurrentWool(Block block) {
        for (GameColor gameColor : GameColor.values()) {
            if(gameColor.getMaterial().equals(block.getType())){
                if(getCurrentLocation().getX() == block.getX()) return true;
            }
        }
        return false;
    }

    private void addPotionEffect(GamePlayer gamePlayer, PotionEffect potionEffect){if(gamePlayer.getPlayer()!=null) gamePlayer.getPlayer().addPotionEffect(potionEffect);}
    private void removePotionEffect(GamePlayer gamePlayer){if(gamePlayer.getPlayer()!=null) gamePlayer.getPlayer().removePotionEffect(PotionEffectType.SPEED);}

    public void loadOptions(ConfigManager configManager){
        gameMode = configManager.getGameMode();
        baseLocation = configManager.getBaseLocation();
        correctLocation = configManager.getCorrectLocation();
        maxTurn = configManager.getMaxTurn();
        maxPlayerSize = configManager.getMaxPlayerSize();
        isColorRepeat = configManager.getIsColorRepeat();
        speedModeSeconds = configManager.getSpeedModeTimerSeconds();
    }

    public void saveOptions(ConfigManager configManager){
        configManager.setGameMode(gameMode);
        configManager.setSpeedModeTimerSeconds(speedModeSeconds);
        configManager.setIsColorRepeat(isColorRepeat);
        //最後の参加者を記録する
    }
}
