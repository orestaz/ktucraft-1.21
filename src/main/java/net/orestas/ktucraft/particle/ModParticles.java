package net.orestas.ktucraft.particle;// ModParticles.java
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;

public class ModParticles {
    public static final SimpleParticleType THE_TRIARCH_SONIC_BOOM =
            Registry.register(Registries.PARTICLE_TYPE,
                    Identifier.of(KTUCRAFT.MOD_ID, "the_triarch_sonic_boom"),
                    FabricParticleTypes.simple());

    public static void register() {
        KTUCRAFT.LOGGER.info("Registering Mod Particles for " + KTUCRAFT.MOD_ID);
    }
}