package com.koens.autograph.listeners;

import com.koens.autograph.Autograph;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class PlayerInventoryClickListener implements Listener {

    private Autograph plugin;

    public PlayerInventoryClickListener(Autograph p) {
        this.plugin = p;
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();
            ItemStack mat = event.getCurrentItem();
            if (mat != null) {
                if (mat.getType().equals(Material.WRITTEN_BOOK)) {
                    BookMeta meta = (BookMeta) mat.getItemMeta();
                    if (meta.hasAuthor() && meta.hasTitle()) {
                        if (meta.getAuthor().equals(p.getName()) && meta.getTitle().equals(plugin.getBookname())) {
                            event.setResult(Event.Result.DENY);
                        }
                    }
                }
            }
        }
    }
}
