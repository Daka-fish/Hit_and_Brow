package net.tv.twitch.chrono_fish.hit_and_brow.speed;

import net.tv.twitch.chrono_fish.hit_and_brow.game.Game;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SpeedTimer extends BukkitRunnable {

    private final Game game;
    private int timer;
    private BukkitTask task;

    public SpeedTimer(Game game){
        this.game = game;
        this.timer = game.getSpeedModeSeconds();
    }

    @Override
    public void run() {
        if(timer==0){
            cancel();
            game.getTurnPlayer().submitColors();
            return;
        }
        timer --;
        game.getCustomBossBar().setProgress((float) timer/game.getSpeedModeSeconds());
    }

    public void startTimer(){
        task = this.runTaskTimer(game.getMain(),0L,20L);
    }

    public void taskCancel(){
        if(task!=null) {
            task.cancel();
            timer = 0;
        }
    }
}
