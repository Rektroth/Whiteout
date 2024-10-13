/*
 * Patch for MC-159283
 *
 * Authored for CraftBukkit/Spigot by David Slovikosky <davidslovikosky@gmail.com> on June 9, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on July 4, 2024.
 */

package io.github.rektroth.whiteout.mixin.mc159283;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// compiler warning says that the target is public - this is incorrect, ignore it
// i can't figure out how to suppress it
@Mixin(targets = "net.minecraft.world.gen.densityfunction.DensityFunctionTypes$EndIslands")
public abstract class EndIslandsMixin {
	/**
	 * Calculates the same sqrt value, but with parameters `x` and `z` cast as longs to avoid an overflow.
	 * @param value boilerplate
	 */
	@Redirect(
		at = @At(ordinal = 0, target = "Lnet/minecraft/util/math/MathHelper;sqrt(F)F", value = "INVOKE"),
		method = "sample(Lnet/minecraft/util/math/noise/SimplexNoiseSampler;II)F"
	)
	private static float sqrtWithLongs(
		float value,
		@Local(argsOnly = true, ordinal = 0) int x,
		@Local(argsOnly = true, ordinal = 1) int z
	) {
		return MathHelper.sqrt((long) x * (long) x + (long) z * (long) z);
	}
}
