package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.hit_and_brow.HabColor;
import net.tv.twitch.chrono_fish.hit_and_brow.habItem.HabItem;
import net.tv.twitch.chrono_fish.hit_and_brow.player.HabPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HabListener implements Listener {

    private final HabGame habGame;

    public HabListener(HabGame habGame){this.habGame = habGame;}

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(habGame.getHabPlayer(e.getPlayer()) != null){
            HabPlayer habPlayer = habGame.getHabPlayer(e.getPlayer());

            if(e.getItem() != null && e.getItem().equals(HabItem.HAB_BLAZE_ROD())){
                if(habGame.isRunning()){
                    if(habGame.getTurnPlayer().equals(habPlayer)){
                        habPlayer.submitColors();
                        habGame.setNextPlayer();
                    }else{
                        e.getPlayer().sendMessage("§cあなたのターンではありません");
                    }
                }
                return;
            }

            Block clickedBlock = e.getClickedBlock();
            if(clickedBlock != null && HabColor.getMaterials().contains(clickedBlock.getType())){
                if(habGame.isRunning()){
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
