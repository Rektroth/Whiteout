/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Set;

@Mixin(ExplosionImpl.class)
public class ExplosionImplMixin {
	/**
	 * Redirects the target method to only add the provided block position if the corresponding block is destroyable.
	 * @param instance   The set of block positions.
	 * @param blockPos   The position of the block to be added to the set.
	 * @param blockState The state of the block to be added to the set.
	 * @return {@code true} if this set did not already contain the specified block and the block is destroyable.
	 */
	@Redirect(
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"),
		method = "getBlocksToDestroy")
	private boolean addIfDestroyable(Set<BlockPos> instance, Object blockPos, @Local BlockState blockState) {
		if (BlockUtil.isDestroyable(blockState.getBlock())) {
			return instance.add((BlockPos)blockPos);
		}

		return false;
	}
}
