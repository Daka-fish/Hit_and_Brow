package net.tv.twitch.chrono_fish.hit_and_brow.player;

import net.tv.twitch.chrono_fish.hit_and_brow.Manager.CustomBoard;
import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import net.tv.twitch.chrono_fish.hit_and_brow.game.CustomColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CustomPlayer {

    private final Game game;
    private final Player player;

    private final CustomBoard customBoard;

    public CustomPlayer(Game game, Player player){
        this.game = game;
        this.player = player;
        this.customBoard = new CustomBoard(this);
        player.setScoreboard(customBoard.getScoreboard());
    }

    public Game getGame() {return game;}
    public Player getPlayer() {return player;}

    public CustomBoard getHabScoreboard() {return customBoard;}

    public void submitColors(){
        ArrayList<CustomColor> correctColors = game.getCorrectColors();
        ArrayList<CustomColor> playerColor = getPlayerColor();
        int hit = 0;
        int brow = 0;

        for(CustomColor customColor : playerColor){
            if(correctColors.contains(customColor)){
                if(correctColors.indexOf(customColor) == playerColor.indexOf(customColor)){
                    hit++;
                    continue;
                }
                brow++;
            }
        }

        StringBuilder yourAnswer = new StringBuilder("[ターン"+(game.getTurnCount())+"] 提出した答え：");
        for(int i=0;i<4;i++) {
            yourAnswer.append(playerColor.get(i).getColorBlock());
        }
        player.sendMessage(yourAnswer.toString());

        if(hit == 4){
            game.finish();
            return;
        }

        game.getParticipants().forEach(habPlayer ->habPlayer.getHabScoreboard().setTurnCount());

        player.sendMessage("§e"+hit+"§fヒット、"+"§a"+brow+"§fブロー！");
    }

    public ArrayList<CustomColor> getPlayerColor(){
        int index = 0;
        ArrayList<CustomColor> playerColor = new ArrayList<>();
        Location baseLoc = game.getConfigManager().getBaseLocation().clone();
        Location currentLoc = baseLoc.add(2*(game.getTurnCount()-1),0,0);
        for(int i=0; i<4; i++){
            playerColor.add(index, CustomColor.getHabColor(currentLoc.getBlock().getType()));
            index++;
            currentLoc.add(0,0,1);
        }
        return playerColor;
    }
}
