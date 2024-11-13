package net.tv.twitch.chrono_fish.hit_and_brow.Manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.tv.twitch.chrono_fish.hit_and_brow.HabColor;
import net.tv.twitch.chrono_fish.hit_and_brow.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

public class ScoreBoardManager {

    private final CustomPlayer customPlayer;

    private final Scoreboard scoreboard;
    private final Objective obj;

    private String correctColor;
    private String turnCount;

    public ScoreBoardManager(CustomPlayer customPlayer){
        this.customPlayer = customPlayer;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.obj = scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, Component.text("-info-").decorate(TextDecoration.BOLD));
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        resetScoreboard();
        obj.getScore("").setScore(0);
        obj.getScore("+答え").setScore(-1);
        obj.getScore(correctColor).setScore(-2);
        obj.getScore(" ").setScore(-3);
        obj.getScore("+ターン数").setScore(-4);
        obj.getScore(turnCount).setScore(-5);
        obj.getScore("  ").setScore(-6);
    }

    public Scoreboard getScoreboard() {return scoreboard;}

    public void setCorrectColor(){
        scoreboard.resetScores(correctColor);
        StringBuilder str = new StringBuilder(" └  ");
        for(HabColor habColor : customPlayer.getHabGame().getCorrectColors()){
            str.append(habColor.getColorBlock());
        }
        correctColor = str.toString();
        obj.getScore(correctColor).setScore(-2);
    }

    public void setTurnCount(){
        scoreboard.resetScores(turnCount);
        turnCount = " └  "+ "§e"+ customPlayer.getHabGame().getTurnCount();
        obj.getScore(turnCount).setScore(-5);
    }

    public void resetScoreboard(){
        scoreboard.resetScores(correctColor);
        StringBuilder str = new StringBuilder(" └  ");
        for(int i=0; i<4; i++){
            str.append(HabColor.BLACK.getColorBlock());
        }
        correctColor = str.toString();
        scoreboard.resetScores(turnCount);
        turnCount = " └  "+ "§e"+ 0;

        obj.getScore(correctColor).setScore(-2);
        obj.getScore(turnCount).setScore(-5);
    }
}
