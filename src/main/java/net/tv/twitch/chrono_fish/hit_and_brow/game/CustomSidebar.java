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

    private final String empty = "";
    private String mode =    "+モード: ";
    private String correct = "+答え: ■■■■";
    private String turn =    "+ターン数: §e0";

    public CustomSidebar(){
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("sidebar",
                Criteria.DUMMY,
                Component.text("-INFO-").decorate(TextDecoration.BOLD));

        objective.getScore(empty).setScore(0);
        objective.getScore(mode).setScore(-1);
        objective.getScore(correct).setScore(-3);
        objective.getScore(turn).setScore(-5);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Scoreboard getScoreboard() {return scoreboard;}

    public void resetModeScore(GameMode gameMode){
        scoreboard.resetScores(mode);
        mode = "+モード: " + gameMode.get_mode_name();
        objective.getScore(mode).setScore(-1);
    }

    public void setCorrectScore(ArrayList<GameColor> correct_colors){
        scoreboard.resetScores(correct);
        StringBuilder str = new StringBuilder("+答え: ");
        for (GameColor gameColor : correct_colors) {
            str.append(gameColor.getColorBlock());
        }
        correct = str.toString();
        objective.getScore(correct).setScore(-3);
    }

    public void hideCorrectScore(){
        scoreboard.resetScores(correct);
        correct = "+答え: ■■■■";
        objective.getScore(correct).setScore(-3);
    }

    public void resetTurnScore(int turn_count){
        scoreboard.resetScores(turn);
        turn = "+ターン数: " + "§e" + turn_count;
        objective.getScore(turn).setScore(-5);
    }
}
