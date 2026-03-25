package io.github.rektroth.whiteout.mixin.nethervoiddamage;

import io.github.rektroth.whiteout.util.PortalUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.portal.PortalForcer;
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
	private ServerLevel level;

	/**
	 * Filters points that are outside the logical boundary of the dimension out of a stream of points.
	 * @param instance  The stream of points to be filtered.
	 * @param predicate The predicate already being used to filter the points.
	 * @return The same stream of points with those outside the logical boundary removed.
	 */
	@Redirect(
		at = @At(
			target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;",
			value = "INVOKE",
			ordinal = 0
		),
		method = "findClosestPortalPosition"
	)
	private Stream<BlockPos> filterPointsOutsideLogic(
		Stream<BlockPos> instance,
		Predicate<? super BlockPos> predicate
	) {
		return instance
			.filter(predicate)
			.filter((pos) -> PortalUtil.isBelowCeiling(pos, this.level));
	}

	/**
	 * Returns the provided value or dimension's ceiling height, whichever is smallest.
	 * @param value The value.
	 * @return The value if below the dimension's ceiling or the dimension doesn't have one,
	 * the dimension's ceiling height otherwise.
	 */
	@ModifyVariable(at = @At("STORE"), method = "createPortal", ordinal = 0)
	private int roofMaximum(int value) {
		if (this.level.dimensionType().hasCeiling()) {
			return Math.min(value, this.level.getMinY() + this.level.getLogicalHeight() - 1);
		}

		return value;
	}
}
