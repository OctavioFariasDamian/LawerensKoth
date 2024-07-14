package net.lawerens.koth.commands.rewards;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.commands.SubCommand;
import net.lawerens.koth.model.Koth;
import net.lawerens.koth.model.Reward;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetProbabilityReward implements SubCommand {
    @Override
    public String getName() {
        return "setprobability";
    }

    @Override
    public String getDescription() {
        return "Establece la probabilidad de ganar una recompensa";
    }

    @Override
    public String getSyntax() {
        return "[koth] [group] [probability(decimal)]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendPrefixMessage(sender, "&cEl uso del comando es &7/koth rewards setprobability " + getSyntax());
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
            sendPrefixMessage(sender, "#ff0000Ese grupo no existe.");
            return;
        }

        double probability;
        try{
            probability = Double.parseDouble(args[2]);
        }catch (NumberFormatException e){
            sendPrefixMessage(sender, "#ff0000La probabilidad debe ser un número decimal ##.##");
            return;
        }

        koth.getGroup(group).setProbability(probability);
        try {
            LawerensKoth.getManager().saveKoth(koth);
        } catch (IOException e) {
            sendPrefixMessage(sender, "#ff0000Ha habido una excepción intentando guardar el koth.");
            throw new RuntimeException(e);
        }
        sendPrefixMessage(sender, "&fEstableciste la probabilidad del grupo &b" + group + " &fen &e"+probability+"&f en el koth &a" + koth.getName() + "&f.");

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
        return l;    }
}
