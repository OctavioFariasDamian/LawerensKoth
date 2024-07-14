package net.lawerens.koth.commands;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.model.Koth;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RemoveKoth implements SubCommand{

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remueve un koth";
    }

    @Override
    public String getSyntax() {
        return "[koth]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth remove [koth]");
            return;
        }
        String name = args[0];
        if(LawerensKoth.getManager().get(name) == null){
            sendPrefixMessage(sender, "#ff0000Ese koth no existe.");
            return;
        }

        Koth koth = LawerensKoth.getManager().get(name);

        if(koth.isStarted()){
            sendPrefixMessage(sender, "#ff0000No puedes remover un koth cuando esta iniciado.");
            return;
        }

        LawerensKoth.getManager().deleteKoth(koth);
        sendPrefixMessage(sender, "&fRemoviste el koth &c"+name+"&f.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> l = new ArrayList<>();
        if(args.length == 1){
            for(Koth koth : LawerensKoth.getManager().getKoths()){
                l.add(koth.getName());
            }
        }
        return l;
    }
}
