package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Item stack modifications to prevent breaking permanent blocks.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	/**
	 * Modifies the method to capture tree generation any time that bone meal is used.
	 * @param context The item usage context.
	 * @param cir     boilerplate
	 * @param item    The item used.
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/item/Item;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
			value = "INVOKE"
		),
		method = "useOn"
	)
	private void captureTreeGeneration(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local Item item) {
		if (item == Items.BONE_MEAL) {
			Level level = context.getLevel();
			((CaptureTreeGenerationAccessor)level).whiteout$setCaptureTreeGeneration(true);
		}
	}

	/**
	 * Modifies the method to capture tree generation any time that bone meal is used.
	 * @param context The item usage context.
	 * @param cir     boilerplate
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/item/Item;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
			value = "INVOKE_ASSIGN" // there's a suggestion that this can be converted to an expression - doing that mysteriously breaks stuff, so don't
		),
		method = "useOn"
	)
	private void captureTreeGeneration(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		Level level = context.getLevel();
		((CaptureTreeGenerationAccessor)level).whiteout$setCaptureTreeGeneration(false);
	}
}
