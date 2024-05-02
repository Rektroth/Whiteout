/*
 * Patch for MC-4
 * 
 * Authored for CraftBukkit/Spigot by BillyGalbreath <blake.galbreath@gmail.com> on December 8, 2020.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on July 11, 2023.
 */

package io.github.rektroth.whiteout.mixin.mc4;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Unique
	private double fixCoord(double d) {
		return (Entity)(Object)this instanceof ItemEntity ? MathHelper.lfloor(d * 4096.0D) * (1 / 4096.0D) : d;
	}

	// Unfortunately, I can only modify one variable with one function, and `@ModifyVariable` is non-repeatable.

	/**
	 * Rounds the item entity's X coordinate to the nearest 1 / 4096, which is what the client prefers.
	 * @param x The item entity's X coordinate.
	 * @return The item entity's X coordinate rounded to the nearest 1 / 4096.
	 */
	@ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 0)
	private double fixItemXPositionDesync(double x){
		return this.fixCoord(x);
	}

	/**
	 * Rounds the item entity's Y coordinate to the nearest 1 / 4096, which is what the client prefers.
	 * @param y The item entity's Y coordinate.
	 * @return The item entity's Y coordinate rounded to the nearest 1 / 4096.
	 */
	@ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 1)
	private double fixItemYPositionDesync(double y){
		return this.fixCoord(y);
	}

	/**
	 * Rounds the item entity's Z coordinate to the nearest 1 / 4096, which is what the client prefers.
	 * @param z The item entity's Z coordinate.
	 * @return The item entity's Z coordinate rounded to the nearest 1 / 4096.
	 */
	@ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 2)
	private double fixItemZPositionDesync(double z){
		return this.fixCoord(z);
	}
}
