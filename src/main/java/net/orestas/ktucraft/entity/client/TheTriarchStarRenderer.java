package net.orestas.ktucraft.entity.client;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.entity.custom.TheTriarchEntity;
import net.orestas.ktucraft.entity.custom.TheTriarchStarEntity;


public class TheTriarchStarRenderer extends EntityRenderer<TheTriarchStarEntity> {
    private static final Identifier TEX = Identifier.of(KTUCRAFT.MOD_ID, "textures/entity/the_triarch/end_crystal.png");
    private final TheTriarchStarModel model;

    public TheTriarchStarRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new TheTriarchStarModel(ctx.getPart(TheTriarchStarModel.THE_TRIARCH_STAR));
    }

    @Override
    public void render(TheTriarchStarEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertices, int light) {

        matrices.push();

        // orientacija pagal yaw
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));

        // animacijos skaičiavimas (kad updateAnimation veiktų)
        float animProgress = entity.age + tickDelta;
        this.model.setAngles(entity, 0, 0, animProgress, 0, 0);

        VertexConsumer vc = vertices.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)));
        this.model.getPart().render(matrices, vc, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        matrices.translate(0.0, 0.6, 0.0);
        super.render(entity, yaw, tickDelta, matrices, vertices, light);
    }

    @Override
    public Identifier getTexture(TheTriarchStarEntity entity) {
        return TEX;
    }
}