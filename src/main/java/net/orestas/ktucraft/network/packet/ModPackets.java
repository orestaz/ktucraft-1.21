package net.orestas.ktucraft.network.packet;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModPackets {

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(TriarchMusicStartS2CPayload.ID, TriarchMusicStartS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TriarchMusicStopS2CPayload.ID, TriarchMusicStopS2CPayload.CODEC);
    }

    public static void sendStart(ServerPlayerEntity player, int triarchEntityId) {
        ServerPlayNetworking.send(player, new TriarchMusicStartS2CPayload(triarchEntityId));
    }

    public static void sendStop(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new TriarchMusicStopS2CPayload());
    }
}