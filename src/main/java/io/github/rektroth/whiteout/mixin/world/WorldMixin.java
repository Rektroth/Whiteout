/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.world;

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

	@Unique
	public void whiteout$setCaptureTreeGeneration(boolean captureTreeGeneration) {
		this.captureTreeGeneration = captureTreeGeneration;
	};

	@Unique
	public boolean whiteout$getCaptureTreeGeneration() {
		return this.captureTreeGeneration;
	};

	@Inject(
		at = @At("HEAD"),
		method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
		cancellable = true
	)
	private void dontReplacePermanentBlocks(
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
