/*
 * Patch for MC-273635
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on August 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc273635;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.rektroth.whiteout.util.TrialSpawnerDataResettableWithLogic;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TrialSpawnerState.class)
public class TrialSpawnerStateMixin {
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/block/spawner/TrialSpawnerData;deactivate()V",
			value = "INVOKE"
		),
		method = "tick"
	)
	private void syncWorldEventWithOldDoorState(
		TrialSpawnerData instance,
		@Local(argsOnly = true) TrialSpawnerLogic logic
	) {
		((TrialSpawnerDataResettableWithLogic)instance).deactivate(logic);
	}
}
