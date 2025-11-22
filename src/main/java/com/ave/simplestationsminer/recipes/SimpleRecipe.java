package com.ave.simplestationsminer.recipes;

import net.minecraft.item.ItemStack;

public class SimpleRecipe {
    public final int energy;
    public final ItemStack filter;
    public final int catalysis;
    public final int coolant;
    public final int output;

    public SimpleRecipe(int energy, ItemStack stack, int out) {
        this.energy = energy;
        this.filter = stack;
        catalysis = 1;
        coolant = 1;
        output = out;
    }
}