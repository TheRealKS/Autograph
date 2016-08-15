package com.koens.autograph.listeners;

import com.koens.autograph.Autograph;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    private Autograph plugin;

    public PlayerJoinListener(Autograph p) {
        this.plugin = p;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("autograph.receive")) {
            ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
            List<String> lores = new ArrayList<String>();
            lores.add(ChatColor.AQUA + "Autographs go here!");
            BookMeta meta = (BookMeta) book.getItemMeta();
            meta.setDisplayName(plugin.getBookname());
            meta.setLore(lores);
            book.setItemMeta(meta);
            event.getPlayer().getInventory().addItem(book);
        }
    }
}
