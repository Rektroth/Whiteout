package io.github.rektroth.whiteout.mixin.nethervoiddamage;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	private Level level;

	@Shadow
	protected abstract void onBelowWorld();

	@Final
	@Shadow
	public abstract double getY();

	/**
	 * Triggers a tick in the void if the entity is not an invulnerable player and is above the dimension ceiling.
	 * @param ci boilerplate
	 */
	@Inject(at = @At("TAIL"), method = "checkBelowWorld")
	protected void attemptTickInRoofVoid(CallbackInfo ci) {
		if (!this.level.isClientSide()
			&& this.level.dimensionType().hasCeiling()
			&& this.getY() > this.level.getMinY() + ((ServerLevel)this.level).getLogicalHeight() - 1
			&& (!((Entity)(Object)this instanceof Player player) || !player.getAbilities().invulnerable)
		) {
			this.onBelowWorld();
		}
	}
}
