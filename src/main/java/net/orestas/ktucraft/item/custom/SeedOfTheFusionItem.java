package net.orestas.ktucraft.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.orestas.ktucraft.entity.ModEntities;
import net.minecraft.entity.mob.MobEntity;

public class SeedOfTheFusionItem extends Item {

    public SeedOfTheFusionItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        BlockState clicked = world.getBlockState(pos);
        BlockPos spawnPos = pos.up();

        // "plantinimas" tik ant farmland + viršus turi būti laisvas
        if (!clicked.isOf(Blocks.FARMLAND) || !world.getBlockState(spawnPos).isAir()) {
            return ActionResult.PASS;
        }

        // client grąžinam SUCCESS, kad animacija suveiktų, bet spawninam tik server
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        // --- Spawn Triarch ---
        var triarch = ModEntities.THE_TRIARCH.create(world);
        if (triarch == null) return ActionResult.PASS;

        triarch.refreshPositionAndAngles(
                spawnPos.getX() + 0.5,
                spawnPos.getY(),
                spawnPos.getZ() + 0.5,
                world.random.nextFloat() * 360.0F,
                0.0F
        );

        // jei tavo Triarch yra MobEntity (pas tave jis HostileEntity -> taip)
        if (triarch instanceof MobEntity mob) {
            mob.initialize((ServerWorld) world, world.getLocalDifficulty(spawnPos), SpawnReason.TRIGGERED, null);
        }

        // sunaikina "plantinimo" bloką (farmland -> dirt)
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        world.spawnEntity(triarch);

        return ActionResult.SUCCESS;
    }
}