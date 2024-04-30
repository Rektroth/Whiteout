/*
 * Patch for top of nether void damage
 *
 * Authored for CraftBukkit/Spigot by Zach Brown <zach.brown@destroystokyo.com> on March 1, 2016.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 * Made compatible with Lithium by Rektroth <brian.rexroth.jr@gmail.com> on April 29, 2024
 */

package io.github.rektroth.whiteout.mixin.nethervoiddamage.compat.lithium;

import io.github.rektroth.whiteout.util.PortalUtil;
import me.jellysquid.mods.lithium.common.util.POIRegistryEntries;
import me.jellysquid.mods.lithium.common.world.interests.PointOfInterestStorageExtended;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.PortalForcer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(PortalForcer.class)
public abstract class DontLinkToPortalOnRoof {
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
	 * @reason Unfortunately, since Lithium completely overwrites this function rather than doing something reasonable,
	 * I have no choice but to completely overwrite their overwrite.
	 * What's important is the first line of the predicate passed to `lithium$findNearestForPortalLogic`,
	 * which prevents portals in the overworld from linking to portals that exist above the nether roof.
	 */
	@Overwrite
	public Optional<BlockLocating.Rectangle> getPortalRect(
		BlockPos pos,
		boolean destIsNether,
		WorldBorder worldBorder
	) {
		int searchRadius = destIsNether ? 16 : 128;
		PointOfInterestStorage poiStorage = this.world.getPointOfInterestStorage();
		poiStorage.preloadChunks(this.world, pos, searchRadius);

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
	}
}
