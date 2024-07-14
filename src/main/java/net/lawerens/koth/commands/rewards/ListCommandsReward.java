package net.lawerens.koth.commands.rewards;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.commands.SubCommand;
import net.lawerens.koth.model.Koth;
import net.lawerens.koth.model.Reward;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static net.lawerens.koth.utils.CenterMessage.sendCenteredMessage;
import static net.lawerens.koth.utils.CenterMessage.sendUnderline;

public class ListCommandsReward implements SubCommand {
    @Override
    public String getName() {
        return "listcommands";
    }

    @Override
    public String getDescription() {
        return "Lista los comandos de un grupo de recompensas de un koth.";
    }

    @Override
    public String getSyntax() {
        return "[koth] [grupo]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth rewards listcommands " + getSyntax());
            return;
        }
        Koth koth = LawerensKoth.getManager().get(args[0]);

        if (koth == null) {
            sendPrefixMessage(sender, "#ff0000Ese koth no existe.");
            return;
        }

        String group = args[1];
        Reward g = koth.getGroup(group);
        if (g == null) {
            sendPrefixMessage(sender, "#ff0000Ese grupo no existe.");
            return;
        }
        if(g.getCommands().isEmpty()){
            sendPrefixMessage(sender, "#ff0000¡Ese grupo aún no tiene comandos agregados!");
            return;
        }
        sendUnderline(sender, 'd');
        sender.sendMessage(" ");
        sendCenteredMessage(sender, "&d&lCOMANDOS RECOMPENSAS &7(&e"+group+"&7)");

        for(String cmd : g.getCommands()){
            sendMessage(sender, "&d* &f"+cmd.replace("%player%", "&e%player%&f"));
        }

        sender.sendMessage(" ");
        sendUnderline(sender, 'd');

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> l = new ArrayList<>();
        if(args.length == 1){
            for(Koth koth : LawerensKoth.getManager().getKoths()){
                l.add(koth.getName());
            }
            return l;
        }
        Koth koth = LawerensKoth.getManager().get(args[0]);

        if(args.length == 2){
            if(koth != null){
                for(Reward reward : koth.getRewards()) {
                    l.add(reward.getName());
                }
                return l;
            }
        }
        return l;
    }
}
