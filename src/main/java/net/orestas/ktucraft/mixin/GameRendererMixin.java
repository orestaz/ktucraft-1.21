package net.orestas.ktucraft.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.orestas.ktucraft.client.TriarchScreenShake;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;
    // 1.21: bobView(MatrixStack, float) egzistuoja (private) :contentReference[oaicite:2]{index=2}
    @Inject(method = "bobView", at = @At("TAIL"))
    private void ktucraft$triarchShake(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        float s = TriarchScreenShake.getStrength(tickDelta);
        if (s <= 0f) return;
        if (client.world == null) return;

        float time = client.world.getTime() + tickDelta;

        float yaw = MathHelper.sin(time * 12f) * s * 2.0f;
        float pitch = MathHelper.cos(time * 15f) * s * 2.0f;

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));

        var rand = client.world.getRandom();
        float jx = (rand.nextFloat() - 0.5f) * 0.05f * s;
        float jy = (rand.nextFloat() - 0.5f) * 0.03f * s;
        matrices.translate(jx, jy, 0.0);
    }
}