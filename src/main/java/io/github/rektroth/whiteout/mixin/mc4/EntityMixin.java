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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	/**
	 * Rounds the item entity's coordinate to the nearest 1 / 4096, which is what the client prefers.
	 * @param coord The item entity's coordinate.
	 * @return The item entity's coordinate rounded to the nearest 1 / 4096.
	 */
	@ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "setPos(DDD)V", ordinal = 0)
	private double fixItemXPositionDesync(double coord){
		return (Entity)(Object)this instanceof ItemEntity ? MathHelper.lfloor(coord * 4096.0D) * (1 / 4096.0D) : coord;
	}
}
