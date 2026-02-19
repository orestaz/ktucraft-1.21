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

public class ModItems {
    public static final Item PINK_GARNET = registerItem("pink_garnet", new Item(new Item.Settings()));
    public static final Item RAW_PINK_GARNET = registerItem("raw_pink_garnet", new Item(new Item.Settings()));
    public static final Item THE_TRIARCH_SPAWN_EGG = registerItem("the_triarch_spawn_egg",
            new SpawnEggItem(ModEntities.THE_TRIARCH, 0x050a0d, 0x8422ba, new Item.Settings().rarity(Rarity.EPIC)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(KTUCRAFT.MOD_ID, name), item);
    }

    public static void registerModItems(){
        KTUCRAFT.LOGGER.info("Registering Mod Items for " + KTUCRAFT.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(PINK_GARNET);
            entries.add(RAW_PINK_GARNET);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(THE_TRIARCH_SPAWN_EGG);
        });
    }
}
