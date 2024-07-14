package net.lawerens.koth.commands;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.model.Koth;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetTakeTimeKoth implements SubCommand{

    @Override
    public String getName() {
        return "settaketime";
    }

    @Override
    public String getDescription() {
        return "Establece el tiempo de captura de un koth";
    }

    @Override
    public String getSyntax() {
        return "[koth] [segundos]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth settaketime [koth] [segundos]");
            return;
        }

        Koth koth = LawerensKoth.getManager().get(args[0]);
        if(koth == null){
            sendPrefixMessage(sender, "#ff0000Ese koth no existe.");
            return;
        }

        if(koth.isStarted()){
            sendPrefixMessage(sender, "#ff0000No puedes editar un koth cuando esta iniciado.");
            return;
        }

        int seconds;

        try{
            seconds = Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            sendPrefixMessage(sender, "#ff0000¡"+args[1]+" no es un número entero!");
            return;
        }

        koth.setTakeTime(seconds);
        try {
            LawerensKoth.getManager().saveKoth(koth);
        } catch (IOException e) {
            sendPrefixMessage(sender, "#ff0000Ha habido una excepción intentando guardar el koth.");
            throw new RuntimeException(e);
        }
        sendPrefixMessage(sender, "&fEstableciste el tiempo de captura del koth &a"+koth.getName()+" &fa &e"+seconds+"&f.");

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
