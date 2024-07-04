/*
 * Patch for top of nether void damage
 *
 * Authored for CraftBukkit/Spigot by Zach Brown <zach.brown@destroystokyo.com> on March 1, 2016.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.nethervoiddamage.compat.lithium;

import io.github.rektroth.whiteout.util.PortalUtil;
//import me.jellysquid.mods.lithium.common.util.POIRegistryEntries;
//import me.jellysquid.mods.lithium.common.world.interests.PointOfInterestStorageExtended;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.dimension.PortalForcer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
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
	private ServerWorld world;

	/**
	 * Gets the rectangle of the closest valid existing portal in the opposite dimension if one exists within
	 * the valid range.
	 * @param pos          The position of the portal.
	 * @param destIsNether Whether the destination is in the nether.
	 * @param worldBorder  The world border.
	 * @return The rectangle of the closest valid existing portal if one exists within the valid range.
	 * @author Rektroth
	 * @reason Lithium completely overwrites this function with essentially what you see below,
	 * but without the `PortalUtil.isBelowCeiling` check added to the predicate.
	 * Easiest way to achieve compatibility with Lithium in this case is to simply overwrite the method ourselves.
	 */
	/*@Overwrite
	public Optional<BlockLocating.Rectangle> getPortalPos(
		BlockPos pos,
		boolean destIsNether,
		WorldBorder worldBorder
	) {
		int searchRadius = destIsNether ? 16 : 128;
		PointOfInterestStorage poiStorage = this.world.getPointOfInterestStorage();
		poiStorage.preloadChunks(this.world, pos, searchRadius);

		// all the same above

		Optional<PointOfInterest> ret = ((PointOfInterestStorageExtended)poiStorage).lithium$findNearestForPortalLogic(
			pos,
			searchRadius,
			POIRegistryEntries.NETHER_PORTAL_ENTRY,
			PointOfInterestStorage.OccupationStatus.ANY,
			(poi) -> PortalUtil.isBelowCeiling(poi, this.world) // this line is what's important
				&& this.world.getBlockState(poi.getPos()).contains(Properties.HORIZONTAL_AXIS),
			worldBorder);

		return ret.map((poi) -> {
			BlockPos blockPos = poi.getPos();
			this.world.getChunkManager().addTicket(ChunkTicketType.PORTAL, new ChunkPos(blockPos), 3, blockPos);
			BlockState blockState = this.world.getBlockState(blockPos);

			return BlockLocating.getLargestRectangle(
				blockPos,
				blockState.get(Properties.HORIZONTAL_AXIS),
				21,
				Direction.Axis.Y,
				21,
				(posx) -> this.world.getBlockState(posx) == blockState);
		});
	}*/

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
