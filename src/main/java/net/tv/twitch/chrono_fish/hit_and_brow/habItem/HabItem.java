package net.tv.twitch.chrono_fish.hit_and_brow.habItem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class HabItem {

    public static ItemStack HAB_BOOK(){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.displayName(Component.text("Hit_and_Brow"));
        bookMeta.setTitle("Hit_and_brow");
        bookMeta.setAuthor("UNKNOWN");

        Component text = Component.text("");
        text = text.append(Component.text("\n\n◆参加する"))
                .hoverEvent(HoverEvent.showText(Component.text("参加するにはここをクリック")))
                .clickEvent(ClickEvent.runCommand("/hab join"));
        text = text.append(Component.text("\n\n◆観戦者になる")
                .hoverEvent(HoverEvent.showText(Component.text("観戦するにはここをクリック")))
                .clickEvent(ClickEvent.runCommand("/hab leave")));
        text = text.append(Component.text("\n\n◆参加者を確認する")
                .hoverEvent(HoverEvent.showText(Component.text("参加者を見るにはここをクリック")))
                .clickEvent(ClickEvent.runCommand("/hab list")));
        text = text.append(Component.text("\n\n◆ゲームを開始する")
                .hoverEvent(HoverEvent.showText(Component.text("ゲームを開始するにはここをクリック")))
                .clickEvent(ClickEvent.runCommand("/hab start")));
        text = text.append(Component.text("\n\n◆ゲームを終了する")
                .hoverEvent(HoverEvent.showText(Component.text("ゲームを終了するにはここをクリック")))
                .clickEvent(ClickEvent.runCommand("/hab finish")));

        bookMeta.addPages(text);
        book.setItemMeta(bookMeta);
        return book;
    }

    public static ItemStack HAB_BLAZE_ROD(){
        ItemStack rod = new ItemStack(Material.BLAZE_ROD);
        ItemMeta rodMeta = rod.getItemMeta();
        rodMeta.displayName(Component.text("カラーを提出する"));
        rod.setItemMeta(rodMeta);
        return rod;
    }
}
