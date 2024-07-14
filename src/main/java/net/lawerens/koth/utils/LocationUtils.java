package net.lawerens.koth.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class LocationUtils {

    public static void saveLocation(FileConfiguration config, File file, String path, Location loc) throws IOException {
        ConfigurationSection s = config.createSection(path);
        s.set("x", loc.getX());
        s.set("y", loc.getY());
        s.set("z", loc.getZ());
        s.set("yaw", loc.getYaw());
        s.set("pitch", loc.getPitch());
        s.set("world", loc.getWorld().getName());
        config.save(file);
    }

    public static Location readLocation(ConfigurationSection s) {
        return new Location(
                Bukkit.getWorld(s.getString("world")),
                s.getDouble("x"),
                s.getDouble("y"),
                s.getDouble("z"),
                Float.parseFloat(s.getString("yaw")),
                Float.parseFloat(s.getString("pitch"))
        );
    }

    public static String locationToString(Location loc){
        return loc.getWorld().getName()+", "+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+", "+loc.getYaw()+", "+loc.getPitch();
    }
}
