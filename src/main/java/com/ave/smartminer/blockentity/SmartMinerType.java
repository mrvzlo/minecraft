package com.ave.smartminer.blockentity;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum SmartMinerType implements StringRepresentable {
    Unknown(null),
    STONE(Items.STONE),
    GRAVEL(Items.GRAVEL),
    SAND(Items.SAND),
    COPPER(Items.COPPER_ORE),
    IRON(Items.IRON_ORE),
    GOLD(Items.GOLD_ORE),
    LAPIZ(Items.LAPIS_ORE);

    public final Item minedItem;

    SmartMinerType(Item item) {
        this.minedItem = item;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
