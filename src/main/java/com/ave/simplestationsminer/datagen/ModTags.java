package com.ave.simplestationsminer.datagen;

import com.ave.simplestationsminer.SimpleStationsMiner;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> MINEABLE_TAG = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(SimpleStationsMiner.MODID, "mineable"));
    }
}