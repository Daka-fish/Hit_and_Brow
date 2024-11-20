package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.hit_and_brow.CustomColor;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import net.tv.twitch.chrono_fish.hit_and_brow.player.CustomPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    private final Game game;

    public Events(Game game){this.game = game;}

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(game.getCustomPlayer(e.getPlayer()) != null){
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
                    clickedBlock.breakNaturally();
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getView().title().equals(Component.text("カラーメニュー"))){
            ItemStack itemStack = e.getCurrentItem();
            if(itemStack != null && itemStack.getType().equals(Material.BLACK_STAINED_GLASS_PANE)){
                e.setCancelled(true);
            }
        }
    }
}
