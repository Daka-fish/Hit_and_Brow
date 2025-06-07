package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.tv.twitch.chrono_fish.hit_and_brow.Main;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener {

    private final Game game;

    public Events(Main main){
        this.game = main.getGame();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player interactPlayer = e.getPlayer();
        if(e.getClickedBlock() != null){
            Block clickedBlock = e.getClickedBlock();

            //wool breakable
            if(game.isCurrentWool(clickedBlock)){
                if(game.isRunning()){
                    if(game.getGamePlayer(interactPlayer).equals(game.getTurnPlayer())){
                        if(e.getItem() != null && e.getItem().getType().equals(Material.SHEARS) && e.getAction().isLeftClick()){
                            if(!clickedBlock.getType().equals(GameColor.BLACK.getMaterial())) clickedBlock.breakNaturally();
                            clickedBlock.setType(Material.AIR);
                            return;
                        }
                    }
                }
            }

            if(e.getAction().isLeftClick()){
                if(clickedBlock.getType().equals(Material.EMERALD_BLOCK)){
                    if(!interactPlayer.getGameMode().equals(GameMode.CREATIVE)) {
                        e.setCancelled(true);
                        game.join(interactPlayer);
                    }
                    return;
                }

                if(clickedBlock.getType().equals(Material.LAPIS_BLOCK)){
                    if(!interactPlayer.getGameMode().equals(GameMode.CREATIVE)) {
                        e.setCancelled(true);
                        game.leave(interactPlayer);
                    }
                    return;
                }
            }
        }

        //submit color left-clicking by blaze rod
        if(e.getItem() != null && e.getItem().getType().equals(Material.BLAZE_ROD) && e.getAction().isLeftClick()){
            if(game.isRunning()){
                game.getGamePlayer(interactPlayer).submitColors();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.joinMessage(Component.text(""));
        Player joinPlayer = e.getPlayer();
        joinPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 99*60*20,4,false,false));
        if(game.getGamePlayer(joinPlayer) == null) game.join(joinPlayer);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if(game.getGamePlayer(player) == null) return;
        game.leave(player);
    }
}
