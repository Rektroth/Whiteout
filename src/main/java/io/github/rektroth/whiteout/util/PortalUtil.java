package io.github.rektroth.whiteout.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class PortalUtil {
	/**
	 * Checks if a given point is below the dimension's ceiling (if it has one).
	 * @param pos         The position to check.
	 * @param world       The world the point is located in.
	 * @return            True if below the ceiling or the dimension does not have a ceiling, false otherwise.
	 */
	public static boolean isBelowCeiling(BlockPos pos, ServerWorld world) {
		return !(world.getDimension().hasCeiling()
			&& pos.getY() > world.getBottomY() + world.getLogicalHeight() - 1);
	}
}
