/*
 * Patch for top of nether void damage
 *
 * Authored for CraftBukkit/Spigot by Zach Brown <zach.brown@destroystokyo.com> on March 1, 2016.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PortalForcer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
	@Final
	@Shadow
	private ServerWorld world;

	@Redirect(
		at = @At(
			target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;",
			value = "INVOKE"
		),
		method = "getPortalRect"
	)
	private Stream<PointOfInterest> checkPortalInRoofVoid(
		Stream<PointOfInterest> instance,
		Predicate<? super PointOfInterest> predicate,
		@Local(argsOnly = true) WorldBorder worldBorder
	) {
		return instance.filter((poi) ->
			worldBorder.contains(poi.getPos())
			&& !(this.world.getDimension().hasCeiling()
			&& poi.getPos().getY() > this.world.getBottomY() + this.world.getLogicalHeight() - 1));
	}

	@ModifyVariable(at = @At("STORE"), method = "createPortal")
	private int dontCreatePortalInRoofVoid(int value) {
		if (this.world.getDimension().hasCeiling()) {
			return Math.min(value, this.world.getBottomY() + this.world.getLogicalHeight() - 1);
		}

		return value;
	}
}
