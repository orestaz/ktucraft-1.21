package net.orestas.ktucraft.network.packet;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModPackets {

    /** Kviesti kartą iš common init (KTUCRAFT.onInitialize()) */
    public static void registerPayloads() {
        // S2C payload registracija (turi žinoti codec)
        PayloadTypeRegistry.playS2C().register(TriarchMusicStartS2CPayload.ID, TriarchMusicStartS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TriarchMusicStopS2CPayload.ID, TriarchMusicStopS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TriarchShakeS2CPayload.ID, TriarchShakeS2CPayload.CODEC);
    }

    public static void sendStart(ServerPlayerEntity player, int triarchEntityId) {
        ServerPlayNetworking.send(player, new TriarchMusicStartS2CPayload(triarchEntityId));
    }

    public static void sendStop(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new TriarchMusicStopS2CPayload());
    }

    public static void sendShake(ServerPlayerEntity player, double x, double y, double z, float strength) {
        ServerPlayNetworking.send(player, new TriarchShakeS2CPayload(x, y, z, strength));
    }
}