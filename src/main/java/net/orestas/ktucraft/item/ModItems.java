package net.orestas.ktucraft.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.entity.ModEntities;
import net.orestas.ktucraft.item.custom.SeedOfTheFusionItem;

public class ModItems {
    public static final Item WARDENS_HEART = registerItem("wardens_heart", new Item(new Item.Settings().rarity(Rarity.EPIC)));
    public static final Item SEED_OF_THE_FUSION = registerItem("seed_of_the_fusion", new SeedOfTheFusionItem(new Item.Settings().rarity(Rarity.EPIC)));
    public static final Item THE_TRIARCH_SPAWN_EGG = registerItem("the_triarch_spawn_egg",
            new SpawnEggItem(ModEntities.THE_TRIARCH, 0x050a0d, 0x8422ba, new Item.Settings().rarity(Rarity.EPIC)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(KTUCRAFT.MOD_ID, name), item);
    }

    public static void registerModItems(){
        KTUCRAFT.LOGGER.info("Registering Mod Items for " + KTUCRAFT.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(WARDENS_HEART);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(THE_TRIARCH_SPAWN_EGG);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(SEED_OF_THE_FUSION);
        });
    }
}
