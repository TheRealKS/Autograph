package com.koens.autograph.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfoCommand implements CommandExecutor {
    public boolean onCommand(CommandSender s, Command command, String label, String[] strings) {
        s.sendMessage(ChatColor.AQUA + "=About Autograph=");
        s.sendMessage(ChatColor.GREEN + "Version: 1.0.0");
        s.sendMessage(ChatColor.GREEN + "Written by: TheRealKS123");
        s.sendMessage(ChatColor.GREEN + "Built on: 8-15-2016");
        s.sendMessage(ChatColor.AQUA + "=About Autograph=");
        return true;
    }
}
