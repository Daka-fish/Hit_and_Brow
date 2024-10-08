package net.tv.twitch.chrono_fish.hit_and_brow.player;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.hit_and_brow.game.HabGame;
import net.tv.twitch.chrono_fish.hit_and_brow.HabColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class HabPlayer {

    private final HabGame habGame;
    private final Player player;
    private final Inventory colorInv;

    private final HabScoreboard habScoreboard;

    public HabPlayer(HabGame habGame, Player player){
        this.habGame = habGame;
        this.player = player;
        this.colorInv = Bukkit.createInventory(player,9, Component.text("カラーメニュー"));
        this.habScoreboard = new HabScoreboard(this);

        for(int i=0; i<colorInv.getSize(); i++){
            if(i % 2 == 0) colorInv.setItem(i,new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        player.setScoreboard(habScoreboard.getScoreboard());
    }

    public HabGame getHabGame() {return habGame;}
    public Player getPlayer() {return player;}
    public Inventory getColorInv() {return colorInv;}

    public HabScoreboard getHabScoreboard() {return habScoreboard;}

    public void submitColors(){
        ArrayList<HabColor> correctColors = habGame.getCorrectColors();
        ArrayList<HabColor> colors = getPlayerColor();
        int hit = 0;
        int brow = 0;

        if(colors.contains(HabColor.BLACK)){
            player.sendMessage("§c解答に不備があります");
            habGame.getMain().getLogger().info(player.getUniqueId()+" try to submit these colors, but it failed");
            for(HabColor habColor : colors){
                habGame.getMain().getLogger().info(habColor.name());
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
            habGame.finish();
            return;
        }

        habGame.setTurnCount(habGame.getTurnCount()+1);
        habGame.getHabPlayers().forEach(habPlayer -> {
            habPlayer.getHabScoreboard().setTurnCount();
        });

        player.sendMessage("§e"+hit+"§fヒット、"+"§a"+brow+"ブロー！");
    }

    public ArrayList<HabColor> getPlayerColor(){
        int index = 0;
        ArrayList<HabColor> colors = new ArrayList<>();
        Location baseLoc = habGame.getBaseLocation().clone();
        Location currentLoc = baseLoc.add(2*habGame.getTurnCount(),0,0);
        for(int i=0; i<4; i++){
            colors.add(index, HabColor.getHabColor(currentLoc.getBlock().getType()));
            index++;
            currentLoc.add(0,0,1);
        }
        return colors;
    }
}
