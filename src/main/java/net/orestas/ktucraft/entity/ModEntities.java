package net.orestas.ktucraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.entity.custom.TheTriarchEntity;
import net.orestas.ktucraft.entity.custom.TheTriarchStarEntity;

public class ModEntities {
    public static final EntityType<TheTriarchEntity> THE_TRIARCH = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(KTUCRAFT.MOD_ID, "the_triarch"),
            EntityType.Builder.create(TheTriarchEntity::new, SpawnGroup.MONSTER)
                    .dimensions(2f, 3f).build());

    public static final EntityType<TheTriarchStarEntity> THE_TRIARCH_STAR =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of(KTUCRAFT.MOD_ID, "the_triarch_star"),
                    EntityType.Builder.create(TheTriarchStarEntity::new, SpawnGroup.MISC)
                            .dimensions(1f, 1f)
                            .maxTrackingRange(16)
                            .trackingTickInterval(1)
                            .build()
            );

    public static void registerModEntities(){
        KTUCRAFT.LOGGER.info("Registering Mod Entities for " + KTUCRAFT.MOD_ID);
    }
}
