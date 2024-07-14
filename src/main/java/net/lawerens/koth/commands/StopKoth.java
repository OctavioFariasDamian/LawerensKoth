package net.lawerens.koth.commands;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.model.Koth;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StopKoth implements SubCommand{
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Detiene un koth";
    }

    @Override
    public String getSyntax() {
        return "[koth] [reason...]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth start [koth] [segundos]");
            return;
        }

        Koth koth = LawerensKoth.getManager().get(args[0]);

        if(koth == null){
            sendPrefixMessage(sender, "#ff0000Ese koth no existe.");
            return;
        }

        if(!koth.isStarted()){
            sendPrefixMessage(sender, "#ff0000Ese koth aún no ha iniciado.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length ; i++) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString().trim();

        koth.stop(sender, reason);
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
