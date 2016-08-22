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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    private Autograph plugin;
    private boolean alwaysgive;
    private boolean dirtyreg;

    public PlayerJoinListener(Autograph p, boolean always, boolean dirtyreg) {
        this.plugin = p;
        this.alwaysgive = always;
        this.dirtyreg = dirtyreg;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (event.getPlayer().hasPermission("autograph.receive")) {
            if (alwaysgive) {
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                List<String> lores = new ArrayList<String>();
                lores.add(ChatColor.AQUA + "Autographs go here!");
                BookMeta meta = (BookMeta) book.getItemMeta();
                meta.setLore(lores);
                meta.setAuthor(p.getName());
                meta.setTitle(plugin.getBookname());
                meta.addPage("");
                book.setItemMeta(meta);
                if (plugin.getBookslot() > 36) {
                    event.getPlayer().getInventory().addItem(book);
                } else {
                    event.getPlayer().getInventory().setItem(plugin.getBookslot(), book);
                }
            } else {
                if (dirtyreg) {
                    if (!plugin.getPfile().isSet(p.getUniqueId().toString())) {
                        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                        List<String> lores = new ArrayList<String>();
                        lores.add(ChatColor.AQUA + "Autographs go here!");
                        BookMeta meta = (BookMeta) book.getItemMeta();
                        meta.setLore(lores);
                        meta.setAuthor(p.getName());
                        meta.setTitle(plugin.getBookname());
                        meta.addPage("");
                        book.setItemMeta(meta);
                        if (plugin.getBookslot() > 36) {
                            event.getPlayer().getInventory().addItem(book);
                        } else {
                            event.getPlayer().getInventory().setItem(plugin.getBookslot(), book);
                        }
                        plugin.getPfile().set(p.getUniqueId().toString(), true);
                        try { plugin.getPfile().save(new File(plugin.getDataFolder(), "players.yml")); } catch (IOException e) { e.printStackTrace(); }
                    } else if (plugin.getPfile().isSet(p.getUniqueId().toString())) {
                        if (!plugin.getPfile().getBoolean(p.getUniqueId().toString())){
                            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                            List<String> lores = new ArrayList<String>();
                            lores.add(ChatColor.AQUA + "Autographs go here!");
                            BookMeta meta = (BookMeta) book.getItemMeta();
                            meta.setLore(lores);
                            meta.setAuthor(p.getName());
                            meta.setTitle(plugin.getBookname());
                            meta.addPage("");
                            book.setItemMeta(meta);
                            if (plugin.getBookslot() > 36) {
                                event.getPlayer().getInventory().addItem(book);
                            } else {
                                event.getPlayer().getInventory().setItem(plugin.getBookslot(), book);
                            }
                            plugin.getPfile().set(p.getUniqueId().toString(), true);
                            try { plugin.getPfile().save(new File(plugin.getDataFolder(), "players.yml")); } catch (IOException e) { e.printStackTrace(); }
                        }
                    }
                } else {
                    ItemStack[] invcontents = p.getInventory().getContents();
                    boolean found = false;
                    for (ItemStack is : invcontents) {
                        if (is == null)
                            continue;
                        if (!is.hasItemMeta() && !is.getType().equals(Material.WRITTEN_BOOK))
                            continue;
                        BookMeta meta = (BookMeta) is.getItemMeta();
                        if (!meta.hasTitle() && !meta.hasAuthor())
                            continue;
                        if (meta.getTitle().equals(plugin.getBookname()) && meta.getAuthor().equals(p.getName())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                        List<String> lores = new ArrayList<String>();
                        lores.add(ChatColor.AQUA + "Autographs go here!");
                        BookMeta meta = (BookMeta) book.getItemMeta();
                        meta.setLore(lores);
                        meta.setAuthor(p.getName());
                        meta.setTitle(plugin.getBookname());
                        meta.addPage("");
                        book.setItemMeta(meta);
                        if (plugin.getBookslot() > 36) {
                            event.getPlayer().getInventory().addItem(book);
                        } else {
                            event.getPlayer().getInventory().setItem(plugin.getBookslot(), book);
                        }
                    }
                }
            }
        }
    }
}
