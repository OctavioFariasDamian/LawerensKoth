package net.lawerens.koth.commands.rewards;

import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.commands.SubCommand;
import net.lawerens.koth.model.Koth;
import net.lawerens.koth.model.Reward;
import net.lawerens.koth.utils.LawerensInventory;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsRewards implements SubCommand, Listener {

    @Override
    public String getName() {
        return "items";
    }

    @Override
    public String getDescription() {
        return "Maneja los items de las recompensas de un koth.";
    }

    @Override
    public String getSyntax() {
        return "[koth] [grupo]";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)){
            sendPrefixMessage(sender, "#ff0000Solo jugadores pueden usar esta función.");
            return;
        }
        if (args.length < 2) {
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


        if (koth.getGroup(group) == null) {
            koth.getRewards().add(new Reward(
                    group, new ArrayList<>(), new ArrayList<>(), -1
            ));
            sendPrefixMessage(sender, "&fCreaste el grupo de recompensas &e" + group + "&f.");
        }

        Inventory inventory = new LawerensInventory(
                "&0Recompensas - "+group, 54, koth.getGroup(group).getItems(), "rewards-"+group+"-"+koth.getName()
        ).getInventory();

        player.openInventory(inventory);
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent e){
        if(e.getPlayer() instanceof Player p) {
            if(e.getInventory().getHolder() instanceof LawerensInventory) {
                String name = ((LawerensInventory) e.getInventory().getHolder()).getId();
                String[] s = name.split("-");
                if (s[0].equals("rewards")) {
                    Koth koth = LawerensKoth.getManager().get(s[2]);
                    if (koth == null) {
                        sendPrefixMessage(p, "#ff0000El koth que estabas editando dejó de existir.");
                        return;
                    }
                    Reward group = koth.getGroup(s[1]);
                    if(group == null){
                        sendPrefixMessage(p, "#ff0000El grupo que estabas editando dejó de existir.");
                        return;
                    }

                    List<ItemStack> items = new ArrayList<>();
                    for (int i = 0; i < e.getInventory().getSize(); i++) {
                        ItemStack item = e.getInventory().getItem(i);
                        if(item == null || item.getType() == Material.AIR) continue;
                        items.add(item);
;                   }
                    koth.getGroup(s[1]).getItems().clear();
                    koth.getGroup(s[1]).getItems().addAll(items);

                    try {
                        LawerensKoth.getManager().saveKoth(koth);
                    } catch (IOException ex) {
                        sendPrefixMessage(p, "#ff0000Ha habido una excepción intentando guardar el koth.");
                        throw new RuntimeException(ex);
                    }

                    sendPrefixMessage(p, "&fActualizaste los items del grupo &e"+group.getName()+" &fen el koth &a"+koth.getName()+"&f.");
                }
            }
        }
    }
}

