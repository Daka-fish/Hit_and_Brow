package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {

    private final Game game;

    public Events(Game game){this.game = game;}

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(game.getGamePlayer(e.getPlayer()) != null){
            GamePlayer gamePlayer = game.getGamePlayer(e.getPlayer());

            if(e.getItem() != null && game.isRunning()){

                //submit by blaze_rod
                if(e.getItem().getType().equals(Material.BLAZE_ROD) && e.getAction().isRightClick()){
                    if(game.getTurnPlayer().equals(gamePlayer)){
                        e.setCancelled(true);
                        gamePlayer.submitColors();
                        game.setNextPlayer();
                        game.setTurnCount(game.getTurnCount()+1);
                    }else{
                        gamePlayer.sendActionBar("§cあなたのターンではありません");
                    }
                }

                //wool breakable
                if(e.getItem().getType().equals(Material.SHEARS) && e.getAction().isLeftClick()){
                    if(game.getTurnPlayer().equals(gamePlayer)){

                    }
                }
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.joinMessage(Component.text(""));
        game.join(new GamePlayer(game, e.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        game.leave(game.getGamePlayer(e.getPlayer()));
    }
}
