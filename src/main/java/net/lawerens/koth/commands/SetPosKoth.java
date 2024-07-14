package net.lawerens.koth.commands;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.model.Koth;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.lawerens.koth.utils.LocationUtils.locationToString;

public class SetPosKoth implements SubCommand{

    @Override
    public String getName() {
        return "setpos";
    }

    @Override
    public String getDescription() {
        return "Establecer posiciónes de un koth";
    }

    @Override
    public String getSyntax() {
        return "[koth] [1|2]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)){
            sendPrefixMessage(sender, "#ff0000Solo jugadores pueden usar este comando.");
            return;
        }
        if(args.length < 2){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth setpos [koth] [1|2]");
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

        int pos;

        try{
            pos = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            sendPrefixMessage(sender, "#ff0000Debes elegir un posición 1 o 2.");
            return;
        }

        if(pos != 1 && pos != 2){
            sendPrefixMessage(sender, "#ff0000Debes elegir un posición 1 o 2.");
            return;
        }

        RayTraceResult r = player.rayTraceBlocks(5);
        if(r == null || r.getHitBlock() == null){
            sendPrefixMessage(sender, "#ff0000Debes mirar a un bloque cercano.");
            return;
        }
        Location loc = r.getHitBlock().getLocation();
        if(pos == 1)
            koth.setPos1(loc);
        else
            koth.setPos2(loc);
        try {
            LawerensKoth.getManager().saveKoth(koth);
        } catch (IOException e) {
            sendPrefixMessage(sender, "#ff0000Ha habido una excepción intentando guardar el koth.");
            throw new RuntimeException(e);
        }
        sendPrefixMessage(sender, "&fEstableciste la posición &e"+pos+"&f del koth &a"+koth.getName()+" &fen &b"+locationToString(loc)+"&f.");
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
        else if(args.length == 2){
            l.add("1");
            l.add("2");
            return l;
        }
        return l;
    }
}
