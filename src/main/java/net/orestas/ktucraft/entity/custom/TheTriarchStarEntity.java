package net.orestas.ktucraft.entity.custom;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.orestas.ktucraft.entity.ModEntities;

public class TheTriarchStarEntity extends WitherSkullEntity {

    public final AnimationState flyingAnimationState = new AnimationState();
    private int flyingAnimTimeout = 0;

    public TheTriarchStarEntity(EntityType<? extends WitherSkullEntity> type, World world) {
        super(type, world); // yra 1.21 konstruktorius su (EntityType, World) :contentReference[oaicite:0]{index=0}
    }
    private void setupAnimationStates() {
        if (this.flyingAnimTimeout <= 0) {
            this.flyingAnimTimeout = 15;
            this.flyingAnimationState.start(this.age);
        } else {
            --this.flyingAnimTimeout;
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 2F, true, World.ExplosionSourceType.MOB);
            this.discard();
        }
    }

    /** Patogus factory, kad kurtum kaip anksčiau: owner + kryptis */
    public static TheTriarchStarEntity create(World world, LivingEntity owner, Vec3d dir) {
        TheTriarchStarEntity skull = new TheTriarchStarEntity(ModEntities.THE_TRIARCH_STAR, world);
        skull.setOwner(owner); // ProjectileEntity#setOwner :contentReference[oaicite:1]{index=1}
        skull.setCharged(true);
        skull.setVelocity(dir.x, dir.y, dir.z, 2f, 0f);
        return skull;
    }
    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }
}