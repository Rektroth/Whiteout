package io.github.rektroth.whiteout.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.poi.PointOfInterest;

public abstract class PortalUtil {
	/**
	 * Checks if a given point is below the dimension's ceiling (if it has one).
	 * @param poi         The point to check.
	 * @param world       The world the point is located in.
	 * @return            True if below the ceiling or the dimension does not have a ceiling, false otherwise.
	 */
	public static boolean isBelowCeiling(PointOfInterest poi, ServerWorld world) {
		return !(world.getDimension().hasCeiling()
			&& poi.getPos().getY() > world.getBottomY() + world.getLogicalHeight() - 1);
	}
}
