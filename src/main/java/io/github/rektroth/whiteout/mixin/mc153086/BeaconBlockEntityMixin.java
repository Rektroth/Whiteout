package io.github.rektroth.whiteout.mixin.mc153086;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

/**
 * Beacon block entity modifications for MC-153086 patch.
 */
@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
	@Shadow
    private List<BeaconBeamOwner.Section> beamSections;

	@Shadow
    private int levels;

	/**
	 * Redirects the target method to only play the sound if the beacon is activated.
	 * @param level The world the beacon is in.
	 * @param pos   The position of the beacon in the world.
	 * @param sound The sound to play.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;playSound(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;)V",
			value = "INVOKE"
		),
		method = "setRemoved"
	)
	private void playSoundIfNoBeam(Level level, BlockPos pos, SoundEvent sound) {
		if (this.levels > 0 && !this.beamSections.isEmpty()) {
			BeaconBlockEntity.playSound(level, pos, sound);
		}
	}
}
