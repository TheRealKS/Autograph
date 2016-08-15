package com.koens.autograph.listeners;

import com.koens.autograph.Autograph;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class PlayerDropItemListener implements Listener {

    private Autograph plugin;

    public PlayerDropItemListener(Autograph p) {
        this.plugin = p;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack drop = event.getItemDrop().getItemStack();
        if (drop.getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta meta = (BookMeta) drop.getItemMeta();
            if (meta.hasAuthor() && meta.hasTitle()) {
                if (meta.getTitle().equals(plugin.getBookname()) && meta.getAuthor().equals(p.getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
