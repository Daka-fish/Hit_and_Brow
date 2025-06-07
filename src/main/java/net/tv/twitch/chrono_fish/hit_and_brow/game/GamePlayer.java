package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameColor;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameMode;
import net.tv.twitch.chrono_fish.hit_and_brow.speed.CustomBossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GamePlayer {

    private final Game game;

    private final Player player;
    private final String playerName;

    public GamePlayer(Game game, Player player){
        this.game = game;
        this.player = player;
        this.playerName = player.getName();
    }

    public Player getPlayer() {return player;}

    public String getPlayerName() {return playerName;}

    public void sendMessage(String message) {if(player != null) player.sendMessage(message);}

    public void sendActionBar(String message) {if(player != null) player.sendActionBar(Component.text(message));}

    public void submitColors(){
        if(!game.getTurnPlayer().equals(this)){
            sendActionBar("§cあなたのターンではありません");
            return;
        }
        if(game.getGameMode().equals(GameMode.SPEED)) {
            game.getCustomBossBar().setProgress(0);
            game.cancelTimer();
        }
        if(player.getInventory().contains(Material.BLAZE_ROD)) player.getInventory().remove(Material.BLAZE_ROD);
        if(game.checkColor(getPlayerColor())) game.finish(player);
        if(game.isRunning()) {
            if(game.getTurnCount()==game.getMaxTurn() && !game.getGameMode().equals(GameMode.SPEED)){
                game.draw();
                return;
            }
            int current_turn_count = game.getTurnCount()+1;
            game.setTurnCount(current_turn_count);
            game.getSidebar().resetTurnCountScore(current_turn_count);
            game.setNextPlayer();
        }
    }

    public ArrayList<GameColor> getPlayerColor(){
        int index = 0;
        ArrayList<GameColor> playerColor = new ArrayList<>();
        Location currentLoc = game.getCurrentLocation().clone();
        for(int i=0; i<4; i++){
            playerColor.add(index, GameColor.getHabColor(currentLoc.getBlock().getType()));
            index++;
            currentLoc.add(0,0,1);
        }
        return playerColor;
    }

    public void setSidebar(CustomSidebar customSidebar) {if(player!=null) player.setScoreboard(customSidebar.getScoreboard());}
    public void setEmptySidebar() {if(player!=null) player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());}

    public void showCustomBossBar(CustomBossBar customBossBar) {if(player!=null && customBossBar!=null) player.showBossBar(customBossBar.getBossBar());}
    public void hideCustomBossBar(CustomBossBar customBossBar) {if(player!=null && customBossBar!=null) player.hideBossBar(customBossBar.getBossBar());}
}
