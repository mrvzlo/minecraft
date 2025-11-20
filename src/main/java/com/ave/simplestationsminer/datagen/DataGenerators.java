package com.ave.simplestationsminer.datagen;

import java.util.concurrent.CompletableFuture;

import com.ave.simplestationsminer.SimpleStationsMiner;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = SimpleStationsMiner.MODID)
public class DataGenerators {
    @SubscribeEvent
    private static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput out = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeServer()) {
            ModBlockTagProvider blockTags = new ModBlockTagProvider(out, lookup, helper);
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new ModItemTagProvider(out, lookup, blockTags, helper));
            generator.addProvider(event.includeServer(), new ModRecipeProvider(out, lookup));
        }
    }
}
