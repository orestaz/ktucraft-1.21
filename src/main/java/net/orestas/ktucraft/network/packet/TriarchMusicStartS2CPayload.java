package net.orestas.ktucraft.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;

public record TriarchMusicStartS2CPayload(int triarchEntityId) implements CustomPayload {

    public static final Identifier RAW_ID = Identifier.of(KTUCRAFT.MOD_ID, "the_triarch_music_start");
    public static final CustomPayload.Id<TriarchMusicStartS2CPayload> ID = new CustomPayload.Id<>(RAW_ID);

    public static final PacketCodec<RegistryByteBuf, TriarchMusicStartS2CPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.VAR_INT, TriarchMusicStartS2CPayload::triarchEntityId, TriarchMusicStartS2CPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}