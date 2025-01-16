package net.tv.twitch.chrono_fish.hit_and_brow.instance;

public enum GameMode {

    NORMAL("ノーマル"),
    PRACTICE("プラクティス"),
    SPEED("スピード");

    private final String mode_name;

    GameMode(String mode_name){
        this.mode_name = mode_name;
    }

    public String get_mode_name() {return mode_name;}
}
