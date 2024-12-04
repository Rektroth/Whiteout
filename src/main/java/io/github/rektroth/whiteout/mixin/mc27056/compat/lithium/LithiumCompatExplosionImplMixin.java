/*
 * Patch for MC-27056
 *
 * Authored for CraftBukkit/Spigot by commandblockguy <commandblockguy1@gmail.com> on August 14, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc27056.compat.lithium;

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
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ExplosionImpl.class)
public abstract class LithiumCompatExplosionImplMixin {
    @Final
    @Shadow
    private ServerWorld world;

    @Shadow
    protected abstract List<BlockPos> getBlocksToDestroy();

    /**
     * I imagine this causes a massive slow-down, but the only way I can think to get around the fact that Lithium
     * completely skips the code loop where Paper's fix is injected is to go through the list they make and add the
     * base block counterparts for any piston heads found.
     * Unfortunately, this work-around relies on redirecting the one and only place in the *vanilla* code where
     * the target is called - mods that call the target elsewhere will not receive this patch. I'm not sure how to
     * get around this problem.
     * @return List of blocks to destroy along with any headless pistons found.
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/ExplosionImpl;getBlocksToDestroy()Ljava/util/List;"), method = "explode")
    private List<BlockPos> getBlocksAndHeadlessPistonsToDestroy(ExplosionImpl instance) {
        List<BlockPos> list = this.getBlocksToDestroy();

        for (BlockPos pos : list) {
            BlockState state = this.world.getBlockState(pos);

            if (state.getBlock() == Blocks.MOVING_PISTON) {
                BlockEntity extension = this.world.getBlockEntity(pos);

                if (extension instanceof PistonBlockEntity blockEntity && blockEntity.isSource()) {
                    Direction direction = state.get(PistonHeadBlock.FACING);
                    list.add(pos.offset(direction.getOpposite()));
                }
            }
        }

        return list;
    }
}
