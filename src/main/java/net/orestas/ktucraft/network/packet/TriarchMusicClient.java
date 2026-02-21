package net.orestas.ktucraft.network.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class TriarchMusicClient {
    private static TriarchBossMusicInstance current;

    public static void initReceivers() {

        ClientPlayNetworking.registerGlobalReceiver(TriarchMusicStartS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> start(context.client(), payload.triarchEntityId()));
        });

        ClientPlayNetworking.registerGlobalReceiver(TriarchMusicStopS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> stop(context.client()));
        });
    }

    private static void start(MinecraftClient client, int bossId) {
        stop(client);
        current = new TriarchBossMusicInstance(client, bossId);
        client.getSoundManager().play(current);
    }

    private static void stop(MinecraftClient client) {
        if (current != null) {
            client.getSoundManager().stop(current);
            current = null;
        }
    }
}