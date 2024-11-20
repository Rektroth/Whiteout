/*
 * Patch for MC-27056
 *
 * Authored for CraftBukkit/Spigot by commandblockguy <commandblockguy1@gmail.com> on August 14, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc27056.compat.lithium;

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
public abstract class LithiumCompatExplosionImplMixin {
    @Final
    @Shadow
    private ServerWorld world;

    /**
     * I imagine this causes a massive slow-down, but the only way I can think to get around the fact that Lithium
     * completely skips the code loop where Paper's fix is injected is to go through the list they make and add the
     * base block counterparts for any piston heads found.
     * @param cir boilerplate
     * @param set The set of block positions.
     */
    @Inject(at = @At("RETURN"), method = "getBlocksToDestroy")
    private void destroyHeadlessPistons(CallbackInfoReturnable<List<BlockPos>> cir, @Local LocalRef<Set<BlockPos>> set) {
        Set<BlockPos> newSet = set.get();

        for (BlockPos pos : newSet) {
            BlockState state = this.world.getBlockState(pos);

            if (state.getBlock() == Blocks.MOVING_PISTON) {
                BlockEntity extension = this.world.getBlockEntity(pos);

                if (extension instanceof PistonBlockEntity blockEntity && blockEntity.isSource()) {
                    Direction direction = state.get(PistonHeadBlock.FACING);
                    newSet.add(pos.offset(direction.getOpposite()));
                }
            }
        }

        set.set(newSet);
    }
}
