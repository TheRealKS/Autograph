package com.koens.autograph.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("autograph.receive")) {
            ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
            book.getItemMeta().setDisplayName("Autograph Book");
            List<String> lores = new ArrayList<String>();
            lores.add(ChatColor.AQUA + "Autographs go here!");
            book.getItemMeta().setLore(lores);
            BookMeta meta = (BookMeta) book.getItemMeta();
            List<String> pages = new ArrayList<String>();
            pages.add("Hoi Hoi Hoi");
            meta.setPages(pages);
            book.setItemMeta(meta);
            event.getPlayer().getInventory().addItem(book);
        }
    }
}
