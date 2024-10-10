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
import net.minecraft.util.math.*;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(ExplosionImpl.class)
public abstract class ExplosionImplMixin {
    @Final
    @Shadow
    private ServerWorld world;

    /**
     * Modifies the method to also destroy the corresponding piston base of any piston heads it destroys.
     * @param cir        boilerplate
     * @param set        The set of positions of blocks destroyed.
     * @param blockPos   The position of the block just destroyed.
     * @param blockState The state of the block just destroyed.
     */
    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/explosion/ExplosionBehavior;canDestroyBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;F)Z"),
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
