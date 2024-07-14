package net.lawerens.koth.commands.rewards;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.commands.SubCommand;
import net.lawerens.koth.model.Koth;
import net.lawerens.koth.model.Reward;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoveCommandReward implements SubCommand {
    @Override
    public String getName() {
        return "removecommand";
    }

    @Override
    public String getDescription() {
        return "Remove un comando a las recompensas de un koth.";
    }

    @Override
    public String getSyntax() {
        return "[koth] [grupo] [comando (sin /) o ID]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth rewards removecommand " + getSyntax());
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
        String command = "";
        if (koth.getGroup(group) == null) {
            sendPrefixMessage(sender, "#ff0000Ese grupo no existe.");
            return;
        }

        Integer index = null;

        try{
            index = Integer.parseInt(args[2]);
        }catch (NumberFormatException e){
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }

            command = sb.toString().trim();
            if(koth.getGroup(group).getCommands().contains(command)) {
                koth.getGroup(group).getCommands().remove(command);
            }else{
                sendPrefixMessage(sender, "#ff0000No se encontro ese comando en ese grupo.");
                return;
            }
        }

        if(index != null){
            int size = koth.getGroup(group).getCommands().size();
            if(koth.getGroup(group).getCommands().isEmpty() || index < 0 || index > size || koth.getGroup(group).getCommands().get(index) == null){
                sendPrefixMessage(sender, "#ff0000No se encuentra ningun comando en el index "+index+".");
                return;
            }

            command = koth.getGroup(group).getCommands().get(index);
            koth.getGroup(group).getCommands().remove(index.intValue());
        }

        try {
            LawerensKoth.getManager().saveKoth(koth);
        } catch (IOException e) {
            sendPrefixMessage(sender, "#ff0000Ha habido una excepción intentando guardar el koth.");
            throw new RuntimeException(e);
        }
        sendPrefixMessage(sender, "&fRemoveiste el comando &e'" + command + "'&f del grupo &b" + group + " &fen el koth &a" + koth.getName() + "&f.");

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
        l.add("[comand (sin /)]");
        return l;
    }
}
