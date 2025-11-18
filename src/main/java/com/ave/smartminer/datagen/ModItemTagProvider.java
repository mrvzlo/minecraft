package com.ave.smartminer.datagen;

import java.util.concurrent.CompletableFuture;

import com.ave.smartminer.SmartMiner;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            BlockTagsProvider blockTags,
            ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags.contentsGetter(), SmartMiner.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.MINEABLE_TAG)
                .add(Items.STONE)
                .add(Items.GRAVEL)
                .add(Items.SAND)
                .addTag(ItemTags.COAL_ORES)
                .addTag(ItemTags.IRON_ORES)
                .addTag(ItemTags.GOLD_ORES)
                .addTag(ItemTags.LAPIS_ORES)
                .addTag(ItemTags.DIAMOND_ORES)
                .addTag(ItemTags.EMERALD_ORES)
                .addTag(ItemTags.COPPER_ORES)
                .addTag(ItemTags.REDSTONE_ORES)
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "ores"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("forge", "ores"));

    }
}
