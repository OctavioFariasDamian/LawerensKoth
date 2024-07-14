package net.lawerens.koth.model;

import lombok.Getter;
import net.lawerens.koth.LawerensKoth;
import net.lawerens.koth.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KothManager {

    private final @NotNull File folder;
    private final @Getter @NotNull List<Koth> koths = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public KothManager(LawerensKoth plugin) {
        this.folder =  new File(plugin.getDataFolder(), "koths");
        if(!folder.exists()) folder.mkdir();
        loadKoths();
    }

    public Koth get(String name){
        for(Koth koth : koths){
            if(koth.getName().equals(name)) return koth;
        }
        return null;
    }

    public void loadKoths(){
        koths.clear();
        if(folder.listFiles() == null) return;
        for(File file : Objects.requireNonNull(folder.listFiles())){
            if(!file.getName().endsWith(".yml")) continue;
            String kothName = file.getName().replace(".yml", "");
            FileConfiguration config = new YamlConfiguration();
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException("Error cargando "+file.getName()+":", e);
            }

            Location pos1 = null;
            if(config.getConfigurationSection("pos1") != null) {
                pos1 = LocationUtils.readLocation(Objects.requireNonNull(config.getConfigurationSection("pos1")));
            }
            Location pos2 = null;
            if(config.getConfigurationSection("pos2") != null) {
                pos2 = LocationUtils.readLocation(Objects.requireNonNull(config.getConfigurationSection("pos2")));
            }

            int takeTime = -1;
            if(config.isInt("takeTime")){
                takeTime = config.getInt("takeTime");
            }

            List<Reward> rewards = new ArrayList<>();

            if(config.getConfigurationSection("rewards") != null){
                for(String key : config.getConfigurationSection("rewards").getKeys(false)){
                    ConfigurationSection rS = config.getConfigurationSection("rewards."+key);
                    assert rS != null;
                    List<String> commands = new ArrayList<>();
                    List<ItemStack> items = new ArrayList<>();
                    double probability = -1;
                    if(rS.isList("commands")){
                        commands = rS.getStringList("commands");
                    }

                    if(rS.getConfigurationSection("items") != null){
                        for(String itemKey : rS.getConfigurationSection("items").getKeys(false)){
                            items.add(rS.getItemStack("items."+itemKey));
                        }
                    }

                    if(rS.isDouble("probability")){
                        probability = rS.getDouble("probability");
                    }
                    rewards.add(new Reward(key, items, commands, probability));
                }
            }

            Koth koth = new Koth(kothName, pos1, pos2, config, file, null, takeTime, rewards);
            koths.add(koth);
        }
    }
    public void saveKoth(Koth koth) throws IOException {
        File file = koth.getFile();
        if(!file.exists()) file.createNewFile();

        FileConfiguration config = koth.getConfig();
        if(koth.getPos1() != null) LocationUtils.saveLocation(config, file, "pos1", koth.getPos1());
        if(koth.getPos2() != null) LocationUtils.saveLocation(config, file, "pos2", koth.getPos2());

        if(koth.getTakeTime() != -1) config.set("takeTime", koth.getTakeTime());
        if(!koth.getRewards().isEmpty()){
            for(Reward reward : koth.getRewards()){
                config.set("rewards."+reward.getName()+".commands", reward.getCommands());
                ConfigurationSection iS = config.createSection("rewards."+reward.getName()+".items");
                for(int i = 0 ; i < reward.getItems().size() ; i++){
                    iS.set(String.valueOf(i), reward.getItems().get(i));
                }
                if(reward.getProbability() != -1){
                    config.set("rewards."+reward.getName()+".probability", reward.getProbability());
                }
            }
        }

        config.save(file);
    }

    public void createKoth(String name) throws IOException {
        File file = new File(folder, name + ".yml");

        file.createNewFile();

        FileConfiguration config = new YamlConfiguration();

        try{
            config.load(file);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        Koth koth = new Koth(name, null, null, config, file, null, -1, new ArrayList<>());
        getKoths().add(koth);
    }

    public void deleteKoth(Koth koth){
        File file = koth.getFile();
        if(file.exists())
            file.delete();

        getKoths().remove(koth);
    }
}
