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
        ArrayList<HabColor> playerColor = getPlayerColor();
        int hit = 0;
        int brow = 0;

        for(HabColor habColor : playerColor){
            if(correctColors.contains(habColor)){
                if(correctColors.indexOf(habColor) == playerColor.indexOf(habColor)){
                    hit++;
                    continue;
                }
                brow++;
            }
        }

        String yourAnswer = "提出した答え：";
        for(int i=0;i<4;i++) {
            yourAnswer += playerColor.get(i).getColorBlock();
        }
        player.sendMessage(yourAnswer);

        if(hit == 4){
            game.finish();
            return;
        }

        game.setTurnCount(game.getTurnCount()+1);
        game.getHabPlayers().forEach(habPlayer -> {
            habPlayer.getHabScoreboard().setTurnCount();
        });

        player.sendMessage("§e"+hit+"§fヒット、"+"§a"+brow+"§fブロー！");
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
