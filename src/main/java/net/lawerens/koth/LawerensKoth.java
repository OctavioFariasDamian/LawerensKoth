package net.lawerens.koth;

import lombok.Getter;
import net.lawerens.koth.commands.KothCommand;
import net.lawerens.koth.model.Koth;
import net.lawerens.koth.model.KothManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LawerensKoth extends JavaPlugin {

    @Getter private static KothManager manager;

    @Override
    public void onLoad() {
        getDataFolder().mkdir();

    }

    @Override
    public void onEnable() {
        manager = new KothManager(this);
        Objects.requireNonNull(getCommand("koth")).setExecutor(new KothCommand());
        Objects.requireNonNull(getCommand("koth")).setTabCompleter(new KothCommand());

    }

    @Override
    public void onDisable() {
        for(Koth koth : getManager().getKoths()){
            if(koth.isStarted() && koth.getTask() != -2){
                getServer().getScheduler().cancelTask(koth.getTask());
            }
        }
    }
}
