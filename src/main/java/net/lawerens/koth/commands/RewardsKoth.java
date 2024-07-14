package net.lawerens.koth.commands;

import net.lawerens.koth.commands.rewards.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.lawerens.koth.utils.CenterMessage.sendCenteredMessage;
import static net.lawerens.koth.utils.CenterMessage.sendUnderline;

public class RewardsKoth implements SubCommand{

    private final List<SubCommand> subCommands = new ArrayList<>();

    public RewardsKoth(){
        subCommands.add(new AddCommandReward());
        subCommands.add(new RemoveCommandReward());
        subCommands.add(new ListCommandsReward());
        subCommands.add(new SetProbabilityReward());
        ItemsRewards itemsRewards = new ItemsRewards();
        subCommands.add(itemsRewards);
        Bukkit.getPluginManager().registerEvents(
                itemsRewards, Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("LawerensKoth"))
        );
    }
    @Override
    public String getName() {
        return "rewards";
    }

    @Override
    public String getDescription() {
        return "Maneja las recompensas de un koth";
    }

    @Override
    public String getSyntax() {
        return "<addcommand|listcommands|setprobability|removecommand|items> <koth> <group> [ID/Contenido...]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0){
            sendUnderline(sender, 'a');
            sendCenteredMessage(sender, "&a&lSUBCOMANDOS DE RECOMPENSAS DE KOTHS");
            for(SubCommand subCommand : subCommands){
                sendCenteredMessage(sender, "&c/koth rewards "+subCommand.getName()+" "+subCommand.getSyntax()+"&7 - &f"+subCommand.getDescription());
            }
            sendUnderline(sender, 'a');

            return;
        }
        for(SubCommand subCommand : subCommands){
            if(subCommand.getName().equalsIgnoreCase(args[0])){
                subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                return;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> ret = new ArrayList<>();
        if(args.length == 1){
            for(SubCommand subCommand : subCommands){
                ret.add(subCommand.getName());
            }
            return ret;
        } else {
            for(SubCommand subCommand : subCommands){
                if(subCommand.getName().equalsIgnoreCase(args[0])){
                    ret = subCommand.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
                    return ret;

                }
            }
        }
        return new ArrayList<>();
    }
}
