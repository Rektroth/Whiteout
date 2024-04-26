/*
 * Patch for MC-27056
 *
 * Authored for CraftBukkit/Spigot by commandblockguy <commandblockguy1@gmail.com> on August 14, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.world.explosion;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Final
    @Shadow
    private World world;

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/explosion/ExplosionBehavior;canDestroyBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;F)Z"),
        locals = LocalCapture.CAPTURE_FAILHARD,
        method = "collectBlocksAndDamageEntities")
    private void noHeadlessPiston(
        CallbackInfo ci,
        Set<BlockPos> set,
        int i,
        int j,
        int k,
        int l,
        double d,
        double e,
        double f,
        double g,
        float h,
        double m,
        double n,
        double o,
        float p,
        BlockPos blockPos,
        BlockState blockState
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
