package io.github.rektroth.whiteout.mixin.mc27056;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

/**
 * Server explosion modifications for MC-27056 patch.
 */
@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin {
    @Final
    @Shadow
    private ServerLevel level;

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
        method = "calculateExplodedPositions")
    private void destroyHeadlessPiston(
        CallbackInfoReturnable<List<BlockPos>> cir,
        @Local LocalRef<Set<BlockPos>> set,
        @Local BlockPos blockPos,
        @Local BlockState blockState
    ) {
        if (blockState.getBlock() == Blocks.MOVING_PISTON) {
            BlockEntity extension = this.level.getBlockEntity(blockPos);

            if (extension instanceof PistonMovingBlockEntity blockEntity && blockEntity.isSourcePiston()) {
                Direction direction = blockState.getValue(PistonHeadBlock.FACING);

                Set<BlockPos> newSet = set.get();
                newSet.add(blockPos.relative(direction.getOpposite()));
                set.set(newSet);
            }
        }
    }
}
