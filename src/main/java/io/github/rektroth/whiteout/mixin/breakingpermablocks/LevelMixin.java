package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelMixin implements CaptureTreeGenerationAccessor {
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
		method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
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
