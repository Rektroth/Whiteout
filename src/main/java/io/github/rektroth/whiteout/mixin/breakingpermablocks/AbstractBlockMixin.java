/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
	@Shadow
	public abstract Item asItem();

	/**
	 * Redirects the `isAir` check to *add* a check that the block is destroyable as well.
	 * @param instance The state of the block.
	 * @return Whether the block is not both not air and destroyable.
	 */
	@Redirect(
		at = @At(target = "Lnet/minecraft/block/BlockState;isAir()Z", value = "INVOKE"),
		method = "onExploded"
	)
	private boolean isNotAirAndIsDestroyable(BlockState instance) {
		return !(!instance.isAir() && BlockUtil.isDestroyable(instance.getBlock()));
	}

	/**
	 * Modifies the `canReplace` method to return false if the block is permanent.
	 * @param state   The state of the block.
	 * @param context The item being placed.
	 * @param cir     The callback.
	 */
	@Inject(at = @At("RETURN"), method = "canReplace", cancellable = true)
	private void canOnlyBeReplacedIfDestroyable(
		BlockState state,
		ItemPlacementContext context,
		CallbackInfoReturnable<Boolean> cir
	) {
		cir.setReturnValue(state.isReplaceable()
			&& (context.getStack().isEmpty() || !context.getStack().isOf(this.asItem()))
			&& (BlockUtil.isDestroyable(state.getBlock())
				|| (context.getPlayer() != null && context.getPlayer().getAbilities().creativeMode)));
	}

	@Mixin(AbstractBlock.AbstractBlockState.class)
	public abstract static class AbstractBlockStateMixin {
		@Final
		@Shadow
		private PistonBehavior pistonBehavior;

		@Shadow
		public abstract Block getBlock();

		/**
		 * Modifies the `getPistonBehavior` method to return BLOCK behavior if the block is permanent.
		 * @param cir The callback.
		 */
		@Inject(at = @At("RETURN"), method = "getPistonBehavior", cancellable = true)
		private void blockIfPermanent(CallbackInfoReturnable<PistonBehavior> cir) {
			cir.setReturnValue(!BlockUtil.isDestroyable(this.getBlock()) ? PistonBehavior.BLOCK : this.pistonBehavior);
		}
	}
}
