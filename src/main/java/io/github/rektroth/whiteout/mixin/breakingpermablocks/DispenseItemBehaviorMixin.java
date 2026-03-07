package io.github.rektroth.whiteout.mixin.breakingpermablocks;

import io.github.rektroth.whiteout.accessors.CaptureTreeGenerationAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DispenseItemBehavior.class)
public interface DispenseItemBehaviorMixin {
	/**
	 * Captures tree generation on use of bone meal by a dispenser.
	 * @param item     The item being used by the dispenser.
	 * @param behavior The default dispenser behavior.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/level/block/DispenserBlock;registerBehavior(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/core/dispenser/DispenseItemBehavior;)V",
			value = "INVOKE",
			ordinal = 33
		),
		method = "bootStrap"
	)
	private static void captureTreeGeneration(ItemLike item, DispenseItemBehavior behavior) {
		DispenserBlock.registerBehavior(item, new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(final BlockSource source, final ItemStack dispensed) {
				this.setSuccess(true);
				Level level = source.level();
				BlockPos blockPos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));

				((CaptureTreeGenerationAccessor)level).whiteout$setCaptureTreeGeneration(true);

				if (!BoneMealItem.growCrop(dispensed, level, blockPos) && !BoneMealItem.growWaterPlant(dispensed, level, blockPos, null)) {
					this.setSuccess(false);
				} else if (!level.isClientSide()) {
					level.levelEvent(1505, blockPos, 15);
				}

				((CaptureTreeGenerationAccessor)level).whiteout$setCaptureTreeGeneration(false);

				return dispensed;
			}
		});
	}
}
