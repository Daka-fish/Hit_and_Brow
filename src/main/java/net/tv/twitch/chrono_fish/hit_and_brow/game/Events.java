package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.tv.twitch.chrono_fish.hit_and_brow.CustomColor;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import net.tv.twitch.chrono_fish.hit_and_brow.player.CustomPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Events implements Listener {

    private final Game game;

    public Events(Game game){this.game = game;}

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(game.getCustomPlayer(e.getPlayer()) != null && e.getAction().isRightClick()){
            CustomPlayer customPlayer = game.getCustomPlayer(e.getPlayer());

            if(e.getItem() != null && e.getItem().equals(HabItem.HAB_BLAZE_ROD())){
                if(game.isRunning()){
                    if(game.getTurnPlayer().equals(customPlayer)){
                        customPlayer.submitColors();
                        game.setNextPlayer();
                    }else{
                        e.getPlayer().sendMessage("§cあなたのターンではありません");
                    }
                }
                return;
            }

            Block clickedBlock = e.getClickedBlock();
            if(clickedBlock != null && CustomColor.getMaterials().contains(clickedBlock.getType())){
                if(game.isRunning()){
                    if(!customPlayer.equals(game.getTurnPlayer())){
                        customPlayer.getPlayer().sendMessage("§cあなたのターンではありません");
                        return;
                    }
                    if(clickedBlock.getLocation().getX() != (game.getConfigManager().getBaseLocation().getX()+(game.getTurnCount()-1)*2)) {
                        e.setCancelled(true);
                        customPlayer.getPlayer().sendMessage("§c解答場所が違います");
                        return;
                    }
                    if(clickedBlock.getType().equals(Material.BLACK_WOOL)){
                        clickedBlock.setType(Material.AIR);
                        return;
                    }
                    clickedBlock.breakNaturally();
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
}
