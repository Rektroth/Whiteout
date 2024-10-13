/*
 * Patch for MC-244739
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc244739;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GoatEntity.class)
public abstract class GoatEntityMixin {
	@Unique
	private boolean isBreedingItem = false;

	@Shadow
	public abstract boolean isBreedingItem(ItemStack stack);

	/**
	 * Checks if the item the player is holding is a breeding item before the breeding occurs.
	 * @param player    boilerplate
	 * @param hand      boilerplate
	 * @param cir       boilerplate
	 * @param itemStack The item the player is holding.
	 */
	@Inject(
		at = @At(
			target = "Lnet/minecraft/entity/passive/AnimalEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
			value="INVOKE"
		),
		method = "interactMob"
	)
	private void checkIsBreedingItem(
		PlayerEntity player,
		Hand hand,
		CallbackInfoReturnable<ActionResult> cir,
		@Local(ordinal = 0) ItemStack itemStack
	) {
		isBreedingItem = this.isBreedingItem(itemStack);
	}

	/**
	 * Checks if the player was holding a breeding item *before* the breeding occurred.
	 * @param instance boilerplate
	 * @param stack    boilerplate
	 * @return True if the player was holding a breeding item before breeding occurred, false otherwise.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/entity/passive/GoatEntity;isBreedingItem(Lnet/minecraft/item/ItemStack;)Z",
			value = "INVOKE"
		),
		method = "interactMob"
	)
	private boolean isBreedingItem(GoatEntity instance, ItemStack stack) {
		return isBreedingItem;
	}
}
