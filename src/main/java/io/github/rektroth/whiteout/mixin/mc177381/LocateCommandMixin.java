package io.github.rektroth.whiteout.mixin.mc177381;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.command.LocateCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {
	/**
	 * Uses the Math.hypot method to calculate distance instead of a manual hypotenuse calculation using Math.sqrt.
	 * @param value boilerplate
	 * @param i     The distance on the x-axis.
	 * @param j     The distance on the y-axis.
	 * @return      The hypotenuse of i and j.
	 */
	@Redirect(
		at = @At(target = "Lnet/minecraft/util/math/MathHelper;sqrt(F)F", value = "INVOKE"),
		method = "getDistance"
	)
	private static float hypot(float value, @Local(ordinal = 4) int i, @Local(ordinal = 5) int j) {
		return (float)Math.hypot(i, j);
	}
}
