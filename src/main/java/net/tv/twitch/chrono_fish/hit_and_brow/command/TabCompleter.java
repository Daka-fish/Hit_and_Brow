package net.tv.twitch.chrono_fish.hit_and_brow.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("hab")) {
            if (strings.length == 1) {
                suggestions.add("start");
                suggestions.add("kill-game");
                suggestions.add("book");
                suggestions.add("list");
                suggestions.add("help");
                suggestions.add("setting");
                suggestions.add("mode");
                suggestions.add("color-repeat");
                suggestions.add("speed-time");
            }

            if (strings.length == 2) {
                switch (strings[0].toLowerCase()) {
                    case "mode":
                        suggestions.add("normal");
                        suggestions.add("speed");
                        break;

                    case "color-repeat":
                        suggestions.add("true");
                        suggestions.add("false");
                        break;

                    case "help":
                        suggestions.add("start");
                        suggestions.add("finish");
                        suggestions.add("book");
                        suggestions.add("list");
                        suggestions.add("setting");
                        suggestions.add("mode");
                        suggestions.add("color-repeat");
                        suggestions.add("speed-time");
                        break;

                    default:
                        break;
                }
            }
        }

        if (suggestions.isEmpty()) {
            return null;
        }
        List<String> filteredSuggestions = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(strings[strings.length - 1].toLowerCase())) {
                filteredSuggestions.add(suggestion);
            }
        }
        return filteredSuggestions;
    }
}
