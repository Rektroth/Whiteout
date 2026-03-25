package io.github.rektroth.whiteout.mixin.mc174630;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.BeaconMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Beacon menu modifications for MC-174630 patch.
 */
@Mixin(BeaconMenu.class)
public abstract class BeaconMenuMixin {
	/**
	 * Modifies the target method to not set a secondary effect when the provided secondary effect is both not
	 * regeneration and not the same effect as the primary.
	 * @param primary    The primary status effect.
	 * @param secondary  The secondary status effect.
	 * @param ci         boilerplate
	 * @param secondary2 Local reference to update the secondary status effect.
	 */
	@Inject(at = @At("HEAD"), method = "updateEffects")
	private void validateSecondaryPower(
		final Optional<Holder<MobEffect>> primary,
		final Optional<Holder<MobEffect>> secondary,
		CallbackInfo ci,
		@Local(argsOnly = true, ordinal = 1) LocalRef<Optional<Holder<MobEffect>>> secondary2
	) {
		if (secondary.isPresent() && secondary.get() != MobEffects.REGENERATION && primary.isPresent() && secondary.get() != primary.get()) {
			secondary2.set(Optional.empty());
		}
	}
}
