package net.orestas.ktucraft.network.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.orestas.ktucraft.client.TriarchScreenShake;

public class TriarchMusicClient {

    private static TriarchBossMusicInstance current;

    /** Kviesti iš KtuCraftClient.onInitializeClient() */
    public static void initReceivers() {

        ClientPlayNetworking.registerGlobalReceiver(TriarchMusicStartS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> start(context.client(), payload.triarchEntityId()));
        });

        ClientPlayNetworking.registerGlobalReceiver(TriarchMusicStopS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> stop(context.client()));
        });

        ClientPlayNetworking.registerGlobalReceiver(TriarchShakeS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                MinecraftClient client = context.client();
                if (client.player == null) return;

                // dist-based falloff, kad kratytų tik arti boom’o
                double dist = client.player.squaredDistanceTo(payload.x(), payload.y(), payload.z());
                float radius = 48f; // blokai
                float factor = 1.0f - (float) (Math.sqrt(dist) / radius);
                factor = MathHelper.clamp(factor, 0f, 1f);
                if (factor <= 0f) return;

                int duration = (int) (10 + 10 * factor); // 0.5s–1s
                TriarchScreenShake.trigger(duration, payload.strength() * factor);
            });
        });
    }

    private static void start(MinecraftClient client, int bossId) {
        stop(client);
        current = new TriarchBossMusicInstance(client, bossId);
        client.getSoundManager().play(current);
    }

    public static void stop(MinecraftClient client) {
        if (current != null) {
            client.getSoundManager().stop(current);
            current = null;
        }
    }

    public static void tick(MinecraftClient client) {
        if (current != null && client.world != null) {
            current.tick();
        }
    }
}