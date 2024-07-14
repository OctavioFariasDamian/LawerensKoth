package net.lawerens.koth.commands.rewards;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.commands.SubCommand;
import net.lawerens.koth.model.Koth;
import net.lawerens.koth.model.Reward;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddCommandReward implements SubCommand {

    @Override
    public String getName() {
        return "addcommand";
    }

    @Override
    public String getDescription() {
        return "Añade un comando a las recompensas de un koth.";
    }

    @Override
    public String getSyntax() {
        return "[koth] [grupo] [comando (sin /)]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth rewards addcommand " + getSyntax());
            return;
        }
        Koth koth = LawerensKoth.getManager().get(args[0]);

        if (koth == null) {
            sendPrefixMessage(sender, "#ff0000Ese koth no existe.");
            return;
        }

        if(koth.isStarted()){
            sendPrefixMessage(sender, "#ff0000No puedes editar un koth cuando esta iniciado.");
            return;
        }

        String group = args[1];

        if (koth.getGroup(group) == null) {
            koth.getRewards().add(new Reward(
                    group, new ArrayList<>(), new ArrayList<>(), -1
            ));
            sendPrefixMessage(sender, "&fCreaste el grupo de recompensas &e" + group + "&f.");
        }

        // addcommand koth grupo pito pitote
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        String command = sb.toString().trim();
        koth.getGroup(group).getCommands().add(command);
        try {
            LawerensKoth.getManager().saveKoth(koth);
        } catch (IOException e) {
            sendPrefixMessage(sender, "#ff0000Ha habido una excepción intentando guardar el koth.");
            throw new RuntimeException(e);
        }
        sendPrefixMessage(sender, "&fAñadiste el comando &e'" + command + "'&f del grupo &b" + group + " &fen el koth &a" + koth.getName() + "&f.");

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
                if(l.isEmpty()) l.add("[Nuevo Grupo]");
                return l;
            }
        }
        l.add("[comand (sin /)]");
        return l;
    }
}
