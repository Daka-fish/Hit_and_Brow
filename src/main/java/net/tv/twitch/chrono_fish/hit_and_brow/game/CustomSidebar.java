package net.tv.twitch.chrono_fish.hit_and_brow.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameColor;
import net.tv.twitch.chrono_fish.hit_and_brow.instance.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class CustomSidebar {

    private final Scoreboard scoreboard;
    private final Objective objective;

    private String color_repeat = "+答えの同色: ";
    private String mode =     "+モード: ";
    private String correct =  "+答え: §0■■■■";
    private String turnCount ="+ターン数: §e0";
    private String turnPlayer="  └ ";

    public CustomSidebar(){
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, Component.text("-INFO-").decorate(TextDecoration.BOLD));

        String empty = "";
        objective.getScore(empty).setScore(0);
        objective.getScore(mode).setScore(-1);
        objective.getScore(empty +" ").setScore(-2);
        objective.getScore(color_repeat).setScore(-3);
        objective.getScore(empty +"  ").setScore(-4);
        objective.getScore(correct).setScore(-5);
        objective.getScore(empty +"   ").setScore(-6);
        objective.getScore(turnCount).setScore(-7);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Scoreboard getScoreboard() {return scoreboard;}

    public void resetModeScore(GameMode gameMode){
        scoreboard.resetScores(mode);
        mode = "+モード: " + gameMode.get_mode_name();
        objective.getScore(mode).setScore(-1);
    }

    public void resetColorRepeat(boolean isColorRepeat){
        scoreboard.resetScores(color_repeat);
        color_repeat = "+答えの同色: "+((isColorRepeat) ? "§aオン" : "§fオフ");
        objective.getScore(color_repeat).setScore(-3);
    }

    public void setCorrectScore(ArrayList<GameColor> correct_colors){
        scoreboard.resetScores(correct);
        StringBuilder str = new StringBuilder("+答え: ");
        for (GameColor gameColor : correct_colors) {
            str.append(gameColor.getColorBlockStr());
        }
        correct = str.toString();
        objective.getScore(correct).setScore(-5);
    }

    public void hideCorrectScore(){
        scoreboard.resetScores(correct);
        correct = "+答え: §0■■■■";
        objective.getScore(correct).setScore(-5);
    }

    public void resetTurnCountScore(int turn_count){
        scoreboard.resetScores(turnCount);
        turnCount = "+ターン数: " + "§e" + turn_count;
        objective.getScore(turnCount).setScore(-7);
    }

    public void resetTurnPlayerScore(GamePlayer gamePlayer){
        scoreboard.resetScores(turnPlayer);
        turnPlayer = "  └ §e"+gamePlayer.getPlayerName();
        objective.getScore(turnPlayer).setScore(-8);
    }
}
