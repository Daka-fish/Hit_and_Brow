package net.tv.twitch.chrono_fish.hit_and_brow;

import org.bukkit.entity.Player;

public class HabPlayer {

    private final Player player;

    public HabPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer() {return player;}
}
