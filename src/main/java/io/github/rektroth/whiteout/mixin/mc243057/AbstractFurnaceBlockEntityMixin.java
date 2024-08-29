package io.github.rektroth.whiteout.mixin.mc243057;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
	@Final
	@Shadow
	protected static int INPUT_SLOT_INDEX;

	@Final
	@Shadow
	protected static int OUTPUT_SLOT_INDEX;

	@Shadow
	protected DefaultedList<ItemStack> inventory;

	/**
	 * @author Rektroth
	 * @reason The fix completely overhauls the method.
	 */
	@Overwrite
	public void provideRecipeInputs(RecipeMatcher finder) {
		finder.addInput(this.inventory.get(INPUT_SLOT_INDEX));
		finder.addInput(this.inventory.get(OUTPUT_SLOT_INDEX));
	}
}
