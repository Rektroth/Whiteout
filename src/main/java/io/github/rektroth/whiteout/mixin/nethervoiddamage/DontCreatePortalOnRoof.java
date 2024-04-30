/*
 * Patch for top of nether void damage
 *
 * Authored for CraftBukkit/Spigot by Zach Brown <zach.brown@destroystokyo.com> on March 1, 2016.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.nethervoiddamage;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PortalForcer.class)
public abstract class DontCreatePortalOnRoof {
	@Final
	@Shadow
	private ServerWorld world;

	/**
	 * Returns the provided value or dimension's ceiling height, whichever is smallest.
	 * @param value The value.
	 * @return The value if below the dimension's ceiling or the dimension doesn't have one,
	 * the dimension's ceiling height otherwise.
	 */
	@ModifyVariable(at = @At("STORE"), method = "createPortal", ordinal = 0)
	private int roofMaximum(int value) {
		if (this.world.getDimension().hasCeiling()) {
			return Math.min(value, this.world.getBottomY() + this.world.getLogicalHeight() - 1);
		}

		return value;
	}
}
