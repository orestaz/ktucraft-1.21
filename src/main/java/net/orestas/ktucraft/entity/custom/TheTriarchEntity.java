package net.orestas.ktucraft.entity.custom;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.orestas.ktucraft.item.ModItems;
import net.orestas.ktucraft.network.packet.ModPackets;
import net.orestas.ktucraft.particle.ModParticles;
import org.jetbrains.annotations.Nullable;
import net.orestas.ktucraft.entity.custom.TheTriarchStarEntity;

import java.util.UUID;

public class TheTriarchEntity extends HostileEntity {

    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(
            Text.literal("The Triarch").formatted(Formatting.LIGHT_PURPLE),
            BossBar.Color.RED,
            BossBar.Style.PROGRESS
    ).setDarkenSky(true);
    private static final Identifier ADV_KILL_TRIARCH =
            Identifier.of("ktucraft", "kill_triarch");

    @Nullable
    private UUID fightPlayerUuid;
    private boolean wasRage = false;

    // ====== Animacijos ======
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackLeftAnimationState = new AnimationState();
    public final AnimationState attackRightAnimationState = new AnimationState();
    public final AnimationState attackMidAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;

    // ====== Attack state machine ======
    private static final int ATTACK_NONE  = 0;
    private static final int ATTACK_LEFT  = 1; // Dragon fireball
    private static final int ATTACK_RIGHT = 2; // Wither skull
    private static final int ATTACK_MID   = 3; // Sonic boom

    private int lastExtraLeftTrigger = 0;
    private int lastExtraRightTrigger = 0;

    private int extraLeftAnimStopAt = -1;
    private int extraRightAnimStopAt = -1;

    private static final TrackedData<Integer> ATTACK_STATE =
            DataTracker.registerData(TheTriarchEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Integer> EXTRA_LEFT_TRIGGER =
            DataTracker.registerData(TheTriarchEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Integer> EXTRA_RIGHT_TRIGGER =
            DataTracker.registerData(TheTriarchEntity.class, TrackedDataHandlerRegistry.INTEGER);

    // <10% HP melee chase speed boost
    private static final Identifier MELEE_ENRAGE_SPEED_ID =
            Identifier.of("ktucraft", "triarch_melee_enrage_speed");

    private static final EntityAttributeModifier MELEE_ENRAGE_SPEED_MOD =
            new EntityAttributeModifier(
                    MELEE_ENRAGE_SPEED_ID,
                    1.0, // +100% => 2x total
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );

    private static final Identifier RAGE_DAMAGE_ID =
            Identifier.of("ktucraft", "triarch_rage_damage");

    private static final EntityAttributeModifier RAGE_DAMAGE_MOD =
            new EntityAttributeModifier(
                    RAGE_DAMAGE_ID,
                    5.0, // +5 damage: 12 -> 17
                    EntityAttributeModifier.Operation.ADD_VALUE
            );

    private int attackTicks = 0;
    private int attackCooldown = 0;
    private int extraLeftCooldown = 0;
    private int extraRightCooldown = 0;
    // client-side, kad animacijos neužsistartu kas tick
    private int lastClientAttackState = ATTACK_NONE;

    public TheTriarchEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.setNoGravity(true);
        this.setHealth(this.getMaxHealth());
        this.experiencePoints = 20000;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ATTACK_STATE, ATTACK_NONE);
        builder.add(EXTRA_LEFT_TRIGGER, 0);
        builder.add(EXTRA_RIGHT_TRIGGER, 0);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);

        if (!this.getWorld().isClient() && this.fightPlayerUuid == null && target instanceof ServerPlayerEntity sp) {
            this.fightPlayerUuid = sp.getUuid();
        }
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, net.minecraft.entity.damage.DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // 1) Jei damage “kaltininkas” yra pats Triarch (pvz. sonic boom ir pan.)
        Entity attacker = source.getAttacker();
        if (attacker == this) {
            return false;
        }

        // 2) Jei damage tiesioginis šaltinis yra projektilis, kurį jis paleido
        Entity direct = source.getSource();
        if (direct instanceof ProjectileEntity proj && proj.getOwner() == this) {
            return false;
        }

