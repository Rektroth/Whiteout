package io.github.rektroth.whiteout.mixin.mc159283;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// compiler warning says that the target is public - this is incorrect, ignore it
// i can't figure out how to suppress it

/**
 * End island density function modifications for MC-159283 patch.
 */
@Mixin(targets = "net.minecraft.world.level.levelgen.DensityFunctions$EndIslandDensityFunction")
public abstract class EndIslandDensityFunctionMixin {
	/**
	 * Calculates the same sqrt value, but with parameters `x` and `z` cast as longs to avoid an overflow.
	 * @param value boilerplate
	 */
	@Redirect(
		at = @At(ordinal = 0, target = "Lnet/minecraft/util/Mth;sqrt(F)F", value = "INVOKE"),
		method = "getHeightValue"
	)
	private static float sqrtWithLongs(
		float value,
		@Local(argsOnly = true, ordinal = 0) int x,
		@Local(argsOnly = true, ordinal = 1) int z
	) {
		return Mth.sqrt((long) x * (long) x + (long) z * (long) z);
	}
}
