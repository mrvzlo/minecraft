package com.ave.smartminer.recipes;

import java.util.List;

import com.ave.smartminer.SmartMiner;
import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.ave.smartminer.datagen.ModTags;
import com.ave.smartminer.screen.SmartMinerScreen;
import com.google.common.collect.Lists;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIModPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(SmartMiner.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MinerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<SimpleRecipe> recipes = Lists.newArrayList();
        for (Holder<Item> item : BuiltInRegistries.ITEM.getTagOrEmpty(ModTags.Items.MINEABLE_TAG)) {
            ItemStack stack = new ItemStack(item);
            int consume = SmartMinerBlockEntity.ENERGY_PER_PROGRESS * SmartMinerBlockEntity.getSpeedMod(item.value());
            recipes.add(new SimpleRecipe(consume, stack,
                    SmartMinerBlockEntity.getOutputSize(item.value())));
        }
        registration.addRecipes(MinerRecipeCategory.REGULAR, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(SmartMiner.SMART_MINER_BLOCK.get()), MinerRecipeCategory.REGULAR);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(SmartMinerScreen.class, 156, 37, 8, 12, MinerRecipeCategory.REGULAR);
    }
}
