/*
 * Patch for MC-27056
 *
 * Authored for CraftBukkit/Spigot by commandblockguy <commandblockguy1@gmail.com> on August 14, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc27056;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Final
    @Shadow
    private World world;

    /**
     * Modifies the method to also destroy the corresponding piston base of any piston heads it destroys.
     * @param ci         boilerplate
     * @param set        The set of positions of blocks destroyed.
     * @param blockPos   The position of the block just destroyed.
     * @param blockState The state of the block just destroyed.
     */
    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/explosion/ExplosionBehavior;canDestroyBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;F)Z"),
        method = "collectBlocksAndDamageEntities")
    private void noHeadlessPiston(
        CallbackInfo ci,
        @Local Set<BlockPos> set,
        @Local BlockPos blockPos,
        @Local BlockState blockState
    ) {
        if (blockState.getBlock() == Blocks.MOVING_PISTON) {
            BlockEntity extension = this.world.getBlockEntity(blockPos);

            if (extension instanceof PistonBlockEntity blockEntity && blockEntity.isSource()) {
                Direction direction = blockState.get(PistonHeadBlock.FACING);
                set.add(blockPos.offset(direction.getOpposite()));
            }
        }
    }
}
