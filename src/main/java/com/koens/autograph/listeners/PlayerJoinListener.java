package com.koens.autograph.listeners;

import com.koens.autograph.Autograph;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    private Autograph plugin;
    private boolean alwaysgive;

    public PlayerJoinListener(Autograph p, boolean always) {
        this.plugin = p;
        this.alwaysgive = always;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (event.getPlayer().hasPermission("autograph.receive")) {
            if (alwaysgive) {
                ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
                List<String> lores = new ArrayList<String>();
                lores.add(ChatColor.AQUA + "Autographs go here!");
                BookMeta meta = (BookMeta) book.getItemMeta();
                meta.setDisplayName(plugin.getBookname());
                meta.setLore(lores);
                book.setItemMeta(meta);
                event.getPlayer().getInventory().addItem(book);
            } else {
                ItemStack[] invcontents = p.getInventory().getContents();
                boolean found = false;
                for (int i = 0; i < invcontents.length; i++) {
                    if (invcontents[i] == null)
                        continue;
                    if (!invcontents[i].hasItemMeta())
                        continue;
                    if (!invcontents[i].getItemMeta().hasDisplayName())
                        continue;
                    if (invcontents[i].getItemMeta().getDisplayName().equals(plugin.getBookname())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
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
    }
}
