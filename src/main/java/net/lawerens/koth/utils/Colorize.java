package net.lawerens.koth.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class Colorize {

    public static void sendPrefixMessage(CommandSender sender, String msg){
        sendMessage(sender, "#E33914&lDROGAS "+msg);
    }

    public static void sendMessage(CommandSender sender, String s) {
        sender.sendMessage(color(s));
    }

    public static String color(String message) {
        Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = hexPattern.matcher(message);

        while (matcher.find()) {
            String hexColor = matcher.group();
            StringBuilder minecraftColor = new StringBuilder("§x");
            for (char c : hexColor.substring(1).toCharArray()) {
                minecraftColor.append("§").append(c);
            }
            message = message.replace(hexColor, minecraftColor.toString());
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
