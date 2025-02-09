package net.tv.twitch.chrono_fish.hit_and_brow.speed;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

public class CustomBossBar {

    private final BossBar bossBar;

    public CustomBossBar(){
        this.bossBar = BossBar.bossBar(
                Component.text("TIMER").decorate(TextDecoration.BOLD),
                1,
                BossBar.Color.BLUE,
                BossBar.Overlay.NOTCHED_20);
    }

    public BossBar getBossBar(){return bossBar;}

    public void setProgress(float progress){bossBar.progress(progress);}
}
