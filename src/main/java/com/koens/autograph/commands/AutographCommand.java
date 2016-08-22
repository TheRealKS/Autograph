package com.koens.autograph.commands;

import com.koens.autograph.Autograph;
import com.koens.autograph.actionbar.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("Since15")
public class AutographCommand implements CommandExecutor {

    private Autograph plugin;
    private int timerID;
    private int t;
    private static final String USAGE = ChatColor.RED + "Usage: %COMMAND% accept|deny|sign[msg]|request[IGN]";
    private final String PREFIX;

    public AutographCommand(Autograph p, Boolean pre) {
        this.plugin = p;
        if (pre)
            PREFIX = ChatColor.AQUA + "[" + ChatColor.WHITE + "AUTOGRAPH" + ChatColor.AQUA + "] ";
        else
            PREFIX = "";
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("sign")) {
                    if (!p.hasPermission("autograph.sign")) {
                        p.sendMessage(ChatColor.RED + "You don't have access to that command!");
                        return true;
                    }
                    if (p.hasMetadata("acceptedSignRequest") && p.getMetadata("acceptedSignRequest").get(0).asBoolean() && p.hasMetadata("acceptedSignRequestFor")) {
                        if (args[1].length() > 256) {
                            p.sendMessage(PREFIX + "The autograph you provided is invalid: Can be max 256 characters.");
                            return true;
                        }
                        ItemStack gotbook = plugin.getFromBooksMap(p.getMetadata("acceptedSignRequestFor").get(0).asString());
                        BookMeta meta = (BookMeta) gotbook.getItemMeta();
                        if (meta.getPageCount() == 50) {
                            p.sendMessage(PREFIX + "This person doesn't have enough space in their autograph book!");
                            return true;
                        }
                        args = Arrays.copyOfRange(args, 1, args.length);
                        StringBuilder sb = new StringBuilder();
                        for (String s1 : args) {
                            sb.append(s1 + " ");
                        }
                        if (meta.getPageCount() == 1 && meta.getPage(1).equals(""))
                            meta.setPage(1, ChatColor.translateAlternateColorCodes('&', sb.toString()));
                        else
                            meta.addPage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                        gotbook.setItemMeta(meta);
                        Player pla = getPlayer(p.getMetadata("acceptedSignRequestFor").get(0).asString());
                        if (pla != null) {
                            pla.getInventory().setItem(plugin.getBookslot(), gotbook);
                            pla.sendMessage(PREFIX + plugin.getRequestfilledtxt().replace("%PLAYER%", p.getName()));
                        } else {
                            p.sendMessage(PREFIX + "OOPS! Something went wrong!");
                        }
                        plugin.removeFromRequestsMap(p.getMetadata("acceptedSignRequestFor").get(0).asString());
                        plugin.removeFromBooksMap(p.getMetadata("acceptedSignRequestFor").get(0).asString());
                        p.removeMetadata("acceptedSignRequest", plugin);
                        p.removeMetadata("acceptedSignRequestFor", plugin);
                        p.sendMessage(PREFIX + plugin.getSignconfirmtxt().replace("%PLAYER%", pla.getName()));
                    } else {
                        p.sendMessage(PREFIX + plugin.getNothingtosigntxt());
                    }
                } else if (args[0].equalsIgnoreCase("request")) {
                    if (args.length > 2) {
                        p.sendMessage(ChatColor.RED + "You have entered an invalid amount of arguments!");
                        p.sendMessage(USAGE.replace("%COMMAND%", command.getName()));
                        return true;
                    }
                    if (!p.hasPermission("autograph.request")) {
                        p.sendMessage(ChatColor.RED + "You don't have access to that command!");
                        return true;
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        final Player target = p;
                        if (player.getName().equals(args[1])) {
                            if (plugin.doesRequestMapContain(player.getName())) {
                                p.sendMessage(PREFIX + plugin.getRequestalreadyrunningtxt());
                                return true;
                            }
                            final Player pl = player;
                            p.sendMessage(PREFIX + plugin.getRequestsenttxt().replace("%PLAYER%", pl.getName()));
                            pl.sendMessage(PREFIX + plugin.getRequesttxt().replace("%PLAYER%", p.getName()));
                            t = plugin.getRequesttime();
                            final String requestname = player.getName();
                            plugin.putIntoRequestsMap(requestname, t);
                            timerID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                                public void run() {
                                    plugin.putIntoRequestsMap(requestname, t);
                                    String msg = plugin.getCountdowntxt();
                                    msg = msg.replace("%PLAYER%", pl.getName());
                                    msg = msg.replace("%TIME%", Integer.toString(t));
                                    if (plugin.getFromRequestsMap(requestname) > 0) {
                                        ActionBarAPI.sendActionBar(target, msg);
                                        t--;
                                    } else {
                                        plugin.removeFromRequestsMap(requestname);
                                        String mssg = plugin.getRequestexpiredtxt();
                                        mssg = mssg.replace("%PLAYER%", pl.getName());
                                        ActionBarAPI.sendActionBar(target, mssg);
                                        plugin.getServer().getScheduler().cancelTask(timerID);
                                    }
                                }
                            }, 20L, 20L);
                            player.setMetadata("runningRequestTimerID", new FixedMetadataValue(plugin, timerID));
                            player.setMetadata("runningRequestSender", new FixedMetadataValue(plugin, p.getName()));
                            break;
                        } else {
                            p.sendMessage(PREFIX + "That player doesn't exist or is not online!");
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "That subcommand doesn't exist!");
                    p.sendMessage(USAGE.replace("%COMMAND%", command.getName()));
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("accept")) {
                    if (!p.hasPermission("autograph.accept")) {
                        p.sendMessage(ChatColor.RED + "You don't have access to that command!");
                        return true;
                    }
                    if (plugin.doesRequestMapContain(p.getName())) {
                        plugin.getServer().getScheduler().cancelTask(p.getMetadata("runningRequestTimerID").get(0).asInt());
                        plugin.removeFromRequestsMap(p.getName());
                        String requestsender = p.getMetadata("runningRequestSender").get(0).asString();
                        Player sender = null;
                        for (Player player : plugin.getServer().getOnlinePlayers()) {
                            if (player.getName().equals(requestsender)) {
                                sender = player;
                                break;
                            }
                        }
                        if (sender != null) {
                            sender.sendMessage(PREFIX + plugin.getAccepttxt().replace("%PLAYER%", p.getName()));
                            sender.setMetadata("acceptedSignRequest", new FixedMetadataValue(plugin, true));
                            sender.setMetadata("acceptedSignRequestFor", new FixedMetadataValue(plugin, p.getName()));
                            ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
                            book.getItemMeta().setDisplayName(plugin.getBookname());
                            List<String> lores = new ArrayList<String>();
                            lores.add(ChatColor.AQUA + "Autographs go here!");
                            book.getItemMeta().setLore(lores);
                            ItemStack mirror = null;
                            ItemStack[] invcontents = p.getInventory().getContents();
                            boolean found = false;
                            for (int i = 0; i < invcontents.length; i++) {
                                if (invcontents[i] == null)
                                    continue;
                                if (!invcontents[i].hasItemMeta() && !invcontents[i].getType().equals(Material.WRITTEN_BOOK))
                                    continue;
                                BookMeta meta = (BookMeta) invcontents[i].getItemMeta();
                                if (!meta.hasTitle() && !meta.hasAuthor())
                                    continue;
                                if (meta.getTitle().equals(plugin.getBookname()) && meta.getAuthor().equals(p.getName())) {
                                    mirror = p.getInventory().getItem(i);
                                    p.getInventory().remove(Material.WRITTEN_BOOK);
                                    p.updateInventory();
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                p.sendMessage(PREFIX + "It seems like you have lost your autograph book! Relog to get a new one!");
                            } else {
                                plugin.putIntoBooksMap(p.getName(), mirror);
                            }

                        } else {
                            p.sendMessage(PREFIX + "Something went wrong while accepting the request!");
                        }
                    } else {
                        p.sendMessage(PREFIX + plugin.getNorequeststxt());
                    }
                } else if (args[0].equalsIgnoreCase("deny")) {
                    if (!p.hasPermission("autograph.deny")) {
                        p.sendMessage(ChatColor.RED + "You don't have access to that command!");
                        return true;
                    }
                    if (plugin.doesRequestMapContain(p.getName())) {
                        plugin.getServer().getScheduler().cancelTask(p.getMetadata("runningRequestTimerID").get(0).asInt());
                        plugin.removeFromRequestsMap(p.getName());
                        String requestsender = p.getMetadata("runningRequestSender").get(0).asString();
                        Player sender = null;
                        for (Player player : plugin.getServer().getOnlinePlayers()) {
                            if (player.getName().equals(requestsender)) {
                                sender = player;
                                break;
                            }
                        }
                        if (sender != null) {
                            ActionBarAPI.sendActionBar(sender, plugin.getDenytxt().replace("%PLAYER%", p.getName()));
                            sender.sendMessage(PREFIX + plugin.getDenytxt().replace("%PLAYER%", p.getName()));
                        } else {
                            p.sendMessage(PREFIX + "Something went wrong while denying the request!");
                        }
                    } else {
                        p.sendMessage(PREFIX + plugin.getNorequeststxt());
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "That subcommand doesn't exist!");
                    p.sendMessage(USAGE.replace("%COMMAND%", command.getName()));
                }
            } else {
                p.sendMessage(ChatColor.RED + "You have entered an invalid amount of arguments!");
                p.sendMessage(USAGE.replace("%COMMAND%", command.getName()));
            }
        } else {
            commandSender.sendMessage("This command can only be run by players!");
        }
        return true;
    }

    private Player getPlayer(String name) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
}
