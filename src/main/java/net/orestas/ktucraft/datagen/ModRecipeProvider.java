package net.orestas.ktucraft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.orestas.ktucraft.block.ModBlocks;
import net.orestas.ktucraft.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SEED_OF_THE_FUSION, 1)
                .pattern("RRR")
                .pattern(" H ")
                .pattern(" S ")
                .input('R', Items.DRAGON_HEAD)
                .input('H', ModItems.WARDENS_HEART)
                .input('S', Items.NETHER_STAR)
                .criterion(hasItem(ModItems.WARDENS_HEART), conditionsFromItem(ModItems.WARDENS_HEART))
                .offerTo(recipeExporter);
    }
}
