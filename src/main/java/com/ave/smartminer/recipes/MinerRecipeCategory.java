package com.ave.smartminer.recipes;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.ave.smartminer.SmartMiner;
import com.ave.smartminer.uihelpers.UIBlocks;
import com.google.common.collect.Lists;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class MinerRecipeCategory implements IRecipeCategory<SimpleRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(SmartMiner.MODID, "regular");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SmartMiner.MODID,
            "textures/gui/jei.png");

    public IGuiHelper guiHelper;
    public static RecipeType<SimpleRecipe> REGULAR = RecipeType.create(SmartMiner.MODID, "regular",
            SimpleRecipe.class);
    private final IDrawableStatic bg;

    public MinerRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        bg = guiHelper.createDrawable(TEXTURE, 0, 0, 176, 80);
    }

    @Override
    public RecipeType<SimpleRecipe> getRecipeType() {
        return REGULAR;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("screen.smartminer.recipes");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(SmartMiner.SMART_MINER_BLOCK_ITEM.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SimpleRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, UIBlocks.CATA_SLOT.left, UIBlocks.CATA_SLOT.top)
                .addIngredients(Ingredient.of(Items.REDSTONE));
        builder.addSlot(RecipeIngredientRole.INPUT, UIBlocks.FUEL_SLOT.left, UIBlocks.FUEL_SLOT.top)
                .addIngredients(Ingredient.of(Items.COAL));
        builder.addSlot(RecipeIngredientRole.INPUT, UIBlocks.COOL_SLOT.left, UIBlocks.COOL_SLOT.top)
                .addIngredients(Ingredient.of(Items.LAPIS_LAZULI));
        builder.addSlot(RecipeIngredientRole.OUTPUT, UIBlocks.OUT_SLOT.left, UIBlocks.OUT_SLOT.top)
                .addItemStack(new ItemStack(recipe.filter.getItem(), recipe.output));
        builder.addSlot(RecipeIngredientRole.CATALYST, UIBlocks.FILTER_SLOT.left, UIBlocks.FILTER_SLOT.top)
                .addIngredients(Ingredient.of(recipe.filter.getItem()));
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public List<Component> getTooltipStrings(SimpleRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX,
            double mouseY) {
        List<Component> list = Lists.newArrayList();
        if (UIBlocks.FUEL_BAR.isHovered(mouseX, mouseY))
            list.add(Component.literal(recipe.energy + " RF"));

        return list;
    }
}
