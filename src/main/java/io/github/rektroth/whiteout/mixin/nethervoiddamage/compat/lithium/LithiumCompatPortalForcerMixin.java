package io.github.rektroth.whiteout.mixin.nethervoiddamage.compat.lithium;

import io.github.rektroth.whiteout.util.PortalUtil;
import net.caffeinemc.mods.lithium.common.util.POIRegistryEntries;
import net.caffeinemc.mods.lithium.common.world.interests.PointOfInterestStorageExtended;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(PortalForcer.class)
public abstract class LithiumCompatPortalForcerMixin {
	@Final
	@Shadow
	private ServerLevel level;

	/**
	 * Gets the rectangle of the closest valid existing portal in the opposite dimension if one exists within
	 * the valid range.
	 * @param approximateExitPos The approximate position of the portal.
	 * @param toNether           Whether the destination is in the nether.
	 * @param worldBorder        The world border.
	 * @return The rectangle of the closest valid existing portal if one exists within the valid range.
	 * @author Rektroth
	 * @reason Lithium completely overwrites this function with essentially what you see below,
	 * but without the `PortalUtil.isBelowCeiling` check added to the predicate.
	 * Easiest way to achieve compatibility with Lithium in this case is to simply overwrite the method ourselves.
	 */
	@Overwrite
	public Optional<BlockPos> findClosestPortalPosition(BlockPos approximateExitPos, boolean toNether, WorldBorder worldBorder) {
		int searchRadius = toNether ? 16 : 128;

		PointOfInterestStorage poiStorage = this.level.getPointOfInterestStorage();
		poiStorage.preloadChunks(this.level, approximateExitPos, searchRadius);

		Optional<PointOfInterest> ret = ((PointOfInterestStorageExtended) poiStorage).lithium$findNearestForPortalLogic(
			approximateExitPos,
			searchRadius,
			POIRegistryEntries.NETHER_PORTAL_ENTRY,
			PointOfInterestStorage.OccupationStatus.ANY,
			(poi) -> PortalUtil.isBelowCeiling(poi.getPos(), this.level) // this line is what's important
				&& this.level.getBlockState(poi.getPos()).contains(Properties.HORIZONTAL_AXIS),
			worldBorder
		);

		return ret.map(PointOfInterest::getPos);
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
