package io.github.rektroth.whiteout.mixin.mc264285;

import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Creeper modifications for the MC-264285 patch.
 */
@Mixin(Creeper.class)
public class CreeperMixin {
	/**
	 * Redirects the check to see if the item is damageable to instead check if the item has a maximum damage not equal
	 * to zero.
	 * @param instance The item.
	 * @return True if the item's maximum damage is not equal to zero; false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z",
			value = "INVOKE"
		),
		method = "mobInteract"
	)
	private boolean isMaxDamageNotEqualToZero(ItemStack instance) {
		return instance.getMaxDamage() != 0;
	}
}
