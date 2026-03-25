package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Portal forcer modifications to prevent breaking permanent blocks.
 */
@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
	@Final
	@Shadow
	private ServerLevel level;

	/**
	 * Cancels the `canHostFrame` method and returns `false` if the portal would replace a permanent block.
	 * @param origin    boilerplate
	 * @param mutable   The state of the block that would be replaced.
	 * @param direction boilerplate
	 * @param offset    boilerplate
	 * @param cir       The callback.
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/core/BlockPos$MutableBlockPos;setWithOffset(Lnet/minecraft/core/Vec3i;III)Lnet/minecraft/core/BlockPos$MutableBlockPos;",
			value="INVOKE_ASSIGN"
		),
		method = "canHostFrame",
		cancellable = true
	)
	private void doNotReplacePermanentBlock(
		BlockPos origin,
		BlockPos.MutableBlockPos mutable,
		Direction direction,
		int offset,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (!BlockUtil.isDestroyable(this.level.getBlockState(mutable).getBlock())) {
			cir.setReturnValue(false);
		}
	}
}
