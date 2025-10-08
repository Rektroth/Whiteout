/*
 * Patch for breaking permanent blocks
 *
 * Authored for CraftBukkit/Spigot by Aikar <aikar@aikar.co>> on May 13, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DispenserBehavior.class)
public interface DispenserBehaviorMixin {
	/**
	 * Captures tree generation on use of bone meal by a dispenser.
	 * @param provider The item being used by the dispenser.
	 * @param behavior The default dispenser behavior.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/block/DispenserBlock;registerBehavior(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/block/dispenser/DispenserBehavior;)V",
			value = "INVOKE",
			ordinal = 35
		),
		method = "registerDefaults"
	)
	private static void captureTreeGeneration(ItemConvertible provider, DispenserBehavior behavior) {
		DispenserBlock.registerBehavior(provider, new FallibleItemDispenserBehavior() {
			protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				this.setSuccess(true);
				World world = pointer.world();
				BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));

				((CaptureTreeGenerationAccessor)world).whiteout$setCaptureTreeGeneration(true);

				if (!BoneMealItem.useOnFertilizable(stack, world, blockPos) && !BoneMealItem.useOnGround(stack, world, blockPos, null)) {
					this.setSuccess(false);
				} else if (!world.isClient()) {
					world.syncWorldEvent(1505, blockPos, 15);
				}

				((CaptureTreeGenerationAccessor)world).whiteout$setCaptureTreeGeneration(false);

				return stack;
			}
		});
	}
}
