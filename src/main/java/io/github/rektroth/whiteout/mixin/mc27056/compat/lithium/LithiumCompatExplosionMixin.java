/*
 * Patch for MC-27056
 *
 * Authored for CraftBukkit/Spigot by commandblockguy <commandblockguy1@gmail.com> on August 14, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 24, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc27056.compat.lithium;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

@Mixin(Explosion.class)
public abstract class LithiumCompatExplosionMixin {
    @Final
    @Shadow
    private World world;

    @Final
    @Shadow
    private ObjectArrayList<BlockPos> affectedBlocks;

    /**
     * I imagine this causes a massive slow-down, but the only way I can think to get around the fact that Lithium
     * completely skips the code loop where Paper's fix is injected is to go through the list they make
     * and add the base block counterparts for any piston heads found.
     * @param ci boilerplate
     */
    @Inject(
        method = "collectBlocksAndDamageEntities()V",
        at = @At(
            remap = false,
            target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;addAll(Ljava/util/Collection;)Z",
            value = "INVOKE_ASSIGN"
        )
    )
    private void removeHeadlessPistons(CallbackInfo ci) {
        for (int i = 0; i < this.affectedBlocks.size(); i++) {
            BlockPos pos = this.affectedBlocks.get(i);
            BlockState state = this.world.getBlockState(pos);

            if (state.getBlock() == Blocks.MOVING_PISTON) {
                BlockEntity extension = this.world.getBlockEntity(pos);

                if (extension instanceof PistonBlockEntity blockEntity && blockEntity.isSource()) {
                    Direction direction = state.get(PistonHeadBlock.FACING);
                    this.affectedBlocks.add(pos.offset(direction.getOpposite()));
                }
            }
        }
    }
}
