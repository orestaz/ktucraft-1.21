package net.orestas.ktucraft.network.packet;

// TriarchBossMusicInstance.java (CLIENT)
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.random.Random;
import net.orestas.ktucraft.sound.ModSounds;

@Environment(EnvType.CLIENT)
public class TriarchBossMusicInstance extends MovingSoundInstance {
    private final MinecraftClient client;
    private final int bossId;

    public TriarchBossMusicInstance(MinecraftClient client, int bossId) {
        super(ModSounds.THE_TRIARCH_BOSS_MUSIC, SoundCategory.MUSIC, Random.create());
        this.client = client;
        this.bossId = bossId;

        this.repeat = true;
        this.repeatDelay = 0;
        this.attenuationType = SoundInstance.AttenuationType.NONE;
        this.volume = 1.0f;
        this.pitch = 1.0f;

        // optional: kad nesusimaišytų su vanilla muzika
        // client.getMusicTracker().stop();
    }

    @Override
    public void tick() {
        if (client.world == null || client.player == null) { setDone(); return; }

        Entity e = client.world.getEntityById(bossId);
        if (e == null || !e.isAlive()) { setDone(); return; }

        // pvz. groja tik jei player arti (256 blokai)
        if (client.player.squaredDistanceTo(e) > 256 * 256) { setDone(); return; }

        // pozicija nebūtina kai AttenuationType.NONE, bet ok palikt
        this.x = e.getX();
        this.y = e.getY();
        this.z = e.getZ();
    }
}