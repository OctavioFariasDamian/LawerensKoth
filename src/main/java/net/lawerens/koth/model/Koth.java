package net.lawerens.koth.model;

import lombok.Data;
import me.clip.placeholderapi.PlaceholderAPI;
import net.lawerens.koth.LawerensKoth;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

import static net.lawerens.koth.utils.CenterMessage.sendCenteredMessage;
import static net.lawerens.koth.utils.CenterMessage.sendUnderline;
import static net.lawerens.koth.utils.Colorize.color;

@Data
public class Koth {

    private final @NotNull String name;
    private @Nullable Location pos1, pos2;
    private final @NotNull FileConfiguration config;
    private final @NotNull File file;
    private int endTime = 0;
    private int takingTime;
    private boolean started = false;
    private @Nullable Player dominating = null;
    private int task = -2;
    private int takeTime;
    private final @NotNull List<Reward> rewards;

    public Koth(@NotNull String name, @Nullable Location pos1, @Nullable Location pos2, @NotNull FileConfiguration config, @NotNull File file, @Nullable Player dominating, int takeTime, @NotNull List<Reward> rewards) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.config = config;
        this.file = file;
        this.dominating = dominating;
        this.takeTime = takeTime;
        this.rewards = rewards;
    }

    public boolean hasPlayerClan(Player p){
        return !getPlayerClan(p).contains("Sin clan");
    }

    public String getPlayerClan(Player p){
        return PlaceholderAPI.setPlaceholders(p, "%clans_name%");
    }

    public List<Player> getPlayersInWorld() {
        List<Player> li = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(PlaceholderAPI.setPlaceholders(p, "%worldguard_region_name%") == "koth") li.add(p);
        }
        return li;
    }

    public Reward getGroup(String name){
        for(Reward reward : rewards){
            if(reward.getName().equals(name)) return reward;
        }
        return null;
    }

    public void stop(CommandSender sender, String reason){
        for(Player player : getPlayersInWorld()) {
            sendUnderline(player, 'c');
            player.sendMessage(" ");
            sendCenteredMessage(player, "#ff0000&lKOTH FINALIZADO");
            player.sendMessage(" ");
            if (sender != null && reason != null) {
                sendCenteredMessage(player, "#fff5baEl koth fue detenido por &c" + sender.getName());
                sendCenteredMessage(player, "#fff5baRazón: &f" + reason);
            }else {
                sendCenteredMessage(player, "#fff5baEl tiempo general acabó");
            }
            sendCenteredMessage(player, "#fff5baEl koth finalizó sin ganador");

            player.sendMessage(" ");
            sendUnderline(player, 'c');
        }

        dominating = null;

        Bukkit.getScheduler().cancelTask(task);
        task = -2;
    }

    public void start() {
        for(Player player : getPlayersInWorld()) {
            sendUnderline(player, 'c');
            player.sendMessage(" ");
            sendCenteredMessage(player, "#ff0000&lKOTH INICIADO");
            player.sendMessage(" ");
            sendCenteredMessage(player, "#fff5ba¡El koth ha empezado! (/warp koth)");
            player.sendMessage(" ");
            sendUnderline(player, 'c');
        }
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(LawerensKoth.getPlugin(LawerensKoth.class), () -> {
            if(dominating != null && !dominating.isOnline()) dominating = null;
            if(endTime < 1) {
                stop(null, null);
                return;
            }
            // VERIFICAR CLAN POR JUGADOR UWU // HUBLAND.gg
            if(takingTime < 1){
                if(dominating == null){
                    for(Player player : getPlayersInWorld()) {
                        sendUnderline(player, 'c');
                        player.sendMessage(" ");
                        sendCenteredMessage(player, "#ff0000&lKOTH FINALIZADO");
                        player.sendMessage(" ");
                        sendCenteredMessage(player, "#fff5baEl koth finalizó sin ganador");
                        player.sendMessage(" ");
                        sendUnderline(player, 'c');
                    }
                }else{
                    for(Player player : getPlayersInWorld()) {
                        sendUnderline(player, 'c');
                        player.sendMessage(" ");
                        sendCenteredMessage(player, "#ff0000&lKOTH FINALIZADO");
                        player.sendMessage(" ");
                        sendCenteredMessage(player, "#fff5baEl koth finalizó teniendo");
                        assert dominating != null;
                        if(!hasPlayerClan(dominating))
                            sendCenteredMessage(player, "#fff5baa #ffca4f"+dominating.getName()+"#fff5ba como ganador");
                        else
                            sendCenteredMessage(player, "#fff5baa #ffca4f"+dominating.getName()+"#fff5ba(#ffca4f"+getPlayerClan(dominating)+"#fff5ba) como ganador");

                        player.sendMessage(" ");
                        sendUnderline(player, 'c');
                    }
                    Reward randomReward = getRandomReward();
                    
                    if(randomReward == null){
                        dominating.sendMessage(color("#ff0000No hemos encontrado ninguna recompensa, avisa a un administrador"));
                    }else{
                        List<Player> toGive = new ArrayList<>();
                        toGive.add(dominating);
                        if(hasPlayerClan(dominating)) {
                            for(Player player : getPlayersInWorld()){
                                if(player == dominating) continue;
                                if(getPlayerClan(player).equals(getPlayerClan(dominating))) toGive.add(player);
                            }
                        }
                        for(Player tg : toGive) {
                            for (String cmd : randomReward.getCommands()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", tg.getName()));
                            }

                            for (ItemStack item : randomReward.getItems()) {
                                if (isInventoryFull(tg)) {
                                    tg.getWorld().dropItemNaturally(tg.getLocation(), item);
                                    continue;
                                }
                                tg.getInventory().addItem(item);
                            }
                        }
                    }

                }

                dominating = null;
                Bukkit.getScheduler().cancelTask(task);
                task = -2;
                return;
            }else if(dominating != null && getPlayersBetweenLocations().isEmpty()){
                dominating = null;
                for(Player player : getPlayersInWorld()) {
                    sendUnderline(player, 'c');
                    player.sendMessage(" ");
                    sendCenteredMessage(player, "#ff0000&lDOMINIO KOTH");
                    player.sendMessage(" ");
                    sendCenteredMessage(player, "#fff5ba¡El koth se encuentra sin dominar!");
                    player.sendMessage(" ");
                    sendUnderline(player, 'c');
                }
                takingTime = takeTime;
            }else if(dominating == null && !getPlayersBetweenLocations().isEmpty()){
                dominating = getPlayersBetweenLocations().get(new SplittableRandom().nextInt(getPlayersBetweenLocations().size()));
                for(Player player : getPlayersInWorld()) {
                    sendUnderline(player, 'c');
                    player.sendMessage(" ");
                    sendCenteredMessage(player, "#ff0000&lDOMINIO KOTH");
                    player.sendMessage(" ");
                    sendCenteredMessage(player, "#fff5baEl koth empezó a ser dominado");
                    assert dominating != null;
                    sendCenteredMessage(player, "#fff5bapor #ffca4f"+dominating.getName());
                    player.sendMessage(" ");
                    sendUnderline(player, 'c');
                }
            }else{
                if(dominating != null) {
                    if (takingTime % 60 == 0) {
                        for (Player player : getPlayersInWorld()) {
                            sendUnderline(player, 'c');
                            player.sendMessage(" ");
                            sendCenteredMessage(player, "#ff0000&lKOTH");
                            player.sendMessage(" ");
                            sendCenteredMessage(player, "#fff5ba¡El koth será capturado");
                            sendCenteredMessage(player, "#fff5baen #ff596a" + takingTime + "#fff5ba segundos!");
                            player.sendMessage(" ");
                            sendUnderline(player, 'c');
                        }
                    }
                }
            }
            if(dominating != null) takingTime--;
            endTime--;
        }, 0L, 20L);
    }

    private boolean isInventoryFull(Player player) {
        PlayerInventory inventory = player.getInventory();
        for (ItemStack item : inventory.getStorageContents()) {
            if (item == null || item.getType().isAir()) {
                return false;
            }
        }
        return true;
    }

    public List<Player> getPlayersBetweenLocations() {

        assert pos2 != null;
        assert pos1 != null;
        Cuboid cuboid = new Cuboid(pos1, pos2);

        List<Player> players = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            if(cuboid.isIn(player)) players.add(player);
        }
        return players;
    }

    public Reward getRandomReward() {
        double sumaProbabilidades = 0.0;
        for (Reward recompensa : getRewards()) {
            sumaProbabilidades += recompensa.getProbability();
        }

        double numeroAleatorio = Math.random() * sumaProbabilidades;
        double sumaParcial = 0.0;

        for (Reward recompensa : getRewards()) {
            sumaParcial += recompensa.getProbability();
            if (numeroAleatorio <= sumaParcial) {
                return recompensa;
            }
        }

        return null;
    }
}
