package io.github.rektroth.whiteout.mixin.mc243057;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Abstract furnace block entity modifications for MC-243057 patch.
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
	@Final
	@Shadow
	protected static int SLOT_INPUT;

	@Final
	@Shadow
	protected static int SLOT_RESULT;

	@Shadow
	protected NonNullList<ItemStack> items;

	/**
	 * Makes the furnace only account the input and output stacks; ignores the fuel stack.
	 * @param contents The available item stack contents.
	 * @param ci       boilerplate
	 */
	@Inject(at = @At("HEAD"), method = "fillStackedContents")
	private void doNotAccountFuelStack(final StackedItemContents contents, CallbackInfo ci) {
		contents.accountStack(this.items.get(SLOT_INPUT));
		contents.accountStack(this.items.get(SLOT_RESULT));
	}
}
