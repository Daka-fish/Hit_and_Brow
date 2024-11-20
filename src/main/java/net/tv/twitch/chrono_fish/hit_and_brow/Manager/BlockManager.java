package net.tv.twitch.chrono_fish.hit_and_brow.Manager;

import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import org.bukkit.Location;
import org.bukkit.Material;

public class BlockManager {
    private final Game game;
    private final ConfigManager configManager;

    public BlockManager(Game game){
        this.game=game;
        this.configManager = new ConfigManager(game.getMain());
    }

    public void setBlackBlocks(){
        Location baseLoc = configManager.getBaseLocation();
        for(int i=0; i<14; i++){
            for(int j=0; j<4; j++){
                if(!baseLoc.getBlock().getType().equals(Material.BLACK_WOOL)){
                    baseLoc.getBlock().setType(Material.BLACK_WOOL);
                }
                baseLoc.add(0,0,1);
            }
            baseLoc.add(2,0,-4);
        }

        Location correctLoc = configManager.getCorrectLocation();
        for(int j=0; j<4; j++){
            if(!correctLoc.getBlock().getType().equals(Material.BLACK_WOOL)){
                correctLoc.getBlock().setType(Material.BLACK_WOOL);
            }
            correctLoc.add(0,0,1);
        }
    }

    public void openCorrectBlock(){
        Location correctLoc = configManager.getCorrectLocation();
        for(int i=0; i<4; i++){
            correctLoc.getBlock().setType(game.getCorrectColors().get(i).getMaterial());
            correctLoc.add(0,0,1);
        }
    }
}
