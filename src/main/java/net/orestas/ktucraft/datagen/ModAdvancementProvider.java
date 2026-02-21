package net.orestas.ktucraft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.RecipeCraftedCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeCraftedCriterion;
import net.minecraft.data.server.advancement.AdvancementTabGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.item.ModItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricDataOutput output,
                                  CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {

        // Vanilla parent: minecraft:adventure/root
        AdvancementEntry adventureRoot = AdvancementTabGenerator.reference("minecraft:adventure/root");

        // --- 1) Warden's Heart ---
        AdvancementEntry wardensHeart = Advancement.Builder.create()
                .parent(adventureRoot)
                .display(
                        ModItems.WARDENS_HEART,
                        Text.literal("Warden's Heart"),
                        Text.literal("Claim the heart of the deep."),
                        null, // background (nereikia, nes ne root tabas)
                        AdvancementFrame.CHALLENGE,
                        true,  // show_toast
                        true,  // announce_to_chat
                        false  // hidden
                )
                .criterion("has_heart", InventoryChangedCriterion.Conditions.items(ModItems.WARDENS_HEART))
                .rewards(AdvancementRewards.Builder.experience(500))
                .build(Identifier.of(KTUCRAFT.MOD_ID, "obtain_wardens_heart"));

        consumer.accept(wardensHeart);

        // --- 1) Warden's Heart ---
        AdvancementEntry seedOfTheFusion = Advancement.Builder.create()
                .parent(adventureRoot)
                .display(
                        ModItems.SEED_OF_THE_FUSION,
                        Text.literal("Planting Chaos"),
                        Text.literal("Craft a Seed of The Fusion"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true, true, false
                )
                .criterion("has_seed",
                        InventoryChangedCriterion.Conditions.items(ModItems.SEED_OF_THE_FUSION)
                )
                .rewards(AdvancementRewards.Builder.experience(150))
                .build(Identifier.of(KTUCRAFT.MOD_ID, "craft_a_seed_of_the_fusion"));

        consumer.accept(seedOfTheFusion);

        // --- 2) The Triarch (impossible, grantinsi per kodą) ---
        AdvancementEntry triarch = Advancement.Builder.create()
                .parent(adventureRoot)
                .display(
                        Items.NETHER_STAR,
                        Text.literal("The Triarch"),
                        Text.literal("Defeat the final fusion of three gods."),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("triggered", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .rewards(AdvancementRewards.Builder.experience(500))
                .build(Identifier.of(KTUCRAFT.MOD_ID, "kill_triarch"));

        consumer.accept(triarch);
    }
}