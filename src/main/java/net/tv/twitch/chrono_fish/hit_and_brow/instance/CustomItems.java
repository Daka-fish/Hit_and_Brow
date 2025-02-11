package net.tv.twitch.chrono_fish.hit_and_brow.instance;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class CustomItems {

    private ItemStack gameSettingBook;

    public void setGameSettingBook() {
        ItemStack customBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) customBook.getItemMeta();

        bookMeta.displayName(Component.text("Game setting book"));
        bookMeta.author(Component.text("Hit & Brow"));
        bookMeta.title(Component.text("Game setting"));

        Component text = Component.text("")
                .append(Component.text("\n+モード: "))
                .append(Component.text("N").decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/hab mode normal"))
                        .hoverEvent(HoverEvent.showText(Component.text("ゲームモードをノーマルに設定します"))))
                .append(Component.text(" / "))
                .append(Component.text("S").decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/hab mode speed"))
                        .hoverEvent(HoverEvent.showText(Component.text("ゲームモードをスピードに設定します"))))
                .append(Component.text("\n\n+同色あり: "))
                .append(Component.text("ON").decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/hab color-repeat true"))
                        .hoverEvent(HoverEvent.showText(Component.text("同色ありをONに設定します"))))
                .append(Component.text(" / "))
                .append(Component.text("OFF").decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/hab color-repeat false"))
                        .hoverEvent(HoverEvent.showText(Component.text("同色ありをOFFに設定します"))))
                .append(Component.text("\n\n+タイマー: "))
                .append(Component.text("+5秒").decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/hab speed-time +"))
                        .hoverEvent(HoverEvent.showText(Component.text("スピードモードの制限時間を5秒追加します"))))
                .append(Component.text(" / "))
                .append(Component.text("-5秒").decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/hab speed-time -"))
                        .hoverEvent(HoverEvent.showText(Component.text("スピードモードの制限時間を5秒減らします"))))
                .append(Component.text("\n\n+START").decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/hab start"))
                        .hoverEvent(HoverEvent.showText(Component.text("ゲームを開始します"))))
                .append(Component.text("\n\n+FINISH").decorate(TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.runCommand("/hab finish"))
                        .hoverEvent(HoverEvent.showText(Component.text("ゲームを強制終了します"))));

        bookMeta.addPages(text);
        customBook.setItemMeta(bookMeta);
        gameSettingBook = customBook;
    }


    public ItemStack GameSettingBook(){return gameSettingBook;}
}
