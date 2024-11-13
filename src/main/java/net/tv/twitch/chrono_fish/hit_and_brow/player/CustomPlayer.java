package net.tv.twitch.chrono_fish.hit_and_brow.player;

import net.tv.twitch.chrono_fish.hit_and_brow.Manager.ConfigManager;
import net.tv.twitch.chrono_fish.hit_and_brow.Manager.ScoreBoardManager;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.HabColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CustomPlayer {

    private final Game game;
    private final Player player;

    private final ScoreBoardManager scoreBoardManager;

    public CustomPlayer(Game game, Player player){
        this.game = game;
        this.player = player;
        this.scoreBoardManager = new ScoreBoardManager(this);
        player.setScoreboard(scoreBoardManager.getScoreboard());
    }

    public Game getHabGame() {return game;}
    public Player getPlayer() {return player;}

    public ScoreBoardManager getHabScoreboard() {return scoreBoardManager;}

    public void submitColors(){
        ArrayList<HabColor> correctColors = game.getCorrectColors();
        ArrayList<HabColor> colors = getPlayerColor();
        int hit = 0;
        int brow = 0;

        if(colors.contains(HabColor.BLACK)){
            player.sendMessage("§c解答に不備があります");
            game.getMain().getLogger().info(player.getUniqueId()+" try to submit these colors, but it failed");
            for(HabColor habColor : colors){
                game.getMain().getLogger().info(habColor.name());
            }
            return;
        }

        for(HabColor habColor : colors){
            if(correctColors.contains(habColor)){
                if(correctColors.indexOf(habColor) == colors.indexOf(habColor)){
                    hit++;
                    continue;
                }
                brow++;
            }
        }

        if(brow == 4){
            game.finish();
            return;
        }

        game.setTurnCount(game.getTurnCount()+1);
        game.getHabPlayers().forEach(habPlayer -> {
            habPlayer.getHabScoreboard().setTurnCount();
        });

        player.sendMessage("§e"+hit+"§fヒット、"+"§a"+brow+"ブロー！");
    }

    public ArrayList<HabColor> getPlayerColor(){
        int index = 0;
        ArrayList<HabColor> playerColor = new ArrayList<>();
        Location baseLoc = new ConfigManager(game.getMain()).getBaseLocation().clone();
        Location currentLoc = baseLoc.add(2* game.getTurnCount(),0,0);
        for(int i=0; i<4; i++){
            playerColor.add(index, HabColor.getHabColor(currentLoc.getBlock().getType()));
            index++;
            currentLoc.add(0,0,1);
        }
        return playerColor;
    }
}
