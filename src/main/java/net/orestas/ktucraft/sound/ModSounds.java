package net.orestas.ktucraft.sound;// ModSounds.java
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;

public class ModSounds {
    public static final SoundEvent THE_TRIARCH_BOSS_MUSIC = register("the_triarch_boss_music");

    private static SoundEvent register(String path) {
        Identifier id = Identifier.of(KTUCRAFT.MOD_ID, path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        KTUCRAFT.LOGGER.info("Registering Mod Sounds for " + KTUCRAFT.MOD_ID);
    }
}