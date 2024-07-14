package net.lawerens.koth.commands;

import net.lawerens.koth.LawerensKoth;
import org.bukkit.command.CommandSender;
import org.codehaus.plexus.util.StringUtils;

import java.io.IOException;
import java.util.List;

public class CreateKoth implements SubCommand{

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Crea un koth";
    }

    @Override
    public String getSyntax() {
        return "[nombre]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0){
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth create [nombre]");
            return;
        }
        String name = args[0];
        if(LawerensKoth.getManager().get(name) != null){
            sendPrefixMessage(sender, "#ff0000Ya existe un koth llamado así.");
            return;
        }

        if(!StringUtils.isAlphanumeric(name)){
            sendPrefixMessage(sender, "#ff0000El nombre '"+name+"' contiene caracteres inválidos.");
            return;
        }

        try {
            LawerensKoth.getManager().createKoth(name);
        } catch (IOException e) {
            sendPrefixMessage(sender, "#ff0000Ha habido una excepción al intentar crear el koth.");
            throw new RuntimeException(e);
        }
        sendPrefixMessage(sender, "&fCreaste el koth &a"+name+"&f.");

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
