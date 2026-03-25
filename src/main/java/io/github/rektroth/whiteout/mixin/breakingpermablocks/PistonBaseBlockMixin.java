package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Piston base block modifications to prevent breaking permanent blocks.
 */
@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin {
	/**
	 * Cancels the method call and return false to prevent retraction if the piston is facing the wrong direction
	 * (meaning the piston was placed before retraction could occur).
	 * @param state The state of the block.
	 * @param level boilerplate
	 * @param pos   boilerplate
	 * @param b0    boilerplate
	 * @param b1    3D data? (not actually sure)
	 * @param cir   The callback.
	 */
	@Inject(at = @At("HEAD"), method = "triggerEvent", cancellable = true)
	private void cancelIfWrongDirection(
		BlockState state,
		Level level,
		BlockPos pos,
		int b0,
		int b1,
		CallbackInfoReturnable<Boolean> cir
	) {
		Direction direction = state.getValue(DirectionalBlock.FACING);
		Direction directionAsQueued = Direction.from3DDataValue(b1 & 7);

		if (direction != directionAsQueued) {
			cir.setReturnValue(false);
		}
	}

	/**
	 * Redirects the call to `removeBlock` in `onSyncedBlockEvent` to only remove the block if it is not permanent.
	 * This fixes headless pistons destroying permanent blocks.
	 * @param instance      The world the event is in.
	 * @param pos           The position of the block.
	 * @param movedByPiston Whether the block is to be moved by the piston.
	 * @param direction     The direction the piston head is facing.
	 * @return True if successful, false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
			value = "INVOKE",
			ordinal = 1
		),
		method = "triggerEvent"
	)
	private boolean removeBlockIfNotPermanent(Level instance, BlockPos pos, boolean movedByPiston, @Local Direction direction) {
		if (instance.getBlockState(pos) == Blocks.PISTON_HEAD.defaultBlockState().setValue(DirectionalBlock.FACING, direction)) {
			return instance.removeBlock(pos, movedByPiston);
		}

		if (!instance.isClientSide()) {
			((ServerLevel)instance).getChunkSource().blockChanged(pos);
		}

		return true;
	}
}
