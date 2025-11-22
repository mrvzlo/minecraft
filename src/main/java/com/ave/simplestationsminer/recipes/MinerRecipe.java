package com.ave.simplestationsminer.recipes;

import com.ave.simplestationsminer.registrations.Registrations;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record MinerRecipe(Ingredient inputItem, ItemStack output) implements Recipe<MinerRecipeInput> {
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(MinerRecipeInput input, World world) {
        if (world.isClient())
            return false;

        return inputItem.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(MinerRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registrations.RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return Registrations.RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<MinerRecipe> {
        public static final MapCodec<MinerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(MinerRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(MinerRecipe::output)).apply(inst, MinerRecipe::new));

        public static final PacketCodec<RegistryByteBuf, MinerRecipe> STREAM_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, MinerRecipe::inputItem,
                ItemStack.PACKET_CODEC, MinerRecipe::output,
                MinerRecipe::new);

        @Override
        public MapCodec<MinerRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, MinerRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}