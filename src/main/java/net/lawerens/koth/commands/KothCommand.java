package net.lawerens.koth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.lawerens.koth.utils.CenterMessage.sendCenteredMessage;
import static net.lawerens.koth.utils.CenterMessage.sendUnderline;
import static net.lawerens.koth.utils.Colorize.color;

public class KothCommand implements CommandExecutor, TabCompleter {

    private final List<SubCommand> subCommands = new ArrayList<>();

    public KothCommand(){
        subCommands.add(new CreateKoth());
        subCommands.add(new RemoveKoth());
        subCommands.add(new SetPosKoth());
        subCommands.add(new SetTakeTimeKoth());
        subCommands.add(new RewardsKoth());
        subCommands.add(new StartKoth());
        subCommands.add(new StopKoth());

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("koth")){
            if(!commandSender.hasPermission("koth.admin")){
                commandSender.sendMessage(color("#ff0000¡No tienes permisos!"));
                return false;
            }
            if(args.length == 0){
                sendUnderline(commandSender, 'a');
                sendCenteredMessage(commandSender, "&a&lSUBCOMANDOS DE KOTH");
                for(SubCommand subCommand : subCommands){
                    sendCenteredMessage(commandSender, "&c/koth "+subCommand.getName()+" "+subCommand.getSyntax()+"&7 - &f"+subCommand.getDescription());
                }
                sendUnderline(commandSender, 'a');

                return false;
            }
            for(SubCommand subCommand : subCommands){
                if(subCommand.getName().equalsIgnoreCase(args[0])){
                    subCommand.execute(commandSender, Arrays.copyOfRange(args, 1, args.length));
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> ret = new ArrayList<>();
        if(args.length == 1){
            for(SubCommand subCommand : subCommands){
                ret.add(subCommand.getName());
            }
            return ret;
        } else {
            for(SubCommand subCommand : subCommands){
                if(subCommand.getName().equalsIgnoreCase(args[0])){
                    ret = subCommand.onTabComplete(commandSender, Arrays.copyOfRange(args, 1, args.length));
                    return ret;

                }
            }
        }
        return new ArrayList<>();
    }
}
