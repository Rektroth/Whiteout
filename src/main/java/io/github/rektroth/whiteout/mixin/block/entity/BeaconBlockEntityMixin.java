/*
 * Patch for MC-153086
 *
 * Authored for CraftBukkit/Spigot by Jake Potrebic <jake.m.potrebic@gmail.com> on July 11, 2022.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 27, 2024.
 */

package io.github.rektroth.whiteout.mixin.block.entity;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
	@Shadow
	List<BeaconBlockEntity.BeamSegment> beamSegments;

	@Shadow
	int level;

	@Redirect(
		at = @At(
			target = "Lnet/minecraft/block/entity/BeaconBlockEntity;playSound(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;)V",
			value = "INVOKE"
		),
		method = "markRemoved"
	)
	private void playSoundIfNoBeam(World world, BlockPos pos, SoundEvent sound) {
		if (this.level > 0 && !this.beamSegments.isEmpty()) {
			BeaconBlockEntity.playSound(world, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE);
		}
	}
}
