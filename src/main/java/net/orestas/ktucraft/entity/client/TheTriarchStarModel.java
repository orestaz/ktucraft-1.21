package net.orestas.ktucraft.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.entity.custom.TheTriarchStarEntity;
import net.orestas.ktucraft.entity.custom.TheTriarchEntity;

public class TheTriarchStarModel<T extends TheTriarchStarEntity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer THE_TRIARCH_STAR = new EntityModelLayer(Identifier.of(KTUCRAFT.MOD_ID, "the_triarch_star"), "main");

    private final ModelPart root;
    private final ModelPart cub1;
    private final ModelPart cube;
    private final ModelPart inner_glass;
    private final ModelPart outer_glass;
    private final ModelPart cub2;
    private final ModelPart cube2;
    private final ModelPart inner_glass2;
    private final ModelPart outer_glass2;
    private final ModelPart cub3;
    private final ModelPart cube3;
    private final ModelPart inner_glass3;
    private final ModelPart outer_glass3;
    private final ModelPart cub4;
    private final ModelPart cube13;
    private final ModelPart inner_glass13;
    private final ModelPart outer_glass13;
    public TheTriarchStarModel(ModelPart root) {
        this.root = root.getChild("root");
        this.cub1 = this.root.getChild("cub1");
        this.cube = this.cub1.getChild("cube");
        this.inner_glass = this.cub1.getChild("inner_glass");
        this.outer_glass = this.cub1.getChild("outer_glass");
        this.cub2 = this.root.getChild("cub2");
        this.cube2 = this.cub2.getChild("cube2");
        this.inner_glass2 = this.cub2.getChild("inner_glass2");
        this.outer_glass2 = this.cub2.getChild("outer_glass2");
        this.cub3 = this.root.getChild("cub3");
        this.cube3 = this.cub3.getChild("cube3");
        this.inner_glass3 = this.cub3.getChild("inner_glass3");
        this.outer_glass3 = this.cub3.getChild("outer_glass3");
        this.cub4 = this.root.getChild("cub4");
        this.cube13 = this.cub4.getChild("cube13");
        this.inner_glass13 = this.cub4.getChild("inner_glass13");
        this.outer_glass13 = this.cub4.getChild("outer_glass13");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 18.0F, 0.0F));

        ModelPartData cub1 = root.addChild("cub1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube = cub1.addChild("cube", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData inner_glass = cub1.addChild("inner_glass", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData outer_glass = cub1.addChild("outer_glass", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cub2 = root.addChild("cub2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7418F, 0.0F, 0.0F));

        ModelPartData cube2 = cub2.addChild("cube2", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData inner_glass2 = cub2.addChild("inner_glass2", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData outer_glass2 = cub2.addChild("outer_glass2", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cub3 = root.addChild("cub3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube3 = cub3.addChild("cube3", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData inner_glass3 = cub3.addChild("inner_glass3", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData outer_glass3 = cub3.addChild("outer_glass3", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cub4 = root.addChild("cub4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData cube13 = cub4.addChild("cube13", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData inner_glass13 = cub4.addChild("inner_glass13", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData outer_glass13 = cub4.addChild("outer_glass13", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override public ModelPart getPart() { return root; }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.updateAnimation(entity.flyingAnimationState, TheTriarchStarAnimations.ANIM_THE_TRIARCH_STAR_FLYING, animationProgress, 1f);
    }
}
