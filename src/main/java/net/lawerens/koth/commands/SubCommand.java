package net.lawerens.koth.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

import static net.lawerens.koth.utils.Colorize.color;

public interface SubCommand {

    String getName();
    String getDescription();
    String getSyntax();
    void execute(CommandSender sender, String[] args);
    List<String> onTabComplete(CommandSender sender, String[] args);

    default void sendMessage(CommandSender sender, String msg){
        sender.sendMessage(color(msg));
    }

    default void sendPrefixMessage(CommandSender sender, String msg){
        sender.sendMessage(color("#E24344&lKOTH &7"+msg));
    }
}
