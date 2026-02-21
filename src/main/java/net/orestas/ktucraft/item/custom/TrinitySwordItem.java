package net.orestas.ktucraft.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TrinitySwordItem extends SwordItem {

    // Balansas
    private static final float PROC_CHANCE = 0.3f;     // 30% (jau matysis)
    private static final int PROC_CD = 20 * 5;          // 5s
    private static final int ABILITY_CD = 20 * 10;      // 10s

    public TrinitySwordItem(ToolMaterials materials, Settings settings) {
        super(materials, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    // --- BASIC HIT: visada matomas efektas + kartais "Rupture proc" ---
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean ok = super.postHit(stack, target, attacker);
        if (attacker.getWorld().isClient()) return ok;

        if (!(attacker instanceof PlayerEntity player)) return ok;
        if (!(attacker.getWorld() instanceof ServerWorld sw)) return ok;

        // 1) VISADA: trinity hit VFX (kad niekad nebūtų "nieko nevyksta")
        sw.spawnParticles(ParticleTypes.ENCHANTED_HIT,
                target.getX(), target.getBodyY(0.6), target.getZ(),
                18, 0.35, 0.35, 0.35, 0.01);

        sw.spawnParticles(ParticleTypes.SCULK_SOUL,
                target.getX(), target.getBodyY(0.5), target.getZ(),
                8, 0.25, 0.25, 0.25, 0.01);

        sw.spawnParticles(ParticleTypes.DRAGON_BREATH,
                target.getX(), target.getBodyY(0.5), target.getZ(),
                6, 0.25, 0.25, 0.25, 0.002);

        sw.playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 0.8f, 1.2f);

        // 2) PROC: "Trinity Rupture" (AOE + garsai) su cooldown
        if (!player.getItemCooldownManager().isCoolingDown(this) && sw.random.nextFloat() < PROC_CHANCE) {
            trinityRupture(sw, player, target);
            player.getItemCooldownManager().set(this, PROC_CD);
        }

        return ok;
    }

    private void trinityRupture(ServerWorld sw, PlayerEntity player, LivingEntity center) {
        // Garsai – rimtai girdisi
        sw.playSound(null, center.getX(), center.getY(), center.getZ(),
                SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1.2f, 0.9f);
        sw.playSound(null, center.getX(), center.getY(), center.getZ(),
                SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.1f);
        sw.playSound(null, center.getX(), center.getY(), center.getZ(),
                SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 0.9f, 1.25f);

        // Didelis VFX “burst”
        sw.spawnParticles(ParticleTypes.PORTAL,
                center.getX(), center.getBodyY(0.5), center.getZ(),
                90, 0.9, 0.55, 0.9, 0.25);

        sw.spawnParticles(ParticleTypes.SCULK_CHARGE_POP,
                center.getX(), center.getBodyY(0.5), center.getZ(),
                45, 0.7, 0.45, 0.7, 0.02);

        sw.spawnParticles(ParticleTypes.DRAGON_BREATH,
                center.getX(), center.getBodyY(0.4), center.getZ(),
                35, 0.8, 0.35, 0.8, 0.01);

        // AOE: 3.5 blokų radius (ne OP, bet jaučiasi)
        Box box = center.getBoundingBox().expand(3.5);
        for (LivingEntity e : sw.getEntitiesByClass(LivingEntity.class, box,
                e -> e.isAlive() && e != player)) {

            // papildomas dmg (saikingas)
            e.damage(player.getDamageSources().magic(), 8.0f);

            // lengvas “pull / shake”
            Vec3d dir = e.getPos().subtract(center.getPos());
            if (dir.lengthSquared() > 0.0001) {
                Vec3d n = dir.normalize();
                e.takeKnockback(0.8, n.x, n.z);
            }
        }
    }

    // --- RIGHT CLICK ABILITY: "Void Cleave" cone slash ---
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient()) {
            return TypedActionResult.success(stack);
        }

        if (!(world instanceof ServerWorld sw)) {
            return TypedActionResult.pass(stack);
        }

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.pass(stack);
        }

        // vizualus slash trail
        Vec3d from = user.getPos().add(0, user.getStandingEyeHeight() * 0.8, 0);
        Vec3d forward = user.getRotationVec(1.0f);

        for (double d = 0; d <= 5.0; d += 0.35) {
            Vec3d p = from.add(forward.multiply(d));
            sw.spawnParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0, 0, 0, 0);
            sw.spawnParticles(ParticleTypes.PORTAL, p.x, p.y, p.z, 1, 0, 0, 0, 0.02);
        }

        sw.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.PLAYERS, 1.0f, 1.15f);

        // Cone AOE priešais (range 5)
        double range = 5.0;
        double dotMin = 0.35; // kuo didesnis, tuo siauresnis kūgis
        Box box = user.getBoundingBox().expand(range);

        for (LivingEntity e : sw.getEntitiesByClass(LivingEntity.class, box,
                e -> e.isAlive() && e != user)) {

            Vec3d to = e.getPos().subtract(user.getPos());
            double dist = to.length();
            if (dist < 0.001 || dist > range) continue;

            double dot = to.normalize().dotProduct(forward);
            if (dot < dotMin) continue;

            // ability dmg (saikingas, bet jaučiasi)
            e.damage(user.getDamageSources().playerAttack(user), 10.0f);
            e.takeKnockback(0.6, -forward.x, -forward.z);
        }

        user.getItemCooldownManager().set(this, ABILITY_CD);
        return TypedActionResult.success(stack);
    }
}