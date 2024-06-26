/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
			target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
			value = "INVOKE"
		),
		method = "useOnBlock"
	)
	private void captureTreeGeneration(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, @Local Item item) {
		if (item == Items.BONE_MEAL) {
			World world = context.getWorld();
			((CaptureTreeGenerationAccessor)world).whiteout$setCaptureTreeGeneration(true);
		}
	}

	/**
	 * Modifies the method to capture tree generation any time that bone meal is used.
	 * @param context The item usage context.
	 * @param cir     boilerplate
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
			value = "INVOKE_ASSIGN"
		),
		method = "useOnBlock"
	)
	private void captureTreeGeneration(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		World world = context.getWorld();
		((CaptureTreeGenerationAccessor)world).whiteout$setCaptureTreeGeneration(false);
	}
}
