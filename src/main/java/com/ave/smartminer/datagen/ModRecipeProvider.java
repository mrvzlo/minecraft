package com.ave.smartminer.datagen;

import java.util.concurrent.CompletableFuture;

import com.ave.smartminer.SmartMiner;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SmartMiner.SMART_MINER_DRILL.get())
                .pattern("LIL")
                .pattern("RIR")
                .pattern("IBI")
                .define('I', Items.IRON_INGOT)
                .define('B', Items.IRON_BLOCK)
                .define('L', Items.LAPIS_LAZULI)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SmartMiner.SMART_MINER_BLOCK.get())
                .pattern("DRD")
                .pattern("RCR")
                .pattern("HRH")
                .define('D', SmartMiner.SMART_MINER_DRILL.get())
                .define('H', Items.HOPPER)
                .define('C', Items.MINECART)
                .define('R', Items.POWERED_RAIL)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(consumer);
    }
}