// package com.ave.simplestationsminer.registrations;

// import java.util.concurrent.CompletableFuture;

// import com.ave.simplestationsminer.SimpleStationsMiner;

// import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
// import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
// import net.minecraft.data.recipe.RecipeExporter;
// import net.minecraft.data.recipe.RecipeGenerator;
// import net.minecraft.data.server.recipe.RecipeProvider;
// import net.minecraft.item.ItemConvertible;
// import net.minecraft.item.Items;
// import net.minecraft.recipe.book.RecipeCategory;
// import net.minecraft.registry.RegistryKey;
// import net.minecraft.registry.RegistryKeys;
// import net.minecraft.registry.RegistryWrapper;
// import net.minecraft.util.Identifier;

// import java.util.List;
// import java.util.concurrent.CompletableFuture;

// public class ModRecipeProvider extends RecipeProvider {
// public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
// super(output, registriesFuture);
// }

// @Override
// protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
// return new RecipeGenerator(wrapperLookup, recipeExporter) {
// @Override
// public void generate() {
// createShaped(RecipeCategory.MISC, Registrations.DRILL_ITEM)
// .pattern("LIL")
// .pattern("RIR")
// .pattern("IBI")
// .define('I', Items.IRON_INGOT)
// .define('B', Items.IRON_BLOCK)
// .define('L', Items.LAPIS_LAZULI)
// .define('R', Items.REDSTONE)
// .unlockedBy("has_iron", has(Items.IRON_INGOT))
// .offerTo(exporter);

// createShaped(RecipeCategory.MISC, Registrations.MINER_BLOCK)
// .pattern("DRD")
// .pattern("RCR")
// .pattern("HRH")
// .define('D', Registrations.DRILL_ITEM)
// .define('H', Items.HOPPER)
// .define('C', Items.MINECART)
// .define('R', Items.POWERED_RAIL)
// .unlockedBy("has_iron", has(Items.IRON_INGOT))
// .offerTo(exporter);
// }
// };
// }

// @Override
// public String getName() {
// return "TODO";
// }
// }