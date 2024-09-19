package net.tv.twitch.chrono_fish.hit_and_brow.habItem;

import net.kyori.adventure.text.Component;
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
        StringBuilder text = new StringBuilder();

        bookMeta.addPages(Component.text(text.toString()));
        book.setItemMeta(bookMeta);
        return book;
    }

    public static ItemStack HAB_STICK(){
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stick.getItemMeta();
        stickMeta.displayName(Component.text("カラーメニューを開く"));
        stick.setItemMeta(stickMeta);
        return stick;
    }

    public static ItemStack HAB_BLAZE_ROD(){
        ItemStack rod = new ItemStack(Material.BLAZE_ROD);
        ItemMeta rodMeta = rod.getItemMeta();
        rodMeta.displayName(Component.text("カラーを提出する"));
        rod.setItemMeta(rodMeta);
        return rod;
    }
}
