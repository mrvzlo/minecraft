package com.ave.smartminer.datagen;

import com.ave.smartminer.SmartMiner;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> MINEABLE_TAG = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(SmartMiner.MODID, "mineable"));
    }
}