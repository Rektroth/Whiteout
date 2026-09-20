package io.github.rektroth.whiteout.mixin.mc297169;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Happy ghast modifications for MC-297169 patch.
 */
@Mixin(HappyGhast.class)
public abstract class HappyGhastMixin extends Animal {
	/**
	 * boilerplate
	 * @param type  boilerplate
	 * @param level boilerplate
	 */
	protected HappyGhastMixin(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	/**
	 * Makes the happy ghast play the mount sound itself instead of the level,
	 * so that it only plays if the ghast isn't silenced.
	 * @param instance boilerplate
	 */

	/**
	 * Makes the happy ghast play the mount sound itself instead of the level,
	 * so that it only plays if the ghast isn't silenced.
	 * @param instance   boilerplate
	 * @param soundEvent The sound event of the ghast's harness goggles being moved down.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/entity/animal/happyghast/HappyGhast;playSound(Lnet/minecraft/sounds/SoundEvent;)V",
			value = "INVOKE"
		),
		method = "addPassenger"
	)
	private void onlyPlaySoundOnMountIfNotSilenced(HappyGhast instance, SoundEvent soundEvent) {
		this.playSound(soundEvent, 1.0F, 1.0F);
	}

	/**
	 * Makes the happy ghast play the dismount sound itself instead of the level,
	 * so that it only plays if the ghast isn't silenced.
	 * @param instance   boilerplate
	 * @param soundEvent The sound event of the ghast's harness goggles being moved up.
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/entity/animal/happyghast/HappyGhast;playSound(Lnet/minecraft/sounds/SoundEvent;)V",
			value = "INVOKE"
		),
		method = "removePassenger"
	)
	private void onlyPlaySoundOnDismountIfNotSilenced(HappyGhast instance, SoundEvent soundEvent) {
		this.playSound(soundEvent, 1.0F, 1.0F);
	}
}
