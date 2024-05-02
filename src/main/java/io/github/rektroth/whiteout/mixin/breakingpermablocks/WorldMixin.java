/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin implements CaptureTreeGenerationAccessor {
	@Shadow
	public abstract BlockState getBlockState(BlockPos pos);

	@Unique
	public boolean captureTreeGeneration = false;

	/**
	 * Sets the value of the `captureTreeGeneration` property.
	 * @param captureTreeGeneration Whether tree generation was captured.
	 */
	@Unique
	public void whiteout$setCaptureTreeGeneration(boolean captureTreeGeneration) {
		this.captureTreeGeneration = captureTreeGeneration;
	};

	/**
	 * Gets the `captureTreeGeneration` property.
	 * @return The value of `captureTreeGeneration`.
	 */
	@Unique
	public boolean whiteout$getCaptureTreeGeneration() {
		return this.captureTreeGeneration;
	};

	/**
	 * Cancels the method and returns false if tree generation has been capture and the block to be replace
	 * is permanent.
	 * @param pos            The position of the block to be set.
	 * @param state          boilerplate
	 * @param flags          boilerplate
	 * @param maxUpdateDepth boilerplate
	 * @param cir            The callback.
	 */
	@Inject(
		at = @At("HEAD"),
		method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
		cancellable = true
	)
	private void checkReplacePermaBlock(
		BlockPos pos,
		BlockState state,
		int flags,
		int maxUpdateDepth,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (this.captureTreeGeneration) {
			BlockState type = getBlockState(pos);

			if (!BlockUtil.isDestroyable(type.getBlock())) {
				cir.setReturnValue(false);
			}
		}
	}
}
