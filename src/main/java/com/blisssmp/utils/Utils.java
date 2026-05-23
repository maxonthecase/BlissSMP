package com.blisssmp.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void msg(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static void msgPrefixed(CommandSender sender, String prefix, String message) {
        sender.sendMessage(color(prefix + message));
    }

    public static String formatTime(long seconds) {
        if (seconds < 60) return seconds + "s";
        if (seconds < 3600) return (seconds / 60) + "m " + (seconds % 60) + "s";
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return h + "h " + m + "m " + s + "s";
    }
}
