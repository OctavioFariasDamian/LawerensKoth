package net.lawerens.koth.commands;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.model.Koth;
import net.lawerens.koth.model.Reward;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StartKoth implements SubCommand{
    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Inicia un koth";
    }

    @Override
    public String getSyntax() {
        return "[koth] [segundos]";
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

        if(koth.isStarted()){
            sendPrefixMessage(sender, "#ff0000Ese koth ya ha iniciado.");
            return;
        }

        if(koth.getPos1() == null || koth.getPos2() == null){
            sendPrefixMessage(sender, "#ff0000Los posiciones del koth deben ser definidas.");
            return;
        }

        if(koth.getTakeTime() == -1){
            sendPrefixMessage(sender, "#ff0000El tiempo de captura aún no esta definido.");
            return;
        }

        if(koth.getRewards().isEmpty()){
            sendPrefixMessage(sender, "#ff0000Por lo menos, debe de haber un grupo de recompensas en el koth.");
            return;
        }

        for(Reward r : koth.getRewards()){
            if(r.getCommands().isEmpty() && r.getItems().isEmpty()){
                sendPrefixMessage(sender, "#ff000El grupo de recompensas "+r.getName()+" no tiene recompensas.");
                return;
            }
            if(r.getProbability() == -1){
                sendPrefixMessage(sender, "#ff0000El grupo de recompensas "+r.getName()+" no tiene definida la probabilidad.");
                return;
            }
        }

        int time;
        try{
            time = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            sendPrefixMessage(sender, "#ff0000El tiempo debe ser un número entero.");
            return;
        }
        koth.setEndTime(time);
        koth.setTakingTime(koth.getTakeTime());
        koth.start();
        sendPrefixMessage(sender, "&f¡Iniciaste el koth &a"+koth.getName()+"&f!");

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
