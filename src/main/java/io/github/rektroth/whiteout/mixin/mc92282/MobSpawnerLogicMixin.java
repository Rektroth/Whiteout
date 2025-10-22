package io.github.rektroth.whiteout.mixin.mc92282;

import net.minecraft.block.spawner.MobSpawnerEntry;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.EntityType;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin {
    @Shadow
    private Pool<MobSpawnerEntry> spawnPotentials;

    /**
     * Clears the list of spawn potentials to be empty every time the entity ID is set.
     * @param type   boilerplate
     * @param world  boilerplate
     * @param random boilerplate
     * @param pos    boilerplate
     * @param ci     boilerplate
     */
    @Inject(at = @At("TAIL"), method = "setEntityId")
    private void clearSpawnPotentials(
        EntityType<?> type,
        World world,
        Random random,
        BlockPos pos,
        CallbackInfo ci
    ) {
        this.spawnPotentials = Pool.of();
    }
}
