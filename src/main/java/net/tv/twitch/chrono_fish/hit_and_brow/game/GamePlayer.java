package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GamePlayer {

    private final Game game;

    private final Player player;
    private final String name;

    public GamePlayer(Game game, Player player){
        this.game = game;
        this.player = player;
        this.name = player.getName();
    }

    public Player getPlayer() {return player;}

    public String getName() {return name;}

    public void sendMessage(String message) {if(player != null) player.sendMessage(message);}

    public void sendActionBar(String message) {if(player != null) player.sendActionBar(Component.text(message));}

    public void submitColors(){
        if(player.getInventory().contains(Material.BLAZE_ROD)) player.getInventory().remove(Material.BLAZE_ROD);
        if(game.checkColor(getPlayerColor())) game.finish(this);
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

    public void setScoreBoard(CustomSidebar customSidebar) {if(player!=null) player.setScoreboard(customSidebar.getScoreboard());}
}
