package net.orestas.ktucraft.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup PINK_GARNET_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(KTUCRAFT.MOD_ID, "pink_garnet_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.WARDENS_HEART))
                    .displayName(Text.translatable("itemgroup.ktucraft.pink_garnet_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.THE_TRIARCH_SPAWN_EGG);
                        entries.add(ModItems.SEED_OF_THE_FUSION);
                        entries.add(ModItems.TRINITY_SWORD);
                        entries.add(ModItems.WARDENS_HEART);
                    }).build());


    public static void registerItemGroups() {
        KTUCRAFT.LOGGER.info("Registering Item Groups for " + KTUCRAFT.MOD_ID);
    }
}