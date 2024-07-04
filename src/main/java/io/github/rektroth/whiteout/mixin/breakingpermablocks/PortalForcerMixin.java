/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.dimension.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
	@Final
	@Shadow
	private ServerWorld world;

	/**
	 * Cancels the `isValidPortalPos` method and returns `false` if the portal would replace a permanent block.
	 * @param pos                        boilerplate
	 * @param temp                       The state of the block that would be replaced.
	 * @param portalDirection            boilerplate
	 * @param distanceOrthogonalToPortal boilerplate
	 * @param cir                        The callback.
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/util/math/BlockPos$Mutable;set(Lnet/minecraft/util/math/Vec3i;III)Lnet/minecraft/util/math/BlockPos$Mutable;",
			value="INVOKE_ASSIGN"
		),
		method = "isValidPortalPos",
		cancellable = true
	)
	private void doesntReplacePermaBlock(
		BlockPos pos,
		BlockPos.Mutable temp,
		Direction portalDirection,
		int distanceOrthogonalToPortal,
		CallbackInfoReturnable<Boolean> cir
	) {
		if (!BlockUtil.isDestroyable(world.getBlockState(temp).getBlock())) {
			cir.setReturnValue(false);
		}
	}
}
