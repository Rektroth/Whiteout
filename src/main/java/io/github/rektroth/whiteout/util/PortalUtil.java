package io.github.rektroth.whiteout.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

/**
 * Utilities for handling portals.
 */
public abstract class PortalUtil {
	/**
	 * Checks if a given point is below the dimension's ceiling (if it has one).
	 * @param pos         The position to check.
	 * @param world       The world the point is located in.
	 * @return            True if below the ceiling or the dimension does not have a ceiling, false otherwise.
	 */
	public static boolean isBelowCeiling(BlockPos pos, ServerLevel world) {
		return !(world.dimensionType().hasCeiling()
			&& pos.getY() > world.getMinY() + world.getLogicalHeight() - 1);
	}
}
