package net.orestas.ktucraft.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;

public record TriarchMusicStopS2CPayload() implements CustomPayload {

    public static final CustomPayload.Id<TriarchMusicStopS2CPayload> ID =
            new CustomPayload.Id<>(Identifier.of(KTUCRAFT.MOD_ID, "the_triarch_music_stop"));

    public static final PacketCodec<RegistryByteBuf, TriarchMusicStopS2CPayload> CODEC =
            PacketCodec.of(
                    (TriarchMusicStopS2CPayload payload, RegistryByteBuf buf) -> {},
                    (RegistryByteBuf buf) -> new TriarchMusicStopS2CPayload()
            );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}