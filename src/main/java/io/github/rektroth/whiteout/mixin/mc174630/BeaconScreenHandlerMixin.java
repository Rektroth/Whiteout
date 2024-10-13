/*
 * Patch for MC-174630
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on October 13, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc174630;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.BeaconScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BeaconScreenHandler.class)
public abstract class BeaconScreenHandlerMixin {
	/**
	 * Modifies the target method to not set a secondary effect when the provided secondary effect is both not
	 * regeneration and not the same effect as the primary.
	 * @param primary    The primary status effect.
	 * @param secondary  The secondary status effect.
	 * @param ci         boilerplate
	 * @param secondary2 Local reference to update the secondary status effect.
	 */
	@Inject(at = @At("HEAD"), method = "setEffects")
	private void validateSecondaryPower(
		Optional<RegistryEntry<StatusEffect>> primary,
		Optional<RegistryEntry<StatusEffect>> secondary,
		CallbackInfo ci,
		@Local(argsOnly = true, ordinal = 1) LocalRef<Optional<RegistryEntry<StatusEffect>>> secondary2
	) {
		if (secondary.isPresent() && secondary.get() != StatusEffects.REGENERATION && primary.isPresent() && secondary.get() != primary.get()) {
			secondary2.set(Optional.empty());
		}
	}
}
