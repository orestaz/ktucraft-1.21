package net.orestas.ktucraft.entity.client;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class TheTriarchStarAnimations {
    public static final Animation ANIM_THE_TRIARCH_STAR_FLYING = Animation.Builder.create(0.75F).looping()
            .addBoneAnimation("root", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, -180.0F, 360.0F), Transformation.Interpolations.LINEAR)
            ))
            .build();
}
