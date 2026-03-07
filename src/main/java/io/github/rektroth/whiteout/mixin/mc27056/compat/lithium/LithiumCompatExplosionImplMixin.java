package io.github.rektroth.whiteout.mixin.mc27056.compat.lithium;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ServerExplosion.class)
public abstract class LithiumCompatExplosionImplMixin {
    @Final
    @Shadow
    private ServerLevel level;

    @Shadow
    protected abstract List<BlockPos> calculateExplodedPositions();

    /**
     * I imagine this causes a massive slow-down, but the only way I can think to get around the fact that Lithium
     * completely skips the code loop where Paper's fix is injected is to go through the list they make and add the
     * base block counterparts for any piston heads found.
     * Unfortunately, this work-around relies on redirecting the one and only place in the *vanilla* code where
     * the target is called - mods that call the target elsewhere will not receive this patch. I'm not sure how to
     * get around this problem.
     * @return List of blocks to destroy along with any headless pistons found.
     */
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerExplosion;calculateExplodedPositions()Ljava/util/List;"), method = "explode")
    private List<BlockPos> getBlocksAndHeadlessPistonsToDestroy(ServerExplosion instance) {
        List<BlockPos> list = this.calculateExplodedPositions();

        for (BlockPos pos : list) {
            BlockState state = this.level.getBlockState(pos);

            if (state.getBlock() == Blocks.MOVING_PISTON) {
                BlockEntity extension = this.level.getBlockEntity(pos);

                if (extension instanceof PistonMovingBlockEntity blockEntity && blockEntity.isSourcePiston()) {
                    Direction direction = state.getValue(PistonHeadBlock.FACING);
                    list.add(pos.relative(direction.getOpposite()));
                }
            }
        }

        return list;
    }
}
