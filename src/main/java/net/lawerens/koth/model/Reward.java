package net.lawerens.koth.model;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
public class Reward {

    private String name;
    private final List<ItemStack> items;
    private final List<String> commands;
    private double probability;

    public Reward(String name, List<ItemStack> items, List<String> commands, double probability) {
        this.name = name;
        this.items = items;
        this.commands = commands;
        this.probability = probability;
    }

}
