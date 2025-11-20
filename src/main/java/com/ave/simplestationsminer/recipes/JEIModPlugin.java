package com.ave.simplestationsminer.recipes;

import java.util.List;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.datagen.ModTags;
import com.ave.simplestationsminer.screen.MinerScreen;
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
        return ResourceLocation.fromNamespaceAndPath(SimpleStationsMiner.MODID, "jei_plugin");
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
            int consume = MinerBlockEntity.ENERGY_PER_PROGRESS * MinerBlockEntity.getSpeedMod(item.value());
            recipes.add(new SimpleRecipe(consume, stack,
                    MinerBlockEntity.getOutputSize(item.value())));
        }
        registration.addRecipes(MinerRecipeCategory.REGULAR, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(SimpleStationsMiner.MINER_BLOCK.get()), MinerRecipeCategory.REGULAR);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MinerScreen.class, 156, 37, 8, 12, MinerRecipeCategory.REGULAR);
    }
}
