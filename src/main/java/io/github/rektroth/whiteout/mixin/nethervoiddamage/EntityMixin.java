/*
 * Patch for top of nether void damage
 *
 * Authored for CraftBukkit/Spigot by Zach Brown <zach.brown@destroystokyo.com> on March 1, 2016.
 * Ported to Fabric by Rektroth <brian.rexroth.jr@gmail.com> on April 28, 2024.
 */

package io.github.rektroth.whiteout.mixin.nethervoiddamage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

	/**
	 * Triggers a tick in the void if the entity is not an invulnerable player and is above the dimension ceiling.
	 * @param ci boilerplate
	 */
	@Inject(at = @At("TAIL"), method = "attemptTickInVoid")
	protected void attemptTickInRoofVoid(CallbackInfo ci) {
		if (!this.world.isClient()
			&& this.world.getDimension().hasCeiling()
			&& this.getY() > this.world.getBottomY() + ((ServerWorld)this.world).getLogicalHeight() - 1
			&& (!((Entity)(Object)this instanceof PlayerEntity player) || !player.getAbilities().invulnerable)
		) {
			this.tickInVoid();
		}
	}
}
