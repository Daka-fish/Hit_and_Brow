package net.tv.twitch.chrono_fish.hit_and_brow;

import java.util.ArrayList;

public class HabGame {

    private final ArrayList<HabPlayer> players;

    public HabGame(){
        this.players = new ArrayList<>();
    }

    public ArrayList<HabPlayer> getPlayers() {return players;}
}
