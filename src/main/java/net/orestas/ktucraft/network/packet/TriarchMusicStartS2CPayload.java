package net.orestas.ktucraft.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;

public record TriarchMusicStartS2CPayload(int triarchEntityId) implements CustomPayload {

    public static final CustomPayload.Id<TriarchMusicStartS2CPayload> ID =
            new CustomPayload.Id<>(Identifier.of(KTUCRAFT.MOD_ID, "the_triarch_music_start"));

    public static final PacketCodec<RegistryByteBuf, TriarchMusicStartS2CPayload> CODEC =
            PacketCodec.of(
                    (TriarchMusicStartS2CPayload payload, RegistryByteBuf buf) ->
                            buf.writeVarInt(payload.triarchEntityId()),
                    (RegistryByteBuf buf) ->
                            new TriarchMusicStartS2CPayload(buf.readVarInt())
            );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}