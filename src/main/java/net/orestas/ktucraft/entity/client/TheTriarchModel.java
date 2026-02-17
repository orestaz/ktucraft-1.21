package net.orestas.ktucraft.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.orestas.ktucraft.KTUCRAFT;
import net.orestas.ktucraft.entity.custom.TheTriarchEntity;

public class TheTriarchModel<T extends TheTriarchEntity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer THE_TRIARCH = new EntityModelLayer(Identifier.of(KTUCRAFT.MOD_ID, "the_triarch"), "main");

    private final ModelPart root;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart body4;
    private final ModelPart head;
    private final ModelPart mirrored;
    private final ModelPart head4;
    private final ModelPart mirrored2;
    private final ModelPart head5;
    private final ModelPart mirrored3;
    private final ModelPart jaw;
    private final ModelPart jaw2;
    private final ModelPart jaw3;
    public TheTriarchModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body1 = this.root.getChild("body1");
        this.body2 = this.root.getChild("body2");
        this.body3 = this.root.getChild("body3");
        this.body4 = this.root.getChild("body4");
        this.head = this.root.getChild("head");
        this.mirrored = this.head.getChild("mirrored");
        this.head4 = this.root.getChild("head4");
        this.mirrored2 = this.head4.getChild("mirrored2");
        this.head5 = this.root.getChild("head5");
        this.mirrored3 = this.head5.getChild("mirrored3");
        this.jaw = this.root.getChild("jaw");
        this.jaw2 = this.root.getChild("jaw2");
        this.jaw3 = this.root.getChild("jaw3");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(-1.7F, -1.5566F, 3.353F));

        ModelPartData body1 = root.addChild("body1", ModelPartBuilder.create().uv(14, 97).cuboid(-13.5F, -3.1F, -16.5F, 26.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.7F, 1.5566F, 9.897F));

        ModelPartData body2 = root.addChild("body2", ModelPartBuilder.create().uv(6, 39).cuboid(-6.0F, -8.9F, -13.5F, 15.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(6, 39).cuboid(-6.0F, -6.5F, -13.5F, 15.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(6, 39).cuboid(-6.0F, -4.0F, -13.5F, 15.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(6, 39).cuboid(-6.0F, -1.5F, -13.5F, 15.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(8, 11).cuboid(-4.0F, -10.0F, -15.0F, 11.0F, 13.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.7F, 8.4566F, 9.397F, 0.2182F, 0.0F, 0.0F));

        ModelPartData body3 = root.addChild("body3", ModelPartBuilder.create().uv(26, 87).cuboid(-2.5F, 5.0F, -8.0F, 5.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.7F, 4.2248F, -0.7635F, 0.6109F, 0.0F, 0.0F));

        ModelPartData body4 = root.addChild("body4", ModelPartBuilder.create().uv(28, 90).cuboid(-1.5F, -1.0F, -7.0F, 3.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(2.2F, 14.5545F, 6.6594F, 1.0472F, 0.0F, 0.0F));

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(85, 93).cuboid(1.0F, 3.0F, -19.0F, 5.0F, 3.0F, 9.0F, new Dilation(0.0F))
                .uv(60, 55).cuboid(-1.0F, -2.0F, -10.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F))
                .uv(43, 114).cuboid(5.0F, -5.0F, -8.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(70, 37).cuboid(4.0F, 2.0F, -18.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.3F, -9.4434F, -0.103F));

        ModelPartData mirrored = head.addChild("mirrored", ModelPartBuilder.create().uv(70, 37).mirrored().cuboid(2.0F, -4.0F, -42.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(43, 114).mirrored().cuboid(0.0F, -11.0F, -32.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 6.0F, 24.0F));

        ModelPartData head4 = root.addChild("head4", ModelPartBuilder.create().uv(85, 93).cuboid(1.0F, 3.0F, -19.0F, 5.0F, 3.0F, 9.0F, new Dilation(0.0F))
                .uv(60, 55).cuboid(-1.0F, -2.0F, -10.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F))
                .uv(43, 114).cuboid(5.0F, -5.0F, -8.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(70, 37).cuboid(4.0F, 2.0F, -18.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-12.3F, -9.4434F, -0.103F));

        ModelPartData mirrored2 = head4.addChild("mirrored2", ModelPartBuilder.create().uv(70, 37).mirrored().cuboid(2.0F, -4.0F, -42.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(43, 114).mirrored().cuboid(0.0F, -11.0F, -32.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 6.0F, 24.0F));

        ModelPartData head5 = root.addChild("head5", ModelPartBuilder.create().uv(85, 93).cuboid(1.0F, 3.0F, -19.0F, 5.0F, 3.0F, 9.0F, new Dilation(0.0F))
                .uv(60, 55).cuboid(-1.0F, -2.0F, -10.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F))
                .uv(43, 114).cuboid(5.0F, -5.0F, -8.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(70, 37).cuboid(4.0F, 2.0F, -18.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(9.7F, -9.4434F, -0.103F));

        ModelPartData mirrored3 = head5.addChild("mirrored3", ModelPartBuilder.create().uv(70, 37).mirrored().cuboid(2.0F, -4.0F, -42.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(43, 114).mirrored().cuboid(0.0F, -11.0F, -32.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 6.0F, 24.0F));

        ModelPartData jaw = root.addChild("jaw", ModelPartBuilder.create().uv(85, 114).cuboid(1.0F, 2.0F, -11.0F, 5.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.3F, -5.4434F, -8.103F));

        ModelPartData jaw2 = root.addChild("jaw2", ModelPartBuilder.create().uv(85, 114).cuboid(1.0F, 2.0F, -11.0F, 5.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(-12.3F, -5.4434F, -8.103F));

        ModelPartData jaw3 = root.addChild("jaw3", ModelPartBuilder.create().uv(85, 114).cuboid(1.0F, 2.0F, -11.0F, 5.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(9.7F, -5.4434F, -8.103F));
        return TexturedModelData.of(modelData, 128, 128);
    }
    @Override
    public void setAngles(TheTriarchEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(netHeadYaw, headPitch);

        this.animateMovement(TheTriarchAnimations.ANIM_THE_TRIARCH_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.idleAnimationState, TheTriarchAnimations.ANIM_THE_TRIARCH_IDLE, ageInTicks, 1f);
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        root.render(matrices, vertexConsumer, light, overlay, color);
    }
    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
        this.jaw.yaw = headYaw * 0.017453292F;
        this.jaw.pitch = headPitch * 0.017453292F;

        this.head4.yaw = headYaw * 0.017453292F;
        this.head4.pitch = headPitch * 0.017453292F;
        this.jaw2.yaw = headYaw * 0.017453292F;
        this.jaw2.pitch = headPitch * 0.017453292F;

        this.head5.yaw = headYaw * 0.017453292F;
        this.head5.pitch = headPitch * 0.017453292F;
        this.jaw3.yaw = headYaw * 0.017453292F;
        this.jaw3.pitch = headPitch * 0.017453292F;
    }
    @Override
    public ModelPart getPart(){
        return root;
    }
}
