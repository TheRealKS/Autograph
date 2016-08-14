package com.koens.autograph.commands;

import com.koens.autograph.Autograph;
import com.koens.autograph.actionbar.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class AutographCommand implements CommandExecutor {

    private Autograph plugin;
    private int timerID;
    private int t;
    private static final String USAGE = ChatColor.RED + "Usage: %COMMAND% accept|deny|sign[msg]|request[IGN]";
    private static final String PREFIX = ChatColor.AQUA + "[" + ChatColor.WHITE + "AUTOGRAPH" + ChatColor.AQUA + "] ";

    public AutographCommand(Autograph p) {
        this.plugin = p;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("sign")){

                } else if (args[0].equalsIgnoreCase("request")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        final Player pl = player;
                        final Player target = p;
                        if (player.getName().equals(args[1])) {
                            p.sendMessage(PREFIX + plugin.getRequestsenttxt().replace("%PLAYER%", pl.getName()));
                            pl.sendMessage(PREFIX + plugin.getRequesttxt().replace("%PLAYER%", p.getName()));
                            t = plugin.getRequesttime();
                            final String requestname = player.getName();
                            plugin.putIntoRequestsMap(requestname, t);
                            timerID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                                public void run() {
                                    plugin.getLogger().info(Integer.toString(t));
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
                        } else {

                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "That subcommand doesn't exist!");
                    p.sendMessage(USAGE.replace("%COMMAND%", command.getName()));
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("accept")) {
                    if (plugin.requests.containsKey(p.getName())) {
                        plugin.getServer().getScheduler().cancelTask(p.getMetadata("runningRequestTimerID").get(0).asInt());
                        plugin.removeFromRequestsMap(p.getName());
                        String requestsender = p.getMetadata("runningRequestSender").get(0).asString();
                        Player sender = null;
                        for (Player player : plugin.getServer().getOnlinePlayers()) {
                            if (player.getName().equals(requestsender)) {
                                sender = player;
                                break;
                            }
                        } if (sender != null) {
                            sender.sendMessage(PREFIX + plugin.getAccepttxt());
                        } else {
                            p.sendMessage(PREFIX + "Something went wrong while accepting the request!");
                        }
                    } else {
                        p.sendMessage(PREFIX + plugin.getNorequeststxt());
                    }
                } else if (args[0].equalsIgnoreCase("deny")) {

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
}
