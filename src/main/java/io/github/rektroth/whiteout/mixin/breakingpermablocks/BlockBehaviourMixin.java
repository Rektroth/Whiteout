package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.util.BlockUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Block behavior modifications to prevent breaking permanent blocks.
 */
@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
	@Shadow
	public abstract Item asItem();

	/**
	 * Redirects the `isAir` check to *add* a check that the block is destroyable as well.
	 * @param instance The state of the block.
	 * @return Whether the block is not both not air and destroyable.
	 */
	@Redirect(
		at = @At(target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z", value = "INVOKE"),
		method = "onExplosionHit"
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
	@Inject(
		at = @At("RETURN"),
		method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z",
		cancellable = true
	)
	private void canOnlyBeReplacedIfDestroyable(
		BlockState state,
		BlockPlaceContext context,
		CallbackInfoReturnable<Boolean> cir
	) {
		cir.setReturnValue(state.canBeReplaced()
			&& (context.getItemInHand().isEmpty() || !context.getItemInHand().is(this.asItem()))
			&& (BlockUtil.isDestroyable(state.getBlock())
				|| (context.getPlayer() != null && context.getPlayer().getAbilities().instabuild)));
	}

	/**
	 * Block state base modifications to prevent breaking permanent blocks.
	 */
	@Mixin(BlockBehaviour.BlockStateBase.class)
	public abstract static class BlockStateBaseMixin {
		@Final
		@Shadow
		private PushReaction pushReaction;

		@Shadow
		public abstract Block getBlock();

		/**
		 * Modifies the `getPistonBehavior` method to return BLOCK behavior if the block is permanent.
		 * @param cir The callback.
		 */
		@Inject(at = @At("RETURN"), method = "getPistonPushReaction", cancellable = true)
		private void blockIfPermanent(CallbackInfoReturnable<PushReaction> cir) {
			cir.setReturnValue(!BlockUtil.isDestroyable(this.getBlock()) ? PushReaction.BLOCK : this.pushReaction);
		}
	}
}
