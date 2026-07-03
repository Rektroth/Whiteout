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
	 * @param except   boilerplate
	 * @param x        boilerplate
	 * @param y        boilerplate
	 * @param z        boilerplate
	 * @param sound    boilerplate
	 * @param source   boilerplate
	 * @param volume   boilerplate
	 * @param pitch    boilerplate
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
			value = "INVOKE"
		),
		method = "addPassenger"
	)
	private void onlyPlaySoundOnMountIfNotSilenced(
		Level instance,
		Entity except,
		double x,
		double y,
		double z,
		SoundEvent sound,
		SoundSource source,
		float volume,
		float pitch
	) {
		this.playSound(SoundEvents.HARNESS_GOGGLES_DOWN, 1.0F, 1.0F);
	}

	/**
	 * Makes the happy ghast play the dismount sound itself instead of the level,
	 * so that it only plays if the ghast isn't silenced.
	 * @param instance boilerplate
	 * @param except   boilerplate
	 * @param x        boilerplate
	 * @param y        boilerplate
	 * @param z        boilerplate
	 * @param sound    boilerplate
	 * @param source   boilerplate
	 * @param volume   boilerplate
	 * @param pitch    boilerplate
	 */
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
			value = "INVOKE"
		),
		method = "removePassenger"
	)
	private void onlyPlaySoundOnDismountIfNotSilenced(
		Level instance,
		Entity except,
		double x,
		double y,
		double z,
		SoundEvent sound,
		SoundSource source,
		float volume,
		float pitch
	) {
		this.playSound(SoundEvents.HARNESS_GOGGLES_DOWN, 1.0F, 1.0F);
	}
}
