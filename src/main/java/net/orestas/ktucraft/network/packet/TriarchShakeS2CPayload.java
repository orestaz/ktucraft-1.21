package net.orestas.ktucraft.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;

public record TriarchShakeS2CPayload(double x, double y, double z, float strength) implements CustomPayload {
    public static final Id<TriarchShakeS2CPayload> ID =
            new CustomPayload.Id<>(Identifier.of(KTUCRAFT.MOD_ID, "triarch_shake"));

    public static final PacketCodec<RegistryByteBuf, TriarchShakeS2CPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE, TriarchShakeS2CPayload::x,
                    PacketCodecs.DOUBLE, TriarchShakeS2CPayload::y,
                    PacketCodecs.DOUBLE, TriarchShakeS2CPayload::z,
                    PacketCodecs.FLOAT,  TriarchShakeS2CPayload::strength,
                    TriarchShakeS2CPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}