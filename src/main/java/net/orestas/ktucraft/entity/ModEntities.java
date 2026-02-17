package net.orestas.ktucraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.entity.custom.TheTriarchEntity;

public class ModEntities {
    public static final EntityType<TheTriarchEntity> THE_TRIARCH = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(KTUCRAFT.MOD_ID, "the_triarch"),
            EntityType.Builder.create(TheTriarchEntity::new, SpawnGroup.MONSTER)
                    .dimensions(2f, 3f).build());

    public static void registerModEntities(){
        KTUCRAFT.LOGGER.info("Registering Mod Entities for " + KTUCRAFT.MOD_ID);
    }
}
