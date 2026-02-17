package net.orestas.ktucraft.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.entity.custom.TheTriarchEntity;

public class TheTriarchRenderer extends MobEntityRenderer<TheTriarchEntity, TheTriarchModel<TheTriarchEntity>> {
    public TheTriarchRenderer(EntityRendererFactory.Context context) {
        super(context, new TheTriarchModel<>(context.getPart(TheTriarchModel.THE_TRIARCH)), 0.75f);
    }

    @Override
    public Identifier getTexture(TheTriarchEntity entity) {
        return Identifier.of(KTUCRAFT.MOD_ID, "textures/entity/the_triarch/the_triarch.png");
    }

    @Override
    public void render(TheTriarchEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.scale(2f, 2f, 2f);

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
