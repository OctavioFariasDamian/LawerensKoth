package net.lawerens.koth.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.lawerens.koth.utils.Colorize.color;

public class LawerensInventory implements InventoryHolder {

    private final @NotNull Inventory inventory;
    @Getter private final String id;

    public LawerensInventory(String title, int size, List<ItemStack> items, String id){
        this.id = id;
        inventory = Bukkit.createInventory(this, size, color(title));

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            inventory.setItem(i, item);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }


}
