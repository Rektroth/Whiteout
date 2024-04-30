/*
 * Patch for top of nether void damage
 *
 * Authored for CraftBukkit/Spigot by Zach Brown <zach.brown@destroystokyo.com> on March 1, 2016.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.nethervoiddamage;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.rektroth.whiteout.util.PortalUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PortalForcer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(PortalForcer.class)
public abstract class DontLinkToPortalOnRoof {
	@Final
	@Shadow
	private ServerWorld world;

	/**
	 * Filters points that are outside the logical boundary of the dimension out of a stream of points.
	 * @param instance    The stream of points to be filtered.
	 * @param predicate   The predicate already being used to filter the points.
	 * @param worldBorder The world border.
	 * @return The same stream of points with those outside the logical boundary removed.
	 */
	@Redirect(
		at = @At(
			target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;",
			value = "INVOKE",
			ordinal = 0
		),
		method = "getPortalRect"
	)
	private Stream<PointOfInterest> filterPointsOutsideLogic(
		Stream<PointOfInterest> instance,
		Predicate<? super PointOfInterest> predicate,
		@Local(argsOnly = true) WorldBorder worldBorder
	) {
		return instance
			.filter(predicate)
			.filter((poi) -> PortalUtil.isBelowCeiling(poi, this.world));
	}
}