        // 3) Jei tai dragon breath tipo AreaEffectCloud, kurį jis sukūrė
        if (direct instanceof AreaEffectCloudEntity cloud && cloud.getOwner() == this) {
            return false;
        }

        return super.damage(source, amount);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AttackGoal(this));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(5, new FlyGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(0, new RevengeGoal(this));
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);

        ModPackets.sendStart(player, this.getId());
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);

        ModPackets.sendStop(player);
    }

    @Override
    public void onDeath(net.minecraft.entity.damage.DamageSource source) {
        super.onDeath(source);
        if (!this.getWorld().isClient()) {
            for (var p : this.bossBar.getPlayers()) {
                ModPackets.sendStop(p);
            }
            Entity attacker = source.getAttacker();
            if (attacker instanceof ServerPlayerEntity sp) {
                grantAdv(sp, ADV_KILL_TRIARCH);
            }
            this.dropStack(new ItemStack(ModItems.TRINITY_SWORD));
        }
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1000.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.25F)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 1.5F)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 80.0)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0F)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.5F);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 30;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    private void playBossSound(SoundEvent event, float volume, float pitch) {
        if (this.getWorld().isClient()) return;
        this.getWorld().playSound(
                null, // null = visiems girdisi
                this.getX(), this.getY(), this.getZ(),
                event,
                SoundCategory.HOSTILE,
                volume,
                pitch
        );
    }

    private float randPitch(float base, float spread) {
        return base + (this.random.nextFloat() * 2f - 1f) * spread;
    }

    private void maintainFlightAltitude() {
        // tik serveris valdo fizika
        if (this.getWorld().isClient()) return;

        this.setNoGravity(true);
        this.fallDistance = 0.0F;

        int x = MathHelper.floor(this.getX());
        int z = MathHelper.floor(this.getZ());

        int topY = this.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
        double minY = topY + 1.0;   // >= 1 blokas virš žemės
        double maxY = topY + 15.0;  // ne aukščiau nei 15

        LivingEntity target = this.getTarget();

        // norimas aukštis: seka target, bet clamp’inam į min/max
        double desiredY = (target != null)
                ? MathHelper.clamp(target.getY() + 2.0, minY, maxY)
                : MathHelper.clamp(minY + 4.0, minY, maxY);

        double dy = desiredY - this.getY();

        // švelnus vertikalus koregavimas (be šokinėjimo)
        double vy = MathHelper.clamp(dy * 0.20, -0.6, 0.6);

        Vec3d v = this.getVelocity();
        double newY = MathHelper.lerp(0.35, v.y, vy);
        this.setVelocity(v.x, newY, v.z);

        // jei vis tiek “paliečia” ground — pastumiam į viršų
        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().x, 0.35, this.getVelocity().z);
        }
    }

    private void applyAerialChase() {
        if (this.getWorld().isClient()) return;

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) return;

        float hp = this.getHealth() / this.getMaxHealth();

        // bazinis chase (pasituninsi vėliau)
        double baseChase = 0.22;         // ~4.4 blokai/sek
        double enragedChase = baseChase * 2.0; // <10% => 2x

        boolean enraged = hp < 0.10f;

        double chase = enraged ? enragedChase : baseChase;

        Vec3d to = new Vec3d(target.getX() - this.getX(), 0.0, target.getZ() - this.getZ());
        if (to.lengthSquared() < 0.0001) return;

        Vec3d desired = to.normalize().multiply(chase);

        Vec3d v = this.getVelocity();
        double newX = MathHelper.lerp(0.25, v.x, desired.x);
        double newZ = MathHelper.lerp(0.25, v.z, desired.z);

        // paliekam Y iš maintainFlightAltitude()
        this.setVelocity(newX, v.y, newZ);
    }

    private void updateMeleeEnrageSpeed() {
        if (this.getWorld().isClient()) return;

        EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.GENERIC_FLYING_SPEED);
        if (speed == null) return;

        LivingEntity target = this.getTarget();
        float hp = this.getHealth() / this.getMaxHealth();
        int state = this.dataTracker.get(ATTACK_STATE);

        boolean shouldBoost = hp < 0.10f && target != null && target.isAlive() && this.squaredDistanceTo(target) > 9.0;

        if (shouldBoost) {
            if (!speed.hasModifier(MELEE_ENRAGE_SPEED_ID)) {
                speed.addTemporaryModifier(MELEE_ENRAGE_SPEED_MOD);
            }
        } else {
            if (speed.hasModifier(MELEE_ENRAGE_SPEED_ID)) {
                speed.removeModifier(MELEE_ENRAGE_SPEED_ID);
            }
        }
    }

    private void updateRageDamage() {
        if (this.getWorld().isClient()) return;

        EntityAttributeInstance dmg = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (dmg == null) return;

        boolean enraged = (this.getHealth() / this.getMaxHealth()) < 0.10f;

        if (enraged) {
            if (!dmg.hasModifier(RAGE_DAMAGE_ID)) {
                dmg.addTemporaryModifier(RAGE_DAMAGE_MOD);
            }
        } else {
            if (dmg.hasModifier(RAGE_DAMAGE_ID)) {
                dmg.removeModifier(RAGE_DAMAGE_ID);
            }
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        // --- tik serveris sprendžia atakas ---
        if (this.getWorld().isClient()) return;

        boolean rage = (this.getHealth() / this.getMaxHealth()) < 0.10f;
        if (rage && !wasRage) {
            playBossSound(SoundEvents.ENTITY_WARDEN_ROAR, 1.6f, randPitch(0.95f, 0.05f));
        }
        wasRage = rage;

        if (this.fightPlayerUuid != null && this.getWorld() instanceof ServerWorld sw) {
            ServerPlayerEntity sp = sw.getServer().getPlayerManager().getPlayer(this.fightPlayerUuid);

            // jei playeris mirė (ar dingo iš serverio) – despawn boss
            if (sp == null || !sp.isAlive()) {
                playBossSound(SoundEvents.ENTITY_ENDER_DRAGON_DEATH, 1.6f, randPitch(0.95f, 0.05f));
                Text msg = Text.literal("The Triarch fades as its challenger falls..").formatted(Formatting.LIGHT_PURPLE);
                sw.getServer().getPlayerManager().broadcast(msg, false);
                this.discard();
                return;
            }
        }

        updateMeleeEnrageSpeed();
        updateRageDamage();

        if (attackCooldown > 0) attackCooldown--;
        if (extraLeftCooldown > 0) extraLeftCooldown--;
        if (extraRightCooldown > 0) extraRightCooldown--;

        int state = this.dataTracker.get(ATTACK_STATE);

        // jei vyksta ataka – tęsia timeline
        if (state != ATTACK_NONE) {
            attackTicks++;
            tickAttack(state);
            return;
        }

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) return;
        if (attackCooldown > 0) return;

        float hp = this.getHealth() / this.getMaxHealth();

        if (hp > 0.75f) {
            startAttack(ATTACK_LEFT);
        } else if (hp > 0.25f) {
            startAttack(ATTACK_RIGHT);
        } else {
            startAttack(ATTACK_MID);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.extinguish();

        // server: flight + chase
        if (!this.getWorld().isClient()) {
            maintainFlightAltitude();
            applyAerialChase();
        }

        // client: animacijos
        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
            this.tickClientAttackAnimations();
            this.tickClientExtraHeadAnimations();
        }
    }

    // =========================
    //  Attack state machine
    // =========================

    private void startAttack(int state) {
        this.dataTracker.set(ATTACK_STATE, state);
        this.attackTicks = 0;
    }

    private void endAttack(int cooldownTicks) {
        this.dataTracker.set(ATTACK_STATE, ATTACK_NONE);
        this.attackTicks = 0;
        this.attackCooldown = cooldownTicks;
    }

    private void tickAttack(int state) {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            endAttack(20);
            return;
        }

        // visada žiūri į target
        this.getLookControl().lookAt(target, 30.0F, 30.0F);

        switch (state) {
            case ATTACK_LEFT -> {
                // impact tick (priderink prie animacijos)
                if (attackTicks == 10) shootDragonFireballLeft(target);
                if (attackTicks >= 25) endAttack(50);
            }
            case ATTACK_RIGHT -> {
                if (attackTicks == 10) shootWitherSkullRight(target);
                if (attackTicks >= 25) endAttack(35);
            }
            case ATTACK_MID -> {
                if (attackTicks == 12) sonicBoomMid(target);

                // <25%: kartais “prideda” kairę / dešinę galvas kartu su MID
                float hp = this.getHealth() / this.getMaxHealth();
                boolean rage = hp < 0.10f;

                // tikrinam ne kas tick, o periodiškai (kad ne-spamintų)
                if (attackTicks >= 6 && attackTicks % 10 == 0) {
                    float leftChance  = rage ? 0.30f : 0.15f;
                    float rightChance = rage ? 0.35f : 0.18f;

                    if (extraLeftCooldown == 0 && this.random.nextFloat() < leftChance) {
                        shootDragonFireballLeft(target);
                        extraLeftCooldown = rage ? 70 : 100; // 3.5s arba 5s
                        this.dataTracker.set(EXTRA_LEFT_TRIGGER, this.age); // trigger animacijai
                    }

                    if (extraRightCooldown == 0 && this.random.nextFloat() < rightChance) {
                        shootWitherSkullRight(target);
                        extraRightCooldown = rage ? 60 : 90;
                        this.dataTracker.set(EXTRA_RIGHT_TRIGGER, this.age);
                    }
                }

                // <10%: lazeris + melee (jei arti)

                if (hp < 0.10f && attackTicks >= 14 && attackTicks % 10 == 0) {
                    if (this.squaredDistanceTo(target) <= 9.0) { // 3 blokai
                        this.tryAttack(target);
                    }
                }

                if (attackTicks >= 30) endAttack(40);
            }
        }
    }

    // =========================
    //  Animacijos (client)
    // =========================

    private void tickClientAttackAnimations() {
        int state = this.dataTracker.get(ATTACK_STATE); // tavo tracked int (LEFT/RIGHT/MID/NONE)

        if (state == lastClientAttackState) return;

        // optional: sustabdom viską prieš startą
        attackLeftAnimationState.stop();
        attackRightAnimationState.stop();
        attackMidAnimationState.stop();

        if (state == ATTACK_LEFT)  attackLeftAnimationState.start(this.age);
        if (state == ATTACK_RIGHT) attackRightAnimationState.start(this.age);
        if (state == ATTACK_MID)   attackMidAnimationState.start(this.age);

        lastClientAttackState = state;
    }

    private void tickClientExtraHeadAnimations() {
        int l = this.dataTracker.get(EXTRA_LEFT_TRIGGER);
        int r = this.dataTracker.get(EXTRA_RIGHT_TRIGGER);

        if (l != 0 && l != lastExtraLeftTrigger) {
            attackLeftAnimationState.start(this.age);
            extraLeftAnimStopAt = this.age + 25; // ~1.25s (25 tick)
            lastExtraLeftTrigger = l;
        }
        if (r != 0 && r != lastExtraRightTrigger) {
            attackRightAnimationState.start(this.age);
            extraRightAnimStopAt = this.age + 25;
            lastExtraRightTrigger = r;
        }

        // auto-stop, kad neužstrigtų paskutiniam frame
        if (extraLeftAnimStopAt != -1 && this.age >= extraLeftAnimStopAt && this.dataTracker.get(ATTACK_STATE) != ATTACK_LEFT) {
            attackLeftAnimationState.stop();
            extraLeftAnimStopAt = -1;
        }
        if (extraRightAnimStopAt != -1 && this.age >= extraRightAnimStopAt && this.dataTracker.get(ATTACK_STATE) != ATTACK_RIGHT) {
            attackRightAnimationState.stop();
            extraRightAnimStopAt = -1;
        }
    }

    // =========================
    //  Head positions
    // =========================

    private Vec3d headCenterPos() {
        return this.getPos().add(0.0, this.getStandingEyeHeight(), 0.0);
    }

    private Vec3d headLeftPos() {
        float yawRad = this.getYaw() * MathHelper.RADIANS_PER_DEGREE;
        Vec3d right = new Vec3d(-MathHelper.sin(yawRad), 0, MathHelper.cos(yawRad));
        return headCenterPos().add(right.multiply(-0.9)).add(0, 0.25, 0);
    }

    private Vec3d headRightPos() {
        float yawRad = this.getYaw() * MathHelper.RADIANS_PER_DEGREE;
        Vec3d right = new Vec3d(-MathHelper.sin(yawRad), 0, MathHelper.cos(yawRad));
        return headCenterPos().add(right.multiply(0.9)).add(0, 0.25, 0);
    }

    private Vec3d headMidPos() {
        Vec3d forward = this.getRotationVec(1.0F);
        return headCenterPos().add(forward.multiply(0.35)).add(0, 0.35, 0);
    }

    // =========================
    //  Attacks
    // =========================

    // LEFT head: dragon fireball
    private void shootDragonFireballLeft(LivingEntity target) {
        Vec3d from = headLeftPos();
        Vec3d dir = target.getEyePos().subtract(from).normalize();

        DragonFireballEntity fb = new DragonFireballEntity(this.getWorld(), this, dir); // 1.21 constructor :contentReference[oaicite:1]{index=1}
        fb.refreshPositionAndAngles(from.x, from.y, from.z, this.getYaw(), this.getPitch());

        // jei nori “greitesnio” skrydžio – reguliuok čia speed
        fb.setVelocity(dir.x, dir.y, dir.z, 2.0f, 0.0f); //buvo 1.6

        playBossSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.2f, randPitch(0.8f, 0.08f));
        playBossSound(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, randPitch(1.0f, 0.05f));
        this.getWorld().spawnEntity(fb);
    }

    // RIGHT head: wither skull
    private void shootWitherSkullRight(LivingEntity target) {
        Vec3d from = headRightPos();
        Vec3d dir = target.getEyePos().subtract(from).normalize();

        TheTriarchStarEntity star = TheTriarchStarEntity.create(this.getWorld(), this, dir);
        star.refreshPositionAndAngles(from.x, from.y, from.z, this.getYaw(), this.getPitch());
        star.setCharged(true);
        float hp = this.getHealth() / this.getMaxHealth();
        float pwr = (hp < 0.10f) ? 1f : 2.5f; // <-- rage mažiau
        star.setVelocity(dir.x, dir.y, dir.z, pwr, 0.0f);

        playBossSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0f, randPitch(0.9f, 0.08f));
        this.getWorld().spawnEntity(star);
    }

    private void sonicBoomMid(LivingEntity target) {
        if (!(this.getWorld() instanceof ServerWorld sw)) return;

        float hp = this.getHealth() / this.getMaxHealth();
        float damage = (hp < 0.10f) ? 3.0F : 6.0F; // <-- rage mažiau

        // ... particles/beam ...



        Vec3d from = headMidPos();
        Vec3d to = target.getEyePos();
        Vec3d dir = to.subtract(from);
        double dist = dir.length();
        if (dist < 0.001) return;

        dir = dir.normalize();

        // vizualas
        double step = 0.5;
        for (double d = 0; d < Math.min(dist, 30.0); d += step) {
            Vec3d p = from.add(dir.multiply(d));
            sw.spawnParticles(ModParticles.THE_TRIARCH_SONIC_BOOM, p.x, p.y, p.z, 1, 0, 0, 0, 0);
        }
        playBossSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 2.5f, 0.95f);
        // screen shake visiems, kurie mato bossbar (t.y. boss fight)
        float shake = 6.0f;
        for (ServerPlayerEntity p : this.bossBar.getPlayers()) {
            ModPackets.sendShake(p, this.getX(), this.getY(), this.getZ(), shake);
        }
        DamageSource src = this.getDamageSources().sonicBoom(this);
        target.damage(src, damage);

        // knockback
        target.takeKnockback(1.8, -dir.x, -dir.z);

    }

    private static void grantAdv(ServerPlayerEntity player, Identifier id) {
        AdvancementEntry entry = player.getServer().getAdvancementLoader().get(id);
        if (entry == null) return;

        AdvancementProgress progress = player.getAdvancementTracker().getProgress(entry);
        if (progress.isDone()) return;

        for (String criterion : progress.getUnobtainedCriteria()) {
            player.getAdvancementTracker().grantCriterion(entry, criterion);
        }
    }
}