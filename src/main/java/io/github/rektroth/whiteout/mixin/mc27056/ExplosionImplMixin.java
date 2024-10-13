/*
 * Patch for MC-27056
 *
 * Authored for CraftBukkit/Spigot by commandblockguy <commandblockguy1@gmail.com> on August 14, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc27056;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(ExplosionImpl.class)
public abstract class ExplosionImplMixin {
    @Final
    @Shadow
    private ServerWorld world;

    /**
     * Modifies the target method to also add the position of the corresponding piston base to the set of block
     * positions if the block in the position just added was a piston head.
     * @param cir        boilerplate
     * @param set        The set of block positions.
     * @param blockPos   The position of the block just added to the set.
     * @param blockState The state of the block just added to the set.
     */
    @Inject(
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"),
        method = "getBlocksToDestroy")
    private void destroyHeadlessPiston(
        CallbackInfoReturnable<List<BlockPos>> cir,
        @Local LocalRef<Set<BlockPos>> set,
        @Local BlockPos blockPos,
        @Local BlockState blockState
    ) {
        if (blockState.getBlock() == Blocks.MOVING_PISTON) {
            BlockEntity extension = this.world.getBlockEntity(blockPos);

            if (extension instanceof PistonBlockEntity blockEntity && blockEntity.isSource()) {
                Direction direction = blockState.get(PistonHeadBlock.FACING);

                Set<BlockPos> newSet = set.get();
                newSet.add(blockPos.offset(direction.getOpposite()));
                set.set(newSet);
            }
        }
    }
}
