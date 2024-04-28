/*
 * Patch for MC-4
 * 
 * Authored for CraftBukkit/Spigot by BillyGalbreath <blake.galbreath@gmail.com> on December 8, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on July 11, 2023.
 */

/*
 * Patch for top of nether void damage
 *
 * Authored for CraftBukkit/Spigot by Zach Brown <zach.brown@destroystokyo.com> on March 1, 2016.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	private World world;

	@Shadow
	protected abstract void tickInVoid();

	@Final
	@Shadow
	public abstract double getY();

	// `(Entity)(Object)this` sort of "tricks" the compiler
	// https://fabricmc.net/wiki/tutorial:mixin_examples#access_the_this_instance_of_the_class_your_mixin_is_targeting
	// IDE will likely say the code after the check is unreachable, but it isn't

	@ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 0)
	private double fixItemXPositionDesync(double x){
		return (Entity)(Object)this instanceof ItemEntity ? MathHelper.lfloor(x * 4096.0D) * (1 / 4096.0D) : x;
	}

	@ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 1)
	private double fixItemYPositionDesync(double y){
		return (Entity)(Object)this instanceof ItemEntity ? MathHelper.lfloor(y * 4096.0D) * (1 / 4096.0D) : y;
	}

	@ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 2)
	private double fixItemZPositionDesync(double z){
		return (Entity)(Object)this instanceof ItemEntity ? MathHelper.lfloor(z * 4096.0D) * (1 / 4096.0D) : z;
	}

	@Inject(at = @At("TAIL"), method = "attemptTickInVoid")
	protected void attemptTickInRoofVoid(CallbackInfo ci) {
		if (!this.world.isClient
			&& this.world.getDimension().hasCeiling()
			&& this.getY() > this.world.getBottomY() + ((ServerWorld)this.world).getLogicalHeight() - 1
			&& (!((Entity)(Object)this instanceof PlayerEntity player) || !player.getAbilities().invulnerable)
		) {
			this.tickInVoid();
		}
	}
}
