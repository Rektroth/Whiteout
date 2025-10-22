package io.github.rektroth.whiteout.mixin.mc264285;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {
	/**
	 * Redirects the check to see if the item is damageable to instead check if the item has a maximum damage not equal
	 * to zero.
	 * @param instance The item.
	 * @return True if the item's maximum damage is not equal to zero; false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/item/ItemStack;isDamageable()Z",
			value = "INVOKE"
		),
		method = "interactMob"
	)
	private boolean isMaxDamageNotEqualToZero(ItemStack instance) {
		return instance.getMaxDamage() != 0;
	}
}
