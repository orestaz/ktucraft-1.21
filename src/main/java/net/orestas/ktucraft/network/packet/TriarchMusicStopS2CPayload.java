package net.orestas.ktucraft.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;

public record TriarchMusicStopS2CPayload() implements CustomPayload {

    public static final Identifier RAW_ID = Identifier.of(KTUCRAFT.MOD_ID, "the_triarch_music_stop");
    public static final CustomPayload.Id<TriarchMusicStopS2CPayload> ID = new CustomPayload.Id<>(RAW_ID);

    public static final PacketCodec<RegistryByteBuf, TriarchMusicStopS2CPayload> CODEC =
            PacketCodec.unit(new TriarchMusicStopS2CPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}