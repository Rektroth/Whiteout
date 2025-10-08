/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {
	/**
	 * Cancels the method call and return false to prevent retraction if the piston is facing the wrong direction
	 * (meaning the piston was placed before retraction could occur).
	 * @param state The state of the block.
	 * @param world boilerplate
	 * @param pos   boilerplate
	 * @param type  boilerplate
	 * @param data  Piston data? (Not actually sure what this is for.)
	 * @param cir   The callback.
	 */
	@Inject(at = @At("HEAD"), method = "onSyncedBlockEvent", cancellable = true)
	private void cancelIfWrongDirection(
		BlockState state,
		World world,
		BlockPos pos,
		int type,
		int data,
		CallbackInfoReturnable<Boolean> cir
	) {
		Direction direction = state.get(FacingBlock.FACING);
		Direction directionAsQueued = Direction.byIndex(data & 7);

		if (direction != directionAsQueued) {
			cir.setReturnValue(false);
		}
	}

	/**
	 * Redirects the call to `removeBlock` in `onSyncedBlockEvent` to only remove the block if it is not permanent.
	 * This fixes headless pistons destroying permanent blocks.
	 * @param instance  The world the event is in.
	 * @param pos       The position of the block to be moved.
	 * @param move      Whether the block should be moved.
	 * @param direction The direction the piston head is facing.
	 * @return True if successful, false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z",
			value = "INVOKE",
			ordinal = 1
		),
		method = "onSyncedBlockEvent"
	)
	private boolean removeBlockIfNotPermanent(World instance, BlockPos pos, boolean move, @Local Direction direction) {
		if (instance.getBlockState(pos) == Blocks.PISTON_HEAD.getDefaultState().with(FacingBlock.FACING, direction)) {
			return instance.removeBlock(pos, move);
		}

		if (!instance.isClient()) {
			((ServerWorld)instance).getChunkManager().markForUpdate(pos);
		}

		return true;
	}
}
